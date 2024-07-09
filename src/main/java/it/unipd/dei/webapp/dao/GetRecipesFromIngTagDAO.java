
package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.resource.Difficulty;
import it.unipd.dei.webapp.resource.Recipe;

import it.unipd.dei.webapp.utils.QueryLoader;

import it.unipd.dei.webapp.resource.Ingredient;
import it.unipd.dei.webapp.resource.Tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Get the list of searched recipes from the user based on Ingridients and Tags
 */
public final class GetRecipesFromIngTagDAO extends AbstractDAO<List<Recipe>> {

    // Object used to load the query
    private static final QueryLoader queryLoader = new QueryLoader();

    // loading the query and its components to make the dinamic query
    private static String STATEMENT = queryLoader.loadQuery("GetRecipesFromIngTagDinQuery");
    // contains to insert in ???
    private static String tagCodeSTATEMENT = queryLoader.loadQuery("GetRecipesFromIngTagDinQueryTagCode");
    private static String ingCodeSTATEMENT = queryLoader.loadQuery("GetRecipesFromIngTagDinQueryIngCode");
    // contains to insert in ??
    private static String InsSTATEMENT = queryLoader.loadQuery("GetRecipesFromIngTagDinQueryListCode");

    private final List<Ingredient> ingredients;
    private final List<Tag> tags;
    private int size;

    /**
     * Creates a new object for obtaining the ranked list of the recipes
     *
     * @param con         the connection to the database.
     * @param ingredients List of Ingredients for the search
     * @param tags        List of Tags made for the search
     * @param size        maximum size of the list
     */
    public GetRecipesFromIngTagDAO(final Connection con, final List<Ingredient> ingredients, final List<Tag> tags,
            final int size) {
        super(con);
        this.ingredients = ingredients;
        this.tags = tags;
        this.size = size;
    }

    @Override
    public final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // output parameter
        List<Recipe> recipes = new ArrayList<Recipe>();
        // actual executed dinamic statement
        String dinSTATEMENT = STATEMENT;

        try {

            // craft the string (?,?...) based on the length of the ingredients
            String tmp_ing = "";
            String tmp_tag = "";
            for (Ingredient ing : ingredients) {
                tmp_ing = tmp_ing + InsSTATEMENT;
            }

            for (Tag tag : tags) {
                tmp_tag = tmp_tag + InsSTATEMENT;
            }

            // clean and substite the strings if there are in ingredients or tags

            if (tmp_ing.length() > 0) {
                tmp_ing = tmp_ing.substring(0, tmp_ing.length() - 1);

                // replace the ???
                dinSTATEMENT = dinSTATEMENT.replaceFirst("\\?\\?\\?", ingCodeSTATEMENT);
                // replace the ??
                dinSTATEMENT = dinSTATEMENT.replaceFirst("\\?\\?", tmp_ing);
            } else {
                // if no tag do not add the statement
                dinSTATEMENT = dinSTATEMENT.replaceFirst("\\?\\?\\?", "");
            }

            if (tmp_tag.length() > 0) {
                tmp_tag = tmp_tag.substring(0, tmp_tag.length() - 1);

                // replace the ???
                dinSTATEMENT = dinSTATEMENT.replaceFirst("\\?\\?\\?", tagCodeSTATEMENT);
                // replace the ??
                dinSTATEMENT = dinSTATEMENT.replaceFirst("\\?\\?", tmp_tag);
            } else {
                // if no tag do not add the statement
                dinSTATEMENT = dinSTATEMENT.replaceFirst("\\?\\?\\?", "");
            }

            pstmt = con.prepareStatement(dinSTATEMENT);

            // loads the actual argumnents for the query

            int i = 0;
            while (ingredients.size() > 0) {
                Ingredient ing = ingredients.remove(0);
                pstmt.setInt(i + 1, ing.getId());
                i++;
            }
            // Iterate over tags list, excluding the last element
            while (tags.size() > 0) {
                Tag tag = tags.remove(0);
                pstmt.setInt(i + 1, tag.getId());
                i++;
            }

            // set the LIMIT parameter to set the length of the response
            pstmt.setInt(i + 1, size);
            i++;

            rs = pstmt.executeQuery();

            // forge the outputParameter
            while (rs.next()) {
                // aggiungere data
                Date creation_date = rs.getDate("creation_date");
                // this will not break the order of the recipes
                Recipe tmpRec = new Recipe(rs.getInt("id"), rs.getString("name"), rs.getString("description"),
                        rs.getInt("time_minutes"),
                        Difficulty.valueOf(rs.getString("difficulty")), rs.getString("image_url"), creation_date,
                        rs.getInt("user_id"), null);
                recipes.add(tmpRec);
            }

        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }
        }
        this.outputParam = recipes;
    }

}
