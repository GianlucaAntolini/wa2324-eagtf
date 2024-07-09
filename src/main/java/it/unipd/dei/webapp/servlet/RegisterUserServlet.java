
package it.unipd.dei.webapp.servlet;

import it.unipd.dei.webapp.dao.SearchUserByEmailDAO;
import it.unipd.dei.webapp.resource.Actions;
import it.unipd.dei.webapp.resource.User;

import java.io.IOException;

import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.http.HttpSession;

import org.apache.logging.log4j.message.StringFormattedMessage;

/**
 * Creates a new user into the database.
 */
public final class RegisterUserServlet extends AbstractDatabaseServlet {
    /**
     * Build and access the registration page.
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
        LogContext.setAction(Actions.ACCESS_REGISTRATION_PAGE);

        int user_id = -1;
        int role_id = -1;

        HttpSession session = null;

        try {
            // get the current user's session
            session = req.getSession(false);

            if (session != null && session.getAttribute("user_id") != null) {

                // logged, redirect to home.jsp
                user_id = (int) session.getAttribute("user_id");
                role_id = (int) session.getAttribute("role_id");
                req.getRequestDispatcher("/jsp/home.jsp").forward(req, res);
            }
        } catch (NullPointerException ex) {
            LOGGER.error("Cannot search for user: session may not be available.", ex);
        }

        try {
            // stores the user as a request attribute
            // req.setAttribute("err",true);
            req.getRequestDispatcher("/jsp/register.jsp").forward(req, res);

        } catch (Exception ex) {
            LOGGER.error(new StringFormattedMessage("Unable to send response when creating user %d with role %d.",
                    user_id, role_id), ex);
            throw ex;
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeUser();
        }
    }

    /**
     * Creates a new user into the database.
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
        LogContext.setAction(Actions.CREATE_USER);

        // request parameter
        String email = null;
        String name = null;
        String surname = null;
        String password = null;

        int role_id = -1;

        List<User> userList = null;

        try {

            // retrieves the request parameters
            // get the parameters from the request
            email = req.getParameter("email");
            name = req.getParameter("name");
            surname = req.getParameter("surname");
            password = req.getParameter("passwd");

            // searching for users with given email in the db
            userList = new SearchUserByEmailDAO(getConnection(), email).access().getOutputParam();

            if (userList.size() > 0) {
                // res.sendRedirect(req.getContextPath());
                req.setAttribute("err", true);
                req.getRequestDispatcher("/jsp/register.jsp").forward(req, res);
                LOGGER.info("A user with email %s has already been registered in the site.", email);
                LogContext.removeIPAddress();
                LogContext.removeAction();
                return;
            }

            if (req.getParameter("role") != null && req.getParameter("role").equals("admin"))
                role_id = 0;
            else
                role_id = 1;

            // create date of today in format yyyy-MM-dd
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(System.currentTimeMillis());
            String registration_date = formatter.format(date);

            // create user object
            User user = new User(email, password, name, surname, role_id, registration_date);

            // create session and save the user
            HttpSession session = req.getSession(true);

            session.setAttribute("regUser", user);
            session.setAttribute("action", Actions.CREATE_USER);

            LOGGER.info("Trying to register user with email %s and role_id %d. Sending verification email.", email,
                    role_id);

            // go to vefirication servlet
            res.sendRedirect(req.getContextPath() + "/verification");

        } catch (SQLException ex) {
            LOGGER.error("Cannot search for users: unexpected error while accessing the database.", ex);
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
        }

    }
}
