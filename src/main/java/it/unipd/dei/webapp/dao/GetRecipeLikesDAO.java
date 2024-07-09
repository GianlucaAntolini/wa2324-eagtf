
package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.utils.QueryLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Get the number of likes of a recipe searched by its id.
 */
public final class GetRecipeLikesDAO extends AbstractDAO<Integer> {
    /**
     * The identifier of the recipe
     */
    private final int recipe_id;

    /**
     * Load the query from the query.xml file
     */
    private static final QueryLoader queryLoader = new QueryLoader();
    private static String STATEMENT = queryLoader.loadQuery("GetRecipeByIdQuery");

    /**
     * Creates a new object for returning the number of likes of a recipe.
     *
     * @param con       the connection to the database.
     * @param recipe_id the identifier of the recipe.
     */
    public GetRecipeLikesDAO(Connection con, int recipe_id) {
        super(con);

        if (!(recipe_id >= 0)) {
            LOGGER.error("The recipe id must be a positive number.");
            throw new IllegalArgumentException("The recipe id must be a positive number.");
        }

        this.recipe_id = recipe_id;
    }

    @Override
    public final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the result of the search
        Integer likes = null;

        try {
            LOGGER.info("Statement: %s", STATEMENT);
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, recipe_id);
            rs = pstmt.executeQuery();

            if (!rs.next()) {
                throw new SQLException("Recipe not found.");
            }

        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

        }

        this.outputParam = likes;
    }

}
