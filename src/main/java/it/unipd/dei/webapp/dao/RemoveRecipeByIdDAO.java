package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.utils.QueryLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Remove a rejected recipe from the database.
 */
public class RemoveRecipeByIdDAO extends AbstractDAO<Object> {
    /**
     * Creates a new object for removing regected recipes from the database.
     *
     * @param con       the connection to the database.
     * @param recipe_id the identifier of the recipe.
     */
    public RemoveRecipeByIdDAO(Connection con, Integer recipe_id) {
        super(con);

        if (recipe_id == null) {
            LOGGER.error("The recipe id cannot be null.");
            throw new NullPointerException("The recipe id cannot be null.");
        }

        this.recipe_id = recipe_id;
    }

    /**
     * Load the queries from the query.xml file
     */
    private static final QueryLoader queryLoader = new QueryLoader();
    private static String STATEMENT1 = queryLoader.loadQuery("RemoveRecipeByIdQuery1");
    private static String STATEMENT2 = queryLoader.loadQuery("RemoveRecipeByIdQuery2");
    private static String STATEMENT3 = queryLoader.loadQuery("RemoveRecipeByIdQuery3");

    /**
     * The identifier of the recipe
     */
    private final Integer recipe_id;

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs1, rs2, rs3;
        rs1 = rs2 = rs3 = null;

        // the result (final status of the recipe)
        String status = null;

        try {
            pstmt = con.prepareStatement(STATEMENT1);
            pstmt.setInt(1, recipe_id);

            rs1 = pstmt.executeQuery();

            pstmt = con.prepareStatement(STATEMENT2);
            pstmt.setInt(1, recipe_id);

            rs2 = pstmt.executeQuery();

            pstmt = con.prepareStatement(STATEMENT3);
            pstmt.setInt(1, recipe_id);

            rs3 = pstmt.executeQuery();

            if (rs1.next() && rs2.next() && rs3.next())
                if (rs1.getInt("recipe_id") == rs2.getInt("recipe_id") &&
                        rs2.getInt("recipe_id") == rs3.getInt("id")) {
                    status = "removed";
                }

            LOGGER.info("Recipe %d successfully removed", recipe_id);
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
        this.outputParam = status;

    }

}
