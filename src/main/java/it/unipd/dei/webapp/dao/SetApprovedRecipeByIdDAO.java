package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.utils.QueryLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Set the status of a pending recipe as approved or rejected (approved = true,
 * approved = false).
 */
public class SetApprovedRecipeByIdDAO extends AbstractDAO<Object> {
    /**
     * Creates a new object for changing status of pending recipes as approved or
     * rejected.
     *
     * @param con       the connection to the database.
     * @param recipe_id the identifier of the recipe.
     * @param approved  the status of the recipe (true or false).
     */
    public SetApprovedRecipeByIdDAO(Connection con, Integer recipe_id, Boolean approved) {
        super(con);

        if (recipe_id == null) {
            LOGGER.error("The recipe id cannot be null.");
            throw new NullPointerException("The recipe id cannot be null.");
        }

        this.approved = approved;
        this.recipe_id = recipe_id;
    }

    /**
     * Load the query from the query.xml file
     */
    private static final QueryLoader queryLoader = new QueryLoader();
    private static String STATEMENT = queryLoader.loadQuery("SetApprovedRecipeByIdQuery");

    /**
     * The identifier of the recipe
     */
    private final Integer recipe_id;

    /**
     * The status of the recipe
     */
    private final Boolean approved;

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the result (final status of the recipe)
        String status = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setBoolean(1, approved);
            pstmt.setInt(2, recipe_id);

            rs = pstmt.executeQuery();

            if (rs.next())
                if (approved)
                    status = "approved";
                else
                    status = "rejected";

            LOGGER.info("Recipe %d successfully set approved = %b", recipe_id, approved);
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
        this.outputParam = status;
    }

}
