
package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.resource.Difficulty;
import it.unipd.dei.webapp.resource.Ingredient;
import it.unipd.dei.webapp.resource.Recipe;
import it.unipd.dei.webapp.resource.Tag;
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
 * Seraches for the recipes created by the user.
 */
public final class GetUserRecipesByUserIdDAO extends AbstractDAO<List<Recipe>> {

    /**
     * Load the query from the query.xml file
     */
    private static final QueryLoader queryLoader = new QueryLoader();
    private static String STATEMENT1 = queryLoader.loadQuery("GetUserRecipesByUserIdQuery1");
    private static String STATEMENT2 = queryLoader.loadQuery("GetUserRecipesByUserIdQuery2");
    private static String STATEMENT3 = queryLoader.loadQuery("GetUserRecipesByUserIdQuery3");

    /**
     * The identifier of the user
     */
    private final Integer user_id;

    /**
     * Creates a new object for searching for the user's created recipes.
     *
     * @param con     the connection to the database.
     * @param user_id the identifier of the use.
     */
    public GetUserRecipesByUserIdDAO(final Connection con, final Integer user_id) {
        super(con);
        this.user_id = user_id;
    }

    @Override
    public final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;

        // the result of the search
        final List<Recipe> recipes = new ArrayList<Recipe>();

        try {
            pstmt = con.prepareStatement(STATEMENT1);
            pstmt.setInt(1, user_id);

            rs1 = pstmt.executeQuery();

            while (rs1.next()) {
                // get approved value -> bool that can be null
                Boolean approved = null; // Initialize as null initially

                // Retrieve the value from the ResultSet
                Object approvedObj = rs1.getObject("approved");

                // Check if the value is NULL
                if (approvedObj != null) {
                    // If not NULL, convert to boolean
                    approved = rs1.getBoolean("approved");
                }

                Date creation_date = new Date(0);

                try {
                    creation_date = new SimpleDateFormat("yyyy-MM-dd").parse(rs1.getString("creation_date"));
                } catch (ParseException e) {
                    LOGGER.info(
                            "While trying to get recipes of user %d: Date format error, creation_date set to 1970-01-01",
                            user_id);
                }

                recipes.add(new Recipe(rs1.getInt("id"), rs1.getString("name"), rs1.getString("description"),
                        rs1.getInt("time_minutes"), Difficulty.valueOf(rs1.getString("difficulty")),
                        rs1.getString("image_url"), creation_date,
                        rs1.getInt("user_id"), approved, null));

                pstmt = con.prepareStatement(STATEMENT2);

                pstmt.setInt(1, recipes.get(recipes.size() - 1).getId());

                rs2 = pstmt.executeQuery();

                final List<Ingredient> ingredients = new ArrayList<Ingredient>();

                while (rs2.next()) {
                    ingredients.add(new Ingredient(rs2.getInt("id"), rs2.getString("name")));
                }

                recipes.get(recipes.size() - 1).setIngredients(ingredients);

                final List<Tag> tags = new ArrayList<Tag>();

                pstmt = con.prepareStatement(STATEMENT3);

                pstmt.setInt(1, recipes.get(recipes.size() - 1).getId());

                rs3 = pstmt.executeQuery();

                while (rs3.next()) {
                    tags.add(new Tag(rs3.getInt("id"), rs3.getString("name")));
                }

                recipes.get(recipes.size() - 1).setTags(tags);

            }

            LOGGER.info("Recipes successfully searched by  user_id %d", user_id);

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
