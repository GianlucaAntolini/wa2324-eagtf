
package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.resource.Difficulty;
import it.unipd.dei.webapp.resource.Ingredient;
import it.unipd.dei.webapp.resource.Recipe;

import it.unipd.dei.webapp.utils.QueryLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Searches for recipes to be recovered or removed.
 */
public final class GetRecipesToBeRecoveredOrRemovedDAO extends AbstractDAO<List<Recipe>> {

    /**
     * Load the queries from the query.xml file
     */
    private static final QueryLoader queryLoader = new QueryLoader();
    private static String STATEMENT1 = queryLoader.loadQuery("GetRecipesToBeRecoveredOrRemovedQuery1");
    private static String STATEMENT2 = queryLoader.loadQuery("GetRecipesToBeRecoveredOrRemovedQuery2");

    /**
     * Creates a new object for searching for recipes to be recovered or removed.
     *
     * @param con the connection to the database.
     */
    public GetRecipesToBeRecoveredOrRemovedDAO(final Connection con) {
        super(con);
    }

    @Override
    public final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;

        // the result of the search
        final List<Recipe> recipes = new ArrayList<Recipe>();

        try {
            pstmt = con.prepareStatement(STATEMENT1);

            rs1 = pstmt.executeQuery();

            while (rs1.next()) {

                Date creation_date = new Date(0);

                Boolean approved = null;

                try {
                    creation_date = new SimpleDateFormat("yyyy-MM-dd").parse(rs1.getString("creation_date"));
                } catch (ParseException e) {
                    LOGGER.info("While trying to get recipe %d: Date format error, creation_date set to 1970-01-01",
                            rs1.getInt("id"));
                }

                if (rs1.getString("approved") != null) {
                    approved = rs1.getBoolean("approved");
                }

                recipes.add(new Recipe(rs1.getInt("id"), rs1.getString("name"), rs1.getString("description"),
                        rs1.getInt("time_minutes"), Difficulty.valueOf(rs1.getString("difficulty")),
                        rs1.getString("image_url"), creation_date, rs1.getInt("user_id"), approved, null));

                LOGGER.info("Recipe %d retrieved from the database.", recipes.get(recipes.size() - 1).getId());

                pstmt = con.prepareStatement(STATEMENT2);

                pstmt.setInt(1, recipes.get(recipes.size() - 1).getId());

                rs2 = pstmt.executeQuery();

                final List<Ingredient> ingredients = new ArrayList<Ingredient>();

                while (rs2.next()) {
                    ingredients.add(new Ingredient(rs2.getInt("id"), rs2.getString("name")));
                }

                LOGGER.info("Ingredients of recipe %d retrieved from the database.",
                        recipes.get(recipes.size() - 1).getId());

            }

        } finally {
            if (rs1 != null) {
                rs1.close();
            }
            if (rs2 != null) {
                rs2.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

        }

        this.outputParam = recipes;
    }

}
