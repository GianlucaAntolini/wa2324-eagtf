package it.unipd.dei.webapp.servlet;

import it.unipd.dei.webapp.dao.GetAllIngredientsDAO;
import it.unipd.dei.webapp.dao.GetAllTagsDAO;
import it.unipd.dei.webapp.resource.Actions;
import it.unipd.dei.webapp.resource.Ingredient;
import it.unipd.dei.webapp.resource.Tag;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.logging.log4j.message.StringFormattedMessage;

public final class HomeRestServlet extends AbstractDatabaseServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setAction(Actions.VIEW_USER_DATA);

        // status for the user
        // TODO change to a user object
        int user_id = -1;
        int role_id = -1;

        List<Ingredient> ingredients = null;
        List<Tag> tags = null;

        HttpSession session = null;

        try {
            // get the current user's session
            session = req.getSession(false);
            // checks if the session is from a real user
            if (session != null && session.getAttribute("user_id") != null) {
                // if so sets the user parameters
                user_id = (int) session.getAttribute("user_id");
                role_id = (int) session.getAttribute("role_id");
            }
        } catch (NullPointerException ex) {
            LOGGER.error("Cannot search for user: session may not be available.", ex);
        }

        try {
            // get list of ingredients
            ingredients = new GetAllIngredientsDAO(getConnection()).access().getOutputParam();

            // get list of ingredients
            tags = new GetAllTagsDAO(getConnection()).access().getOutputParam();

            // set the attribute to auto update the jsp
            req.setAttribute("usr_src_ing", req.getParameter("usr_src_ing"));
            req.setAttribute("usr_src_tag", req.getParameter("usr_src_tag"));
            req.setAttribute("ingredients", ingredients);
            req.setAttribute("tags", tags);
        } catch (SQLException ex) {
            LOGGER.error("Cannot get ingredients list: database error.", ex);
        }

        try {
            // Ottieni l'oggetto RequestDispatcher per l'altra servlet
            LOGGER.info(req);
            // load the main page
            req.getRequestDispatcher("/jsp/homer.jsp").include(req, res);

        } catch (Exception ex) {
            // TODO Correct this based on the user that is logged and based on his role
            LOGGER.error(new StringFormattedMessage("Unable to send response when creating user %d with .", user_id),
                    ex);
            throw ex;
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeUser();
        }
    }
}
