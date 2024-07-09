
package it.unipd.dei.webapp.servlet;

import it.unipd.dei.webapp.resource.Actions;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.logging.log4j.message.StringFormattedMessage;

public final class AboutUsServlet extends AbstractDatabaseServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction(Actions.VIEW_ABOUT_US_INFO);

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
                req.setAttribute("user_id", user_id);
                req.setAttribute("role_id", role_id);
                req.getRequestDispatcher("/jsp/aboutUs.jsp").forward(req, res);

                LOGGER.info("About Us request (with logged user) successfully served: " + "user_id: "
                        + session.getAttribute("user_id"));

                return;
            }
        } catch (NullPointerException ex) {
            LOGGER.error("Cannot search for user: session may not be available.", ex);
        }

        try {
            req.getRequestDispatcher("/jsp/aboutUs.jsp").forward(req, res);

            LOGGER.info("About Us request successfully served.");

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
