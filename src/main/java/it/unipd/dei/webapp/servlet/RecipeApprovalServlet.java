
package it.unipd.dei.webapp.servlet;

import it.unipd.dei.webapp.dao.GetRecipesToBeApprovedDAO;
import it.unipd.dei.webapp.resource.Actions;
import it.unipd.dei.webapp.resource.Message;
import it.unipd.dei.webapp.resource.Recipe;

import java.io.IOException;

import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.logging.log4j.message.StringFormattedMessage;

/**
 * Get the list of pending recipes and reject or approve them.
 */
public final class RecipeApprovalServlet extends AbstractDatabaseServlet {

    /**
     * Get the list of pending recipes and build the page.
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
        LogContext.setAction(Actions.SEARCH_RECIPES_TO_BE_APPROVED);

        // model
        List<Recipe> recipes = null;
        Message m = null;

        try {

            // retrieve the user id from the session
            HttpSession session = req.getSession(false);

            if (session == null || session.getAttribute("user_id") == null) {
                // not logged, redirect to index.html
                res.sendRedirect(req.getContextPath());
                LOGGER.error("User tried to search for recipes without being logged in.");

                LogContext.removeIPAddress();
                LogContext.removeAction();
                LogContext.removeUser();
                return;

            }

            // check whether the user is admin
            if ((int) session.getAttribute("role_id") != 0) {
                // not admin, redirect to home
                res.sendRedirect(req.getContextPath());
                LOGGER.error("User tried to search for recipes without being admin.");
                LogContext.removeIPAddress();
                LogContext.removeAction();
                LogContext.removeUser();
                return;
            }

            // creates a new object for accessing the database and searching the for the
            // recipes
            recipes = new GetRecipesToBeApprovedDAO(getConnection()).access().getOutputParam();

            m = new Message(String.format("Recipes successfully searched."));

            LOGGER.info("Recipes successfully searched.");

        } catch (SQLException ex) {
            m = new Message("Cannot search for recipes: unexpected error while accessing the database.", "E100",
                    ex.getMessage());
            LOGGER.error("Cannot search for recipes: unexpected error while accessing the database.", ex);
        }

        try {
            // stores the employee list and the message as a request attribute
            req.setAttribute("recipesToBeApprovedList", recipes);
            req.setAttribute("message", m);

            // forwards the control to the to-be-approved-recipes-list.jsp
            req.getRequestDispatcher("/jsp/to-be-approved-recipes-list.jsp").forward(req, res);

        } catch (Exception ex) {
            LOGGER.error(
                    new StringFormattedMessage(
                            "Unable to send response when searching for recipes to be approved."),
                    ex);
            throw ex;
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeUser();
        }
    }
}
