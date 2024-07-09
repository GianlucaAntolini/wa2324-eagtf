
package it.unipd.dei.webapp.servlet;

import it.unipd.dei.webapp.dao.GetRankedRecipesDAO;
import it.unipd.dei.webapp.resource.Actions;
import it.unipd.dei.webapp.resource.Message;
import it.unipd.dei.webapp.resource.Recipe;

import java.io.IOException;

import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Return an the ranked list of the recipes based on their number of likes
 */
public final class RankedRecepiesServlet extends AbstractDatabaseServlet {

    /**
     * Return an the ranked list of the recipes based on their number of likes
     *
     * @param req the HTTP request from the client.
     * @param res the HTTP response from the server.
     * 
     * @throws ServletException if any error occurs while executing the servlet.
     * @throws IOException      if any error occurs in the client/server
     *                          communication.
     */

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // set the stuff in the logger
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setAction(Actions.SEARCH_USER_BY_EMAIL);

        // model
        List<Recipe> recipes = null;
        Message m = null;
        // default size of the query
        int size = 15;

        try {
            // checks if the length we want for the query
            if (req.getParameter("quelen") != null & req.getParameter("quelen") != "") {
                try {
                    size = Integer.parseInt(req.getParameter("quelen"));
                } catch (NumberFormatException e) {
                    LOGGER.error("parameter queryLength is not an int " + req.getParameter("quelen"));
                }
            }

            recipes = new GetRankedRecipesDAO(getConnection(), size).access().getOutputParam();

            req.setAttribute("name_table", "Ranked list recipes:");
            req.setAttribute("empty_table_message", "empty or broken list ");
            req.setAttribute("recipesList", recipes);
            req.setAttribute("message", m);
            // send to the jsp for formatting
            req.getRequestDispatcher("/jsp/include/recipes_list_ranked.jsp").include(req, res);

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
