package it.unipd.dei.webapp.rest;

import it.unipd.dei.webapp.dao.GetRecipeByIdDAO;
import it.unipd.dei.webapp.resource.Actions;
import it.unipd.dei.webapp.resource.Message;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import it.unipd.dei.webapp.resource.Recipe;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.io.OutputStream;

/**
 * A REST resource for listing items (e.g., recipes, products, etc.).
 *
 * @author Nicola Ferro (ferro@dei.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class GetLikesRR extends AbstractRR {

    public GetLikesRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.RQ_FOR_SEARCH, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {

        int recipe_id = -1;
        Recipe r = null;
        Message m = null;
        final OutputStream out = res.getOutputStream();

        try {
            recipe_id = (int) req.getAttribute("recipe_id");

            // creates a new DAO for accessing the database and lists the items
            r = new GetRecipeByIdDAO(con, recipe_id).access().getOutputParam();

            if (r != null) {
                LOGGER.info("Recipe successfully listed.");

                res.setStatus(HttpServletResponse.SC_OK);
                r.toJSON(out);
            } else { // it should not happen
                LOGGER.error("Fatal error while listing Recipe.");

                m = new Message("Cannot list Recipe: unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(out);
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot list Recipe: unexpected database error.", ex);

            m = new Message("Cannot list Recipe: unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(out);
        }
    }
}
