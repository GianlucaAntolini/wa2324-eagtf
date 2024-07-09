package it.unipd.dei.webapp.servlet;

import it.unipd.dei.webapp.dao.UpdateLoggedUserDataDAO;
import it.unipd.dei.webapp.resource.Actions;

import java.io.IOException;

import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.logging.log4j.message.StringFormattedMessage;

/**
 * Update the data of a logged user.
 */
public final class UpdateLoggedUserDataServlet extends AbstractDatabaseServlet {

    /**
     * Update the data of a logged user.
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
        LogContext.setAction(Actions.UPDATE_USER_DATA);

        String name = null;
        String surname = null;

        int user_id = -1;

        try {

            // get the current user's session
            HttpSession session = req.getSession(false);

            // get the id of the user
            user_id = (int) session.getAttribute("user_id");

            name = req.getParameter("name");
            surname = req.getParameter("surname");

            // creates a new object for accessing the database and searching the user
            new UpdateLoggedUserDataDAO(getConnection(), name, surname, user_id).access();

            LOGGER.info("User with id %d successfully updated.", user_id);

        } catch (SQLException ex) {
            LOGGER.error("Cannot update user data: unexpected error while accessing the database.", ex);
        }

        try {

            // redirect to index.html
            res.sendRedirect(req.getContextPath() + "/usr-data");

        } catch (Exception ex) {
            LOGGER.error(new StringFormattedMessage("Unable to send response when updating user %d.", user_id), ex);
            throw ex;
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeUser();
        }
    }

}
