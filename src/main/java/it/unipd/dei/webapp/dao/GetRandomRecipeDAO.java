
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
 * Get from the database a ranomly selected recipe.
 */
public final class GetRandomRecipeDAO extends AbstractDAO<Recipe> {

    /**
     * Load the queries from the query.xml file
     */
    private static final QueryLoader queryLoader = new QueryLoader();
    private static String STATEMENT1 = queryLoader.loadQuery("GetRandomRecipeQuery1");
    private static String STATEMENT2 = queryLoader.loadQuery("GetRandomRecipeQuery2");

    /**
     * Creates a new object for a random recipe.
     *
     * @param con the connection to the database.
     */
    public GetRandomRecipeDAO(final Connection con) {
        super(con);
    }

    @Override
    public final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the result of the search
        Recipe recipe = null;

        try {
            pstmt = con.prepareStatement(STATEMENT1);

            rs = pstmt.executeQuery();

            if (!rs.next()) {
                throw new SQLException("Recipe not found.");
            }

            Date creation_date = new Date(0);

            try {
                creation_date = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("creation_date"));
            } catch (ParseException e) {
                LOGGER.info("While trying to get recipe %d: Date format error, creation_date set to 1970-01-01",
                        rs.getInt("id"));
            }

            recipe = new Recipe(rs.getInt("id"), rs.getString("name"), rs.getString("description"),
                    rs.getInt("time_minutes"), Difficulty.valueOf(rs.getString("difficulty")),
                    rs.getString("image_url"), creation_date, rs.getInt("user_id"), null);

            LOGGER.info("Recipe %d retrieved from the database.", recipe.getId());

            pstmt = con.prepareStatement(STATEMENT2);
            pstmt.setInt(1, recipe.getId());

            rs = pstmt.executeQuery();

            final List<Ingredient> ingredients = new ArrayList<Ingredient>();

            while (rs.next()) {
                ingredients.add(new Ingredient(rs.getInt("id"), rs.getString("name")));
            }

            recipe.setIngredients(ingredients);

            LOGGER.info("Ingredients of recipe %d retrieved from the database.", recipe.getId());

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
