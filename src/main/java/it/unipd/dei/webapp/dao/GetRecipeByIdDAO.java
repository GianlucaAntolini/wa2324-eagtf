
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
 * Searches recipes by their id.
 */
public final class GetRecipeByIdDAO extends AbstractDAO<Recipe> {

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
     * Creates a new object for searching for recipes by their id.
     *
     * @param con       the connection to the database.
     * @param recipe_id the identifier of the role.
     */
    public GetRecipeByIdDAO(Connection con, int recipe_id) {
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
        Recipe recipe = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, recipe_id);
            // LOGGER.info("Statement: %s", pstmt);

            rs = pstmt.executeQuery();

            if (!rs.next()) {
                throw new SQLException("Recipe not found.");
            }

            Date creation_date = new Date(0);

            try {
                creation_date = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("r_date"));
            } catch (ParseException e) {
                LOGGER.info("While trying to get recipe %d: Date format error, creation_date set to 1970-01-01",
                        recipe_id);
            }

            recipe = new Recipe(rs.getInt("r_id"), rs.getString("r_name"), rs.getString("r_descr"),
                    rs.getInt("r_time"), Difficulty.valueOf(rs.getString("r_diff")),
                    rs.getString("r_img"), creation_date, rs.getInt("u_id"), rs.getString("u_name"),
                    rs.getString("u_surname"), null);

            recipe.setNLikes(rs.getInt("likes"));

            List<Ingredient> ingredients = new ArrayList<Ingredient>();
            List<Tag> tags = new ArrayList<Tag>();

            ingredients.add(new Ingredient(rs.getInt("i_id"), rs.getString("i_name")));
            tags.add(new Tag(rs.getInt("t_id"), rs.getString("t_name")));

            while (rs.next()) {
                Ingredient tmpIng = new Ingredient(rs.getInt("i_id"), rs.getString("i_name"));
                Tag tmpTag = new Tag(rs.getInt("t_id"), rs.getString("t_name"));

                boolean ingCheck = false;
                for (Ingredient ing : ingredients) {
                    if (ing.getId() == tmpIng.getId()) {
                        ingCheck = true;
                        break;
                    }
                }

                if (!ingCheck)
                    ingredients.add(tmpIng);

                boolean tagCheck = false;
                for (Tag tag : tags) {
                    if (tag.getId() == tmpTag.getId()) {
                        tagCheck = true;
                        break;
                    }
                }

                if (!tagCheck)
                    tags.add(tmpTag);
            }

            recipe.setIngredients(ingredients);
            recipe.setTags(tags);

            LOGGER.info("Ingredients and tags of recipe %d retrieved from the database.", recipe.getId());

        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

        }

        this.outputParam = recipe;
    }

}
