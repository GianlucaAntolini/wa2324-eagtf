package it.unipd.dei.webapp.servlet;

import it.unipd.dei.webapp.dao.GetRecipeByIdDAO;
import it.unipd.dei.webapp.dao.GetUserLikeRecipeDAO;
import it.unipd.dei.webapp.resource.Actions;
import it.unipd.dei.webapp.resource.Recipe;

import java.io.IOException;

import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.logging.log4j.message.StringFormattedMessage;

/**
 * Get and display the data related to a recipe.
 */
public final class RecipeDetailsServlet extends AbstractDatabaseServlet {

    /**
     * Get and display the data related to a recipe.
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

        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setAction(Actions.VIEW_RECIPE_DETAILS);

        Recipe r = null;
        int recipe_id = 38;
        Boolean like = null;

        try {

            recipe_id = Integer.valueOf(req.getParameter("recipeID"));

            // creates a new object for accessing the database and searching the recipe
            r = new GetRecipeByIdDAO(getConnection(), recipe_id).access().getOutputParam();

            HttpSession session = req.getSession(false);
            if (session != null && session.getAttribute("user_id") != null) {
                int user_id = (int) session.getAttribute("user_id");
                like = new GetUserLikeRecipeDAO(getConnection(), "" + recipe_id, "" + user_id).access()
                        .getOutputParam();

                LOGGER.info("Session active: retrived user like on recipe %d.", recipe_id);
            }

            LOGGER.info("Recipe successfully searched by its id %d.", recipe_id);

        } catch (NullPointerException ex) {
            LOGGER.error("Cannot search for recipe: session may not be available.", ex);
        } catch (SQLException ex) {
            LOGGER.error("Cannot search for recipe: unexpected error while accessing the database.", ex);
        } catch (NumberFormatException ex) {
            LOGGER.error("Cannot search for recipe: recipe id must be a valid number.", ex);
        }

        try {
            // stores the recipe as a request attribute
            req.setAttribute("recipe", r);

            if (like != null) {
                req.setAttribute("liked", like);

                HttpSession session = req.getSession(false);
                req.setAttribute("user_id", session.getAttribute("user_id"));

                LOGGER.info("Session active: saved user like (%b) on recipe %d.", like, recipe_id);
            }

            // forwards the control to the recipe_details JSP
            req.getRequestDispatcher("/jsp/recipe_details.jsp").forward(req, res);

        } catch (Exception ex) {
            LOGGER.error(new StringFormattedMessage("Unable to send response when showing recipe %d.", recipe_id), ex);
            throw ex;
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
        }
    }

}