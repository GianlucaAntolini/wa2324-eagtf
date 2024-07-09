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

/**
 * A REST resource for listing items (e.g., recipes, products, etc.).
 *
 * @author Nicola Ferro (ferro@dei.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class RecipeDetailsRR extends AbstractRR {

    private final int id;

    public RecipeDetailsRR(final HttpServletRequest req, final HttpServletResponse res, Connection con, int id) {
        super(Actions.RQ_FOR_SEARCH, req, res, con);

        this.id = id;
    }

    @Override
    protected void doServe() throws IOException {

        Recipe rec = null;
        Message m = null;

        try {
            // creates a new DAO for accessing the database and lists the items
            rec = new GetRecipeByIdDAO(con, id).access().getOutputParam();

            if (rec != null) {

                res.setStatus(HttpServletResponse.SC_OK);
                rec.toJSON(res.getOutputStream());
                // new ResourceList(itemList).toJSON(res.getOutputStream());
            }

            else { // it should not happen
                LOGGER.error("Recipe not found ");
                m = new Message("Cannot find Recipe: unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }

        } catch (SQLException ex) {
            LOGGER.error("Cannot list items: unexpected database error.", ex);
            m = new Message("Cannot find Recipe: unexpected error.", "E5A1", null);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}
