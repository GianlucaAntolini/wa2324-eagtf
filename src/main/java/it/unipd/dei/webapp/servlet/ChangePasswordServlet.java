
package it.unipd.dei.webapp.servlet;

import it.unipd.dei.webapp.dao.SearchUserByEmailDAO;
import it.unipd.dei.webapp.dao.UpdateUserPswDAO;
import it.unipd.dei.webapp.mail.MailManager;
import it.unipd.dei.webapp.resource.Actions;
import it.unipd.dei.webapp.resource.User;

import java.io.IOException;

import java.sql.SQLException;
import java.util.List;

import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.logging.log4j.message.StringFormattedMessage;

/**
 * Let a logged or verified user to change their password.
 */
public class ChangePasswordServlet extends AbstractDatabaseServlet {

    /**
     * Build and access the change password page.
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
        LogContext.setAction(Actions.CHANGE_PASSWORD);

        try {
            HttpSession session = req.getSession(false);

            if (session != null && (session.getAttribute("user_id") != null
                    || session.getAttribute("ChangePswUser") != null)) {

                req.getRequestDispatcher("/jsp/changePsw.jsp").forward(req, res);
            } else {
                session.setAttribute("changePasswordError1", true);
                res.sendRedirect(req.getContextPath() + "/login");
                return;
            }

        } catch (Exception ex) {
            LOGGER.error(new StringFormattedMessage("Unable to start login."), ex);
            throw ex;
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
        }
    }

    /**
     * Update the user password.
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
        String password1 = null;
        String password2 = null;

        try {
            // retrieves the request parameter
            email = req.getParameter("email_change");
            HttpSession session = req.getSession(true);

            if (email != null) {

                List<User> users = new SearchUserByEmailDAO(getConnection(), email).access().getOutputParam();
                User user = null;

                if (users.size() == 1)
                    user = users.get(0);
                else
                    user = new User(email, null, null, null, -1, null);

                session.setAttribute("ChangePswUser", user);
                session.setAttribute("action", Actions.CHANGE_PASSWORD);

                res.sendRedirect(req.getContextPath() + "/verification");

                LogContext.removeIPAddress();
                LogContext.removeAction();
                return;

            } else if (session != null && (session.getAttribute("user_id") != null
                    || session.getAttribute("ChangePswUser") != null)) {

                password1 = req.getParameter("passwd1");
                password2 = req.getParameter("passwd2");

                int user_id = -1;
                if (session.getAttribute("ChangePswUser") != null)
                    email = ((User) session.getAttribute("ChangePswUser")).getEmail();
                else {
                    email = null;
                    user_id = (int) session.getAttribute("user_id");
                }

                if (password1.equals(password2))
                    if (validatePsw(password1)) {
                        email = new UpdateUserPswDAO(getConnection(), password1, email, user_id).access()
                                .getOutputParam();
                        sendCreationConfirmationEmail(email);
                        session.setAttribute("pswUpdated", true);
                        res.sendRedirect(req.getContextPath() + "/login");
                    } else {
                        req.setAttribute("err1", true);
                        req.getRequestDispatcher("/jsp/changePsw.jsp").forward(req, res);
                        return;
                    }
                else {
                    req.setAttribute("diffPsw", true);
                    req.getRequestDispatcher("/jsp/changePsw.jsp").forward(req, res);
                    return;
                }
                return;
            }
        } catch (SQLException ex) {

            LOGGER.error("User not found, email or password are incorrect", ex);

            // send user back to the login page
            // with an error
            req.setAttribute("err0", true);
            res.sendRedirect(req.getContextPath() + "/login");

        } catch (MessagingException ex) {
            LOGGER.error("Cannot send confirmation email: unexpected error while trying to send the email.", ex);
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeUser();
        }

    }

    /**
     * Checks that the new password meets the requirements.
     *
     * @param password the new password
     */
    private boolean validatePsw(String password) {
        return true;
    }

    /**
     * Sends a confirmation e-mail upon successful password update.
     *
     * @param email the email address of the user that will receive the email.
     *
     * @throws MessagingException if something goes wrong while sending the email.
     */
    private void sendCreationConfirmationEmail(String email) throws MessagingException {

        final StringBuilder sb = new StringBuilder();

        sb.append(String.format("<p>Dear %s,</p>%n", email));
        sb.append(String.format("<p>Your password has been successfully updated.</p>%n%n"));

        sb.append(String.format(
                "<p>If you didn't perform this action you should reset it ASAP (you can click on 'forgot password' on the login form on out website).</p>%n"));

        sb.append(String.format("</ul>%n"));
        sb.append(String.format("<p>Best regards,<br>The Dinner Dilemma Team</p>%n"));

        MailManager.sendMail(email, "Dinner Dilemma - Password successfully updated.",
                sb.toString(), "text/html;charset=UTF-8");

    }
}
