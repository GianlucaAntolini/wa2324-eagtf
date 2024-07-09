
package it.unipd.dei.webapp.servlet;

import it.unipd.dei.webapp.dao.LoginUserDAO;
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
 * Searches for the user in the db to grant login and creats the session.
 */
public final class LoginUserServlet extends AbstractDatabaseServlet {

    /**
     * Build and access the login page.
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
        LogContext.setAction(Actions.LOGIN_USER);

        try {
            // stores the user as a request attribute
            // req.setAttribute("err",true);
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, res);

        } catch (Exception ex) {
            LOGGER.error(new StringFormattedMessage("Unable to start login."), ex);
            throw ex;
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeUser();
        }
    }

    /**
     * Searches for the user in the db to grant login and creates the session.
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
        LogContext.setAction(Actions.LOGIN_USER);

        // request parameter
        String email = null;
        String password = null;

        // model
        User user = null;

        try {
            // retrieves the request parameter
            email = req.getParameter("email");
            password = req.getParameter("passwd");

            HttpSession session = req.getSession(false);
            // User not logged
            if (session != null && session.getAttribute("user_id") != null) {
                res.sendRedirect(req.getContextPath());
                LOGGER.info("The user with id %s is already logged in on the site with session id %s",
                        session.getAttribute("user_id"), session.getId());
                LogContext.removeIPAddress();
                LogContext.removeAction();
                return;
            }

            // creates a new object for accessing the database and login the user
            user = new LoginUserDAO(getConnection(), email, password).access().getOutputParam();

            // user successfully logged in, create the session and add the user_id
            if (user != null) {
                // check if user is banned
                if (user.getRole_id() == 2) {
                    // redirect to home without login
                    LOGGER.info("User with email %s is banned and can't login.", email);
                    req.setAttribute("err1", true);
                    req.getRequestDispatcher("/jsp/login.jsp").forward(req, res);
                    LogContext.removeIPAddress();
                    LogContext.removeAction();
                    return;
                }

                // create session
                session = req.getSession(true);

                session.setAttribute("user_id", user.getId());
                session.setAttribute("role_id", user.getRole_id());

                LOGGER.info("User with email %s and jsessionid %s successfully logged in.", email, session.getId());

                // send redirect to the index page
                res.sendRedirect(req.getContextPath());
            }
        } catch (SQLException ex) {

            LOGGER.error("User not found, email or password are incorrect", ex);

            // send user back to the login page
            // with an error
            req.setAttribute("err0", true);
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, res);

        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeUser();
        }

    }

}
