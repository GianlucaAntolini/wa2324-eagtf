package it.unipd.dei.webapp.rest;

import it.unipd.dei.webapp.dao.RecoverRecipeByIdDAO;
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
public final class RecoverRecipeRR extends AbstractRR {

    public RecoverRecipeRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.RQ_FOR_SEARCH, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {

        Integer recipe_id = -1;
        Message m = null;

        String status = null;

        try {
            recipe_id = Integer.parseInt((String) req.getAttribute("recipe_id"));

            status = (String) new RecoverRecipeByIdDAO(con, recipe_id).access().getOutputParam();

            LOGGER.info("Recipe successfully recovered.");
            m = new Message("Recipe successfully recovered.");

            res.setStatus(HttpServletResponse.SC_OK);

            if (status != null) {
                LOGGER.info("New status successfully retrived = %s.", status);

                res.setStatus(HttpServletResponse.SC_OK);

                final JsonFactory JSON_FACTORY = new JsonFactory();
                JSON_FACTORY.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
                JSON_FACTORY.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
                final JsonGenerator jg = JSON_FACTORY.createGenerator(res.getOutputStream());

                jg.writeStartObject();

                jg.writeStringField("recipe_id", (String) req.getAttribute("recipe_id"));

                jg.writeStringField("status", status);

                jg.writeEndObject();

                jg.flush();
            } else { // it should not happen
                LOGGER.error("Fatal error while retriving the new status.");

                m = new Message("Cannot retrive the new status: unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }

        } catch (SQLException ex) {
            LOGGER.error("Cannot toggle like: unexpected database error.", ex);

            m = new Message("Cannot toggle like: unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}