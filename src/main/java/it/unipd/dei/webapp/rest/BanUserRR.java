package it.unipd.dei.webapp.rest;

import it.unipd.dei.webapp.dao.BanUserByIdDAO;
import it.unipd.dei.webapp.resource.Actions;
import it.unipd.dei.webapp.resource.Message;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

/**
 * A REST resource for toggling likes.
 */
public final class BanUserRR extends AbstractRR {

    /**
     * Creates a new REST resource for banning users.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public BanUserRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.RQ_FOR_SEARCH, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {

        Integer user_id = -1;
        Integer role_id = -1;

        Message m = null;

        String new_rid = null;

        try {
            user_id = Integer.parseInt((String) req.getAttribute("user_id"));
            role_id = Integer.parseInt((String) req.getAttribute("role_id"));

            new_rid = (String) new BanUserByIdDAO(con, role_id, user_id).access().getOutputParam();

            LOGGER.info("New user role successfully retrived.");
            res.setStatus(HttpServletResponse.SC_OK);

            if (new_rid != null) {
                LOGGER.info("New status successfully retrived = %s.", new_rid);

                res.setStatus(HttpServletResponse.SC_OK);

                final JsonFactory JSON_FACTORY = new JsonFactory();
                JSON_FACTORY.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
                JSON_FACTORY.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
                final JsonGenerator jg = JSON_FACTORY.createGenerator(res.getOutputStream());

                jg.writeStartObject();

                jg.writeStringField("user_id", (String) req.getAttribute("user_id"));

                jg.writeStringField("role_id", new_rid);

                jg.writeEndObject();

                jg.flush();

            } else { // it should not happen
                LOGGER.error("Fatal error while (un)banning user.");

                m = new Message("Cannot retrive new user role: unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot toggle like: unexpected database error.", ex);

            m = new Message("Cannot toggle like: unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}
