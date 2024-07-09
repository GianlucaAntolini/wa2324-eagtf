
package it.unipd.dei.webapp.servlet;

import it.unipd.dei.webapp.resource.Actions;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Check whether a user was already logged and closes the session.
 */
public final class LogoutUserServlet extends AbstractDatabaseServlet {

    /**
     * Check whether a user was already logged and closes the session.
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
        LogContext.setAction(Actions.LOGOUT_USER);

        int user_id = -1;

        // get the user's session
        HttpSession session = req.getSession(false);

        // if the session is not null
        if (session != null) {
            // get the current user id
            user_id = (int) session.getAttribute("user_id");

            // remove the session attributes
            session.removeAttribute("user_id");
            session.removeAttribute("role_id");

            // invalidate the session
            session.invalidate();
        }
        // redirect to home
        res.sendRedirect(req.getContextPath());

        LOGGER.info("User with id %d successfully logged out.", user_id);

        LogContext.removeIPAddress();
        LogContext.removeAction();
    }

}
