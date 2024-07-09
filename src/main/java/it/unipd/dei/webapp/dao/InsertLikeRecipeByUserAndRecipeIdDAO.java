package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.utils.QueryLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Adds a like for a user to a recipe.
 */
public class InsertLikeRecipeByUserAndRecipeIdDAO extends AbstractDAO<Object> {
    /**
     * Creates a new object for adding likes for users to recipes.
     *
     * @param con       the connection to the database.
     * @param recipe_id the identifier of the recipe.
     * @param user_id   the identifier of the use.
     */
    public InsertLikeRecipeByUserAndRecipeIdDAO(Connection con, String recipe_id, String user_id) {
        super(con);

        try {
            Integer.valueOf(recipe_id);
            Integer.valueOf(user_id);
        } catch (NumberFormatException e) {
            LOGGER.error("The recipe id must be a positive number.");
            throw new IllegalArgumentException("The user id and the recipe id must be numbers.");
        }

        this.recipe_id = recipe_id;
        this.user_id = user_id;
    }

    /**
     * Load the query from the query.xml file
     */
    private static final QueryLoader queryLoader = new QueryLoader();
    private static String STATEMENT = queryLoader.loadQuery("InsertLikeRecipeByUserAndRecipeIdQuery");

    /**
     * The identifier of the recipe
     */
    private final String recipe_id;

    /**
     * The identifier of the user
     */
    private final String user_id;

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);

            pstmt.setString(1, user_id);
            pstmt.setString(2, recipe_id);

            pstmt.execute();

            LOGGER.info("Recipe %d successfully liked by user %d.", recipe_id, user_id);

        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

    }

}
