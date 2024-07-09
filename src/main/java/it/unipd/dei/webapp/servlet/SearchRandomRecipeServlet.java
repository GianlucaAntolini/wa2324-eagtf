
package it.unipd.dei.webapp.servlet;

import it.unipd.dei.webapp.dao.GetRandomRecipeDAO;

import it.unipd.dei.webapp.resource.Actions;
import it.unipd.dei.webapp.resource.Recipe;

import java.io.IOException;

import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Get a random recipe from the database.
 */
public final class SearchRandomRecipeServlet extends AbstractDatabaseServlet {

    /**
     * Get a random recipe from the database.
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
        LogContext.setAction(Actions.SEARCH_USER_BY_EMAIL);

        // model
        Recipe recipe = null;

        try {

            // no need for session

            // creates a new object for accessing the database and searching the for the
            // recipe
            recipe = new GetRandomRecipeDAO(getConnection()).access().getOutputParam();

            LOGGER.info("Random recipe successfully searched.");

        } catch (SQLException ex) {

            LOGGER.error("Cannot search for recipes: unexpected error while accessing the database.", ex);

            // redirect to home page
            res.sendRedirect(req.getContextPath());
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeUser();
        }

        // redirect to the recipe page

        res.sendRedirect(req.getContextPath() + "/recipeDetails?recipeID=" + recipe.getId());

        LogContext.removeIPAddress();
        LogContext.removeAction();
        LogContext.removeUser();

    }

}