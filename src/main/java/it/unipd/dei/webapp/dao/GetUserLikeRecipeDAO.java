
package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.utils.QueryLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Check whether the user liked the recipe.
 */
public final class GetUserLikeRecipeDAO extends AbstractDAO<Boolean> {

    /**
     * The identifier of the recipe
     */
    private final int recipe_id;

    /**
     * The identifier of the user
     */
    private final int user_id;

    private static final QueryLoader queryLoader = new QueryLoader();
    private static String STATEMENT = queryLoader.loadQuery("GetUserLikeRecipeQuery");

    /**
     * Creates a new object for checking whether the user liked the recipe.
     *
     * @param con       the connection to the database.
     * @param recipe_id the identifier of the recipe.
     * @param user_id   the identifier of the use.
     */
    public GetUserLikeRecipeDAO(Connection con, String recipe_id, String user_id) {
        super(con);

        try {
            this.recipe_id = Integer.valueOf(recipe_id);
            this.user_id = Integer.valueOf(user_id);
        } catch (NumberFormatException e) {
            LOGGER.error("The recipe id must be a positive number.");
            throw new IllegalArgumentException("The user id and the recipe id must be numbers.");
        }
    }

    @Override
    public final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the result of the search
        Boolean like = null;

        try {
            LOGGER.info("Statement: %s", STATEMENT);
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, recipe_id);
            pstmt.setInt(2, user_id);
            rs = pstmt.executeQuery();

            if (!rs.next()) {
                like = false;
            } else {
                like = true;
            }

        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

        }

        this.outputParam = like;
    }

}
