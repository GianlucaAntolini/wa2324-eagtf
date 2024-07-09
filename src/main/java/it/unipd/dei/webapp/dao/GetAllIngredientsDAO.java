
package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.resource.Ingredient;

import it.unipd.dei.webapp.utils.QueryLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Get all the ingredients from the database.
 */
public final class GetAllIngredientsDAO extends AbstractDAO<List<Ingredient>> {

    /**
     * Load the query from the query.xml file
     */
    private static final QueryLoader queryLoader = new QueryLoader();
    private static String STATEMENT = queryLoader.loadQuery("GetAllIngredientsQuery");

    /**
     * Creates a new object for searching for ingredients.
     *
     * @param con the connection to the database.
     */
    public GetAllIngredientsDAO(final Connection con) {
        super(con);
    }

    @Override
    public final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the result of the search
        final List<Ingredient> ingredients = new ArrayList<Ingredient>();

        try {
            pstmt = con.prepareStatement(STATEMENT);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                ingredients.add(new Ingredient(rs.getInt("id"), rs.getString("name")));
            }
            LOGGER.info("Ingredients retrieved from the database.");

        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

        }

        this.outputParam = ingredients;
    }

}
