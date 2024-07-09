package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.utils.QueryLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import it.unipd.dei.webapp.resource.Ingredient;
import it.unipd.dei.webapp.resource.Recipe;
import it.unipd.dei.webapp.resource.Tag;

/**
 * Insert a new recipe in the database.
 */
public class InsertRecipeDAO extends AbstractDAO<Object> {
    /**
     * Creates a new object for creating new recipes.
     *
     * @param con    the connection to the database.
     * @param recipe the recipe to be inserted.
     */
    public InsertRecipeDAO(Connection con, Recipe recipe) {
        super(con);

        if (recipe == null) {
            LOGGER.error("The recipe cannot be null.");
            throw new NullPointerException("The recipe cannot be null.");
        }

        this.recipe = recipe;
    }

    /**
     * Load the query from the query.xml file
     */
    private static final QueryLoader queryLoader = new QueryLoader();
    private static String STATEMENT1 = queryLoader.loadQuery("InsertRecipeQuery1");
    private static String STATEMENT2 = queryLoader.loadQuery("InsertRecipeQuery2");

    // Query to insert the ingredients
    private static String STATEMENT3 = queryLoader.loadQuery("InsertIngredientQuery1");
    private static String STATEMENT4 = queryLoader.loadQuery("InsertIngredientQuery2");

    // Query to insert the tags
    private static String STATEMENT5 = queryLoader.loadQuery("InsertRecipeQuery3");

    /**
     * The recipe to be inserted
     */
    private final Recipe recipe;

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // get the list of ingredients of recipe
            List<Ingredient> ingredients = recipe.getIngredients();

            // cicle over the ingredients of the recipe
            for (Ingredient ingredient : ingredients) {
                // For every ingredient check if it already exists in the database
                pstmt = con.prepareStatement(STATEMENT4);
                pstmt.setString(1, ingredient.getName());
                rs = pstmt.executeQuery();

                // if an ingredient with such name does NOT exist
                if (!rs.next()) {
                    // get the new id
                    pstmt = con.prepareStatement(STATEMENT3);
                    pstmt.setString(1, ingredient.getName());
                    rs = pstmt.executeQuery();

                    rs.next();
                    ingredient.setId(rs.getInt("id")); // this will be the id of the new ingredient to be inserted

                    LOGGER.info("Ingredient %s successfully inserted in the database.", ingredient.getName());
                } else {
                    ingredient.setId(rs.getInt("id"));
                }
            }

            String description = "Ingredients:<br>";

            if (ingredients.size() == 0)
                description += "None<br>";

            for (int i = 0; i < ingredients.size(); i++) {
                description += (i + 1) + ". " + ingredients.get(i).getName() + "<br>";
            }
            description += "<br>";

            description += recipe.getDescription() + "<br><br>";

            // insert the recipe
            pstmt = con.prepareStatement(STATEMENT1);
            pstmt.setString(1, recipe.getName());
            pstmt.setString(2, description);
            pstmt.setInt(3, recipe.getTime_minutes());
            pstmt.setObject(4, recipe.getDifficulty().toString(), Types.OTHER);
            pstmt.setString(5, recipe.getImage_url());
            pstmt.setDate(6, new java.sql.Date(recipe.getCreationDate().getTime()));
            pstmt.setInt(7, recipe.getUser_id());
            pstmt.setNull(8, java.sql.Types.BOOLEAN);

            LOGGER.info(pstmt);

            rs = pstmt.executeQuery();
            rs.next();
            recipe.setId(rs.getInt("id"));

            LOGGER.info("Recipe %d successfully inserted in the database.", recipe.getId());

            // Insert in the "Recipe_Ingredient" table
            pstmt = con.prepareStatement(STATEMENT2);
            for (Ingredient i : recipe.getIngredients()) {
                pstmt.setInt(1, recipe.getId());
                pstmt.setInt(2, i.getId());
                pstmt.execute();
                LOGGER.info("Recipe-Ingredient %d-%d correspondency successfully inserted in the database.",
                        recipe.getId(), i.getId());
            }

            // Insert in the "Recipe_Tag" table
            pstmt = con.prepareStatement(STATEMENT5);
            for (Tag t : recipe.getTags()) {
                pstmt.setInt(1, recipe.getId());
                pstmt.setInt(2, t.getId());
                pstmt.execute();
                LOGGER.info("Recipe-Tag %d-%d correspondency successfully inserted in the database.", recipe.getId(),
                        t.getId());
            }
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

    }

}
