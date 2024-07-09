package it.unipd.dei.webapp.rest;

import it.unipd.dei.webapp.dao.ToggleUserLikeRecipeDAO;
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
 * A REST resource for listing items (e.g., recipes, products, etc.).
 *
 * @author Nicola Ferro (ferro@dei.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class PutLikesRR extends AbstractRR {

    public PutLikesRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.RQ_FOR_SEARCH, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {

        String recipe_id = null;
        String user_id = null;
        Message m = null;
        Boolean like = null;

        try {
            // PUT
            recipe_id = (String) req.getAttribute("recipe_id");
            user_id = (String) req.getAttribute("user_id");
            like = new ToggleUserLikeRecipeDAO(con, recipe_id, user_id).access().getOutputParam();

            LOGGER.info("Like successfully toggled.");
            res.setStatus(HttpServletResponse.SC_OK);

            if (like != null) {
                LOGGER.info("Like successfully retrived.");

                res.setStatus(HttpServletResponse.SC_OK);

                final JsonFactory JSON_FACTORY = new JsonFactory();
                JSON_FACTORY.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
                JSON_FACTORY.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
                final JsonGenerator jg = JSON_FACTORY.createGenerator(res.getOutputStream());

                jg.writeStartObject();

                if (like)
                    jg.writeRaw("like: yes\n");
                else
                    jg.writeRaw("like: no\n");

                jg.flush();
                jg.writeEndObject();
                jg.flush();

            } else { // it should not happen
                LOGGER.error("Fatal error while retriving like.");

                m = new Message("Cannot retrive like: unexpected error.", "E5A1", null);
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
