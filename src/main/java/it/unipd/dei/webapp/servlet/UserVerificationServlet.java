package it.unipd.dei.webapp.servlet;

import it.unipd.dei.webapp.dao.RegisterUserDAO;
import it.unipd.dei.webapp.mail.MailManager;
import it.unipd.dei.webapp.resource.Actions;
import it.unipd.dei.webapp.resource.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Autenticate the user using a one-time code
 */
public final class UserVerificationServlet extends AbstractDatabaseServlet {

    /**
     * Build and access the verification page and send the
     * verification email with the one-time code.
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
        LogContext.setAction(Actions.ACCESS_VERIFICATION_PAGE);

        HttpSession session = null;

        try {
            // get the current user's session
            session = req.getSession(false);

            if (session != null && (session.getAttribute("regUser") != null
                    || session.getAttribute("ChangePswUser") != null)) {

                User user = null;
                if (session.getAttribute("regUser") != null)
                    user = (User) session.getAttribute("regUser");
                else if (session.getAttribute("ChangePswUser") != null)
                    user = (User) session.getAttribute("ChangePswUser");

                String code = generateRandomString(6);

                session.setAttribute("verificationCode", code);
                session.setAttribute("verificationTime", System.currentTimeMillis());

                sendVerificationEmail(user, code);

                req.getRequestDispatcher("/jsp/verification.jsp").forward(req, res);

            } else {
                LOGGER.info("Verification failed (incorrect or expired code).");
                req.setAttribute("verificationError1", true);
                res.sendRedirect(req.getContextPath() + "/home");
            }
        } catch (MessagingException ex) {
            LOGGER.error("Cannot send email with one-time code: unexpected error while trying to send the email.", ex);
        } catch (NullPointerException ex) {
            LOGGER.error("Cannot search for user: session may not be available.", ex);
        }
    }

    /**
     * Generate alphanumeric random string.
     * 
     * @param length the length of the output string.
     */
    private String generateRandomString(int length) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        LOGGER.info("One-time generated code: %s", generatedString);

        return generatedString;
    }

    /**
     * Verify the user throught the one-time code.
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
        String insertedCode = null;

        try {

            // retrieves the code from the request parameters
            insertedCode = req.getParameter("code");
            HttpSession session = req.getSession(false);

            if (session != null
                    && session.getAttribute("verificationCode") != null
                    && session.getAttribute("verificationTime") != null
                    && session.getAttribute("action") != null
                    && (session.getAttribute("regUser") != null
                            || session.getAttribute("ChangePswUser") != null)) {

                String trueCode = (String) session.getAttribute("verificationCode");
                long creationTime = (long) session.getAttribute("verificationTime");

                double elapsedDays = (double) TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - creationTime);

                if (trueCode.equals(insertedCode) && elapsedDays < 1) {
                    // user correctly verified
                    LOGGER.info("User correctly verified.");

                    switch ((String) session.getAttribute("action")) {
                        case Actions.CREATE_USER:
                            User user = (User) session.getAttribute("regUser");
                            new RegisterUserDAO(getConnection(), user).access();

                            LOGGER.info("User successfully registered with email %s and role_id %d.", user.getEmail(),
                                    user.getRole_id());

                            // forward to login to make the login
                            session.setAttribute("verificationSuccess", true);

                            session.removeAttribute("verificationCode");
                            session.removeAttribute("verificationTime");
                            session.removeAttribute("action");
                            session.removeAttribute("regUser");

                            sendCreationConfirmationEmail(user);
                            res.sendRedirect(req.getContextPath() + "/login");
                            return;

                        case Actions.CHANGE_PASSWORD:
                            LOGGER.info("User verified, redirecting to password change.");

                            session.removeAttribute("verificationCode");
                            session.removeAttribute("verificationTime");
                            session.removeAttribute("action");

                            // got to change psw page
                            res.sendRedirect(req.getContextPath() + "/changePsw");
                            return;

                        default:
                            break;
                    }

                } else {
                    // verification failed, send home with error
                    LOGGER.info("Verification failed (incorrect or expired code).");
                    session.setAttribute("verificationError1", true);
                    res.sendRedirect(req.getContextPath() + "/home");
                    return;
                }
            } else {
                // verification failed, send home with error
                LOGGER.info("Verification failed (session data missing).");

                session.setAttribute("verificationError2", true);
                res.sendRedirect(req.getContextPath() + "/home");
                return;
            }

            // forward to vefirication servlet
            req.getRequestDispatcher("/verification").forward(req, res);

        } catch (SQLException ex) {
            LOGGER.error("Cannot search for users: unexpected error while accessing the database.", ex);
        } catch (MessagingException ex) {
            LOGGER.error("Cannot send confirmation email: unexpected error while trying to send the email.", ex);
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
        }

    }

    /**
     * Sends an e-mail with a one-time code to the user email.
     *
     * @param u    the user that will receive the email
     * @param code the one-time code
     *
     * @throws MessagingException if something goes wrong while sending the email.
     */
    private void sendVerificationEmail(User u, String code) throws MessagingException {

        final StringBuilder sb = new StringBuilder();

        String name = u.getName() == null ? u.getEmail() : u.getName();

        sb.append(String.format("<p>Dear %s,</p>%n", name));
        sb.append(
                String.format("<p>We recieved your request of a one-time code for your Dinner Dilemma account.</p>%n"));
        sb.append(String.format("<p>The one-time code is: %s</p>%n", code));
        sb.append(String.format(
                "<p>If you didn't request this code, you can ignore this message, an other user may have typed you e-mail address by mistake.</p>%n",
                code));

        sb.append(String.format("<p>Best regards,<br>The Dinner Dilemma Team</p>%n"));

        MailManager.sendMail(u.getEmail(), "Dinner Dilemma - One-time code.",
                sb.toString(), "text/html;charset=UTF-8");

    }

    /**
     * Sends a confirmation e-mail upon successful creation of a new user.
     *
     * @param e the just created user.
     *
     * @throws MessagingException if something goes wrong while sending the email.
     */
    private void sendCreationConfirmationEmail(User u) throws MessagingException {

        final StringBuilder sb = new StringBuilder();

        sb.append(String.format("<p>Dear %s,</p>%n", u.getSurname()));
        sb.append(String.format("<p>Your account has been successfully created as follows:</p>%n"));

        sb.append(String.format("<ul>%n"));
        sb.append(String.format("<li><b>Name</b>: %s</li>%n", u.getName()));
        sb.append(String.format("<li><b>Surname</b>: %s</li>%n", u.getSurname()));
        sb.append(String.format("<li><b>Registration Date</b>: %s</li>%n", u.getRegistration_date()));
        sb.append(String.format("</ul>%n"));

        sb.append(String.format("<p>Best regards,<br>The Dinner Dilemma Team</p>%n"));

        MailManager.sendMail(u.getEmail(), String.format("Dinner Dilemma - User %s successfully created.", u.getName()),
                sb.toString(), "text/html;charset=UTF-8");

    }

}
