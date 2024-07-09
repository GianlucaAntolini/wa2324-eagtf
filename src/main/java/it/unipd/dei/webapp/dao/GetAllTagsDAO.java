
package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.resource.Tag;

import it.unipd.dei.webapp.utils.QueryLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Get all tags from the database.
 */
public final class GetAllTagsDAO extends AbstractDAO<List<Tag>> {

    /**
     * Load the query from the query.xml file
     */
    private static final QueryLoader queryLoader = new QueryLoader();
    private static String STATEMENT = queryLoader.loadQuery("GetAllTagsQuery");

    /**
     * Creates a new object for searching for tags.
     *
     * @param con the connection to the database.
     */
    public GetAllTagsDAO(final Connection con) {
        super(con);
    }

    @Override
    public final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the result of the search
        final List<Tag> tags = new ArrayList<Tag>();

        try {
            pstmt = con.prepareStatement(STATEMENT);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                tags.add(new Tag(rs.getInt("id"), rs.getString("name")));

            }
            LOGGER.info("Tags retrieved from the database.");

        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

        }

        this.outputParam = tags;
    }

}
