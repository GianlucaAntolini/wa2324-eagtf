
package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.utils.QueryLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Check whether a user has already given like to a recipe and then assign or
 * remove it.
 */
public final class ToggleUserLikeRecipeDAO extends AbstractDAO<Boolean> {

    private final int recipe_id;
    private final int user_id;

    private static final QueryLoader queryLoader = new QueryLoader();
    private static String STATEMENT1 = queryLoader.loadQuery("ToggleUserLikeRecipeQuery1");
    private static String STATEMENT2 = queryLoader.loadQuery("ToggleUserLikeRecipeQuery2");
    private static String STATEMENT3 = queryLoader.loadQuery("ToggleUserLikeRecipeQuery3");

    /**
     * Creates a new object for checking and adding or removing likes.
     *
     * @param con       the connection to the database.
     * @param recipe_id the identifier of the recipe.
     * @param user_id   the identifier of the user.
     */
    public ToggleUserLikeRecipeDAO(Connection con, String recipe_id, String user_id) {
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
        LOGGER.info("Starting waiting process");
        for (int i = 0; i < 1000; i++) {
            LOGGER.info("Waiting... (%d)", i);
        }
        LOGGER.info("Waiting process ended");

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the result (status of the like)
        Boolean like = null;

        try {
            LOGGER.info("Statement: %s", STATEMENT1);
            pstmt = con.prepareStatement(STATEMENT1);
            pstmt.setInt(1, recipe_id);
            pstmt.setInt(2, user_id);
            rs = pstmt.executeQuery();
            pstmt = null;

            if (!rs.next()) {
                like = true;
                LOGGER.info("Statement: %s", STATEMENT3);
                pstmt = con.prepareStatement(STATEMENT3);
                pstmt.setInt(1, recipe_id);
                pstmt.setInt(2, user_id);
                pstmt.executeUpdate();
            } else {
                like = false;
                LOGGER.info("Statement: %s", STATEMENT2);
                pstmt = con.prepareStatement(STATEMENT2);
                pstmt.setInt(1, recipe_id);
                pstmt.setInt(2, user_id);
                pstmt.executeUpdate();
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
