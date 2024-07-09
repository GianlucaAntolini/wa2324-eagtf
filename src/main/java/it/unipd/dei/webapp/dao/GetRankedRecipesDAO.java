
package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.resource.Difficulty;
import it.unipd.dei.webapp.resource.Recipe;

import it.unipd.dei.webapp.utils.QueryLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class represents a Data Access Object (DAO) used to retrieve the ranked
 * list based on likes
 * from the database.
 */
public final class GetRankedRecipesDAO extends AbstractDAO<List<Recipe>> {

    // Instantiate QueryLoader to load SQL queries from external files
    private static final QueryLoader queryLoader = new QueryLoader();

    // Load the SQL query for retrieving ranked recipes
    private static String STATEMENT = queryLoader.loadQuery("GetRankedRecipesQuery");

    // Variable to store the size of the recipe list to be retrieved
    int size;

    /**
     * Creates a new object for obtaining the ranked list of the recipes
     *
     * @param con  the connection to the database.
     * @param size maximum size of the list
     */
    public GetRankedRecipesDAO(final Connection con, int size) {
        super(con);
        this.size = size;
    }

    // Method to execute the data access operation
    @Override
    public final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // output parameter
        List<Recipe> recipes = new ArrayList<Recipe>();

        try {
            pstmt = con.prepareStatement(STATEMENT);
            // log the statement executed by the code
            pstmt.setInt(1, size);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // hold the value of the likes for the recipe
                int likeCount = rs.getInt("like_count");
                // holds the value of the data
                Date creation_date = rs.getDate("creation_date");

                // create the recipe object containing all the data
                Recipe tmpRec = new Recipe(rs.getInt("id"), rs.getString("name"), rs.getString("description"),
                        rs.getInt("time_minutes"),
                        Difficulty.valueOf(rs.getString("difficulty")), rs.getString("image_url"), creation_date,
                        rs.getInt("user_id"), likeCount, null);
                // adds the recepipe with the number of likes
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
