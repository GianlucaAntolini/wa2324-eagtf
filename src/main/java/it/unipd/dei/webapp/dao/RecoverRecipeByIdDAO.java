package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.utils.QueryLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Set rejected recipes as pending (null).
 */
public class RecoverRecipeByIdDAO extends AbstractDAO<Object> {
    /**
     * Creates a new object recovering recipes.
     *
     * @param con       the connection to the database.
     * @param recipe_id the identifier of the recipe.
     */
    public RecoverRecipeByIdDAO(Connection con, Integer recipe_id) {
        super(con);

        if (recipe_id == null) {
            LOGGER.error("The recipe id cannot be null.");
            throw new NullPointerException("The recipe id cannot be null.");
        }

        this.recipe_id = recipe_id;
    }

    /**
     * Load the query from the query.xml file
     */
    private static final QueryLoader queryLoader = new QueryLoader();
    private static String STATEMENT = queryLoader.loadQuery("RecoverRecipeByIdQuery");

    /**
     * The identifier of the recipe
     */
    private final Integer recipe_id;

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the result (final status of the recipe)
        String status = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, recipe_id);

            rs = pstmt.executeQuery();

            if (rs.next())
                status = "recovered";

            LOGGER.info("Recipe %d successfully recovered", recipe_id);
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
        this.outputParam = status;
    }

}
