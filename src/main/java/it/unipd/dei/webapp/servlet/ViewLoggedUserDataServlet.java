package it.unipd.dei.webapp.servlet;

import it.unipd.dei.webapp.dao.ViewLoggedUserDataDAO;
import it.unipd.dei.webapp.resource.Actions;
import it.unipd.dei.webapp.resource.User;

import java.io.IOException;

import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.logging.log4j.message.StringFormattedMessage;

/**
 * Build and access the user data access page.
 */
public final class ViewLoggedUserDataServlet extends AbstractDatabaseServlet {

    /**
     * Build and access the user data access page.
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
        LogContext.setAction(Actions.VIEW_USER_DATA);

        int user_id = -1;

        User usr = null;

        try {

            // get the current user's session
            HttpSession session = req.getSession(false);

            // check whether the use is logged
            if (session == null || session.getAttribute("user_id") == null) {
                // not logged, redirect to index.html
                res.sendRedirect(req.getContextPath());
                return;
            }

            // get the id of the user
            user_id = (int) session.getAttribute("user_id");

            // creates a new object for accessing the database and searching the user
            usr = new ViewLoggedUserDataDAO(getConnection(), user_id).access().getOutputParam();

            LOGGER.info("User successfully searched by its id %d.", user_id);

        } catch (NullPointerException ex) {
            LOGGER.error("Cannot search for user: session may not be available.", ex);
        } catch (SQLException ex) {
            LOGGER.error("Cannot search for user: unexpected error while accessing the database.", ex);
        }

        try {
            // stores the user as a request attribute
            req.setAttribute("user", usr);

            // forwards the control to the view_and_update_user_data JSP
            req.getRequestDispatcher("/jsp/view_and_update_user_data.jsp").forward(req, res);

        } catch (Exception ex) {
            LOGGER.error(new StringFormattedMessage("Unable to send response when creating user %d.", user_id), ex);
            throw ex;
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeUser();
        }
    }

}