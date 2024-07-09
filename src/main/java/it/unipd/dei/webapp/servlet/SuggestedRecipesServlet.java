
package it.unipd.dei.webapp.servlet;

import it.unipd.dei.webapp.dao.GetSuggestionForSearchDAO;
import it.unipd.dei.webapp.resource.Actions;
import it.unipd.dei.webapp.resource.Message;
import it.unipd.dei.webapp.resource.Recipe;

import it.unipd.dei.webapp.resource.Ingredient;
import it.unipd.dei.webapp.resource.Tag;

import java.io.IOException;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * return the HTML list of recipes with the closest number of Ingredients from
 * what asked from the user
 */
public final class SuggestedRecipesServlet extends AbstractDatabaseServlet {

    /**
     * return the HTML list of recipes with the closest number of Ingredients from
     * what asked from the user
     * 
     * @param req
     *            the HTTP request from the client.
     * @param res
     *            the HTTP response from the server.
     * 
     * @throws ServletException
     *                          if any error occurs while executing the servlet.
     * @throws IOException
     *                          if any error occurs in the client/server
     *                          communication.
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // set the stuff in the logger
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setAction(Actions.SEARCH_USER_BY_EMAIL);

        // model
        List<Recipe> recipes = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
        List<Tag> tags = new ArrayList<>();

        // default length of the query
        int size = 15;

        Message m = null;

        LOGGER.info("new SuggestedRecipesServlet get request");

        try {

            // retrive the arguments
            String usr_src_ing = req.getParameter("usr_src_ing");
            String usr_src_tag = req.getParameter("usr_src_tag");

            if (req.getParameter("quelen") != null & req.getParameter("quelen") != "") {
                try {
                    size = Integer.parseInt(req.getParameter("quelen"));
                } catch (NumberFormatException e) {
                    LOGGER.error("parameter queryLength is not an int " + req.getParameter("quelen"));
                }
            }

            // checks for the arguments of the servlet
            if (usr_src_ing != null & usr_src_tag != null) {
                // Split usr_src_ing into smaller strings
                String[] ingArray = usr_src_ing.split(",");
                // Split usr_src_tag into smaller strings
                String[] tagArray = usr_src_tag.split(",");

                // Iterate over ingArray, excluding the last element
                for (int i = 0; i < ingArray.length; i++) {
                    String ing = ingArray[i].trim(); // Trim to remove leading and trailing spaces
                    if (!ing.isEmpty()) {
                        // Do something with each ingredient (ing)
                        ingredients.add(new Ingredient(Integer.parseInt(ing), ""));
                    }
                }

                // Iterate over tagArray, excluding the last element
                for (int i = 0; i < tagArray.length; i++) {
                    String tag = tagArray[i].trim(); // Trim to remove leading and trailing spaces
                    if (!tag.isEmpty()) {
                        // Do something with each tag (tag)
                        tags.add(new Tag(Integer.parseInt(tag), ""));
                    }
                }

                recipes = new GetSuggestionForSearchDAO(getConnection(), ingredients, tags, size).access()
                        .getOutputParam();

                // update the value for the jsp page
                req.setAttribute("usr_src_ing", usr_src_ing);
                req.setAttribute("usr_src_tag", usr_src_tag);
                // setting the error in case the Dao does not return anything
                req.setAttribute("empty_table_message", "No suggestion found ");

            } else {
                // sets the value of the message if we didn't do any query(aka when we arrive to
                // the page)
                req.setAttribute("empty_table_message", "");
            }

            // set the table name
            req.setAttribute("name_table", "Ricette consigliate:");
            // stores the employee list and the message as a request attribute
            req.setAttribute("recipesList", recipes);
            req.setAttribute("message", m);
            req.getRequestDispatcher("/jsp/include/recipes_list_suggested.jsp").include(req, res);

        } catch (SQLException ex) {
            m = new Message("Cannot search for recipes: unexpected error while accessing the database.", "E100",
                    ex.getMessage());
            LOGGER.error("Cannot search for recipes: unexpected error while accessing the database.", ex);
        } catch (Exception ex) {
            m = new Message("Uknown exeption", "", ex.getMessage());
            LOGGER.error("Uknown exeption", ex);
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeUser();
        }

    }

}
