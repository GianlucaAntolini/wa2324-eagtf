
package it.unipd.dei.webapp.servlet;

import it.unipd.dei.webapp.dao.SearchUserByEmailDAO;
import it.unipd.dei.webapp.resource.Actions;
import it.unipd.dei.webapp.resource.Message;
import it.unipd.dei.webapp.resource.User;

import java.io.IOException;

import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.logging.log4j.message.StringFormattedMessage;

/**
 * Search a user by his email.
 */
public final class SearchUserByEmailServlet extends AbstractDatabaseServlet {

    /**
     * Search a user by his email.
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
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setAction(Actions.SEARCH_USER_BY_EMAIL);

        // request parameter
        String email = null;

        // model
        List<User> usr = null;
        Message m = null;

        try {

            // get the current user's session
            HttpSession session = req.getSession(false);

            // check whether the user is logged
            if (session == null || session.getAttribute("user_id") == null) {
                // not logged, redirect to home
                res.sendRedirect(req.getContextPath());
                LOGGER.error("User tried to search for users without being logged in.");
                LogContext.removeIPAddress();
                LogContext.removeAction();
                LogContext.removeUser();
                return;
            }

            // check whether the user is admin
            if ((int) session.getAttribute("role_id") != 0) {
                // not admin, redirect to home
                res.sendRedirect(req.getContextPath());
                LOGGER.error("User tried to search for userss without being admin.");
                LogContext.removeIPAddress();
                LogContext.removeAction();
                LogContext.removeUser();
                return;
            }

            // retrieves the request parameter
            email = req.getParameter("email");

            // creates a new object for accessing the database and searching the for the
            // users
            usr = new SearchUserByEmailDAO(getConnection(), email).access().getOutputParam();

            m = new Message(String.format("User successfully searched."));

            LOGGER.info("User successfully searched by email %s.", email);

        } catch (SQLException ex) {
            m = new Message("Cannot search for users: unexpected error while accessing the database.", "E100",
                    ex.getMessage());
            LOGGER.error("Cannot search for users: unexpected error while accessing the database.", ex);
        }

        try {
            // stores the employee list and the message as a request attribute
            req.setAttribute("userList", usr);
            req.setAttribute("message", m);
            req.setAttribute("email", email);

            // forwards the control to the search-employee-result JSP
            req.getRequestDispatcher("/jsp/search-user-result.jsp").forward(req, res);

        } catch (Exception ex) {
            LOGGER.error(new StringFormattedMessage("Unable to send response when creating user %s.", email), ex);
            throw ex;
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeUser();
        }
    }

}