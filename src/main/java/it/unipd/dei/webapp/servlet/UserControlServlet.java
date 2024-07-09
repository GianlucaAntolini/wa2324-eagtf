package it.unipd.dei.webapp.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import org.apache.logging.log4j.message.StringFormatterMessageFactory;

/**
 * Build and access the user control panel.
 */
public class UserControlServlet extends HttpServlet {

	/**
	 * A LOGGER available for all the subclasses.
	 */
	protected static final Logger LOGGER = LogManager.getLogger(UserControlServlet.class,
			StringFormatterMessageFactory.INSTANCE);

	/**
	 * Build and access the user control panel.
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
		LogContext.setResource(req.getRequestURI());
		LogContext.setAction("OPEN_USR_CONTROL_PANEL");

		int role_id = -1;

		HttpSession session = null;

		try {
			// get the current user's session
			session = req.getSession(false);

			// check whether the use is logged
			if (session != null & session.getAttribute("user_id") != null) {

				// get the role of the user
				role_id = (int) session.getAttribute("role_id");
			} else {
				LOGGER.info("You should not be here: session or user_id is null.");
			}
		} catch (NullPointerException ex) {
			LOGGER.error("Cannot search for user: session may not be available.", ex);
		}

		try {
			// stores the user as a request attribute
			req.setAttribute("role_id", role_id);
			req.setAttribute("session", session);

			// req.setAttribute("session",session);
			// forwards the control to the view_and_update_user_data JSP
			req.getRequestDispatcher("/jsp/usr_control.jsp").forward(req, res);

			// write a "log" statement
			LOGGER.info("Request successfully served.");
		} catch (Exception e) {
			LOGGER.error("Unable to serve request.", e);
			throw e;
		} finally {
			LogContext.removeIPAddress();
			LogContext.removeAction();
			LogContext.removeResource();
		}
	}

}
