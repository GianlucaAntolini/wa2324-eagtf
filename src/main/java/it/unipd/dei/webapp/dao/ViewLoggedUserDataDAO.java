package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.utils.QueryLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.unipd.dei.webapp.resource.User;

/**
 * Search for the user's data given his identifier.
 */
public class ViewLoggedUserDataDAO extends AbstractDAO<User> {

    /**
     * Load the query from the query.xml file
     */
    private static final QueryLoader queryLoader = new QueryLoader();
    private static String STATEMENT = queryLoader.loadQuery("ViewLoggedUserDataQuery");

    private final int user_id;

    /**
     * Creates a new object for searching for user's data.
     *
     * @param con     the connection to the database.
     * @param user_id the identifier of the user.
     */
    public ViewLoggedUserDataDAO(final Connection con, final int user_id) {
        super(con);
        this.user_id = user_id;
    }

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the result of the search
        User user = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, user_id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                // user found
                user = new User(rs.getInt("id"), rs.getString("email"), rs.getString("password"),
                        rs.getString("name"), rs.getString("surname"), rs.getInt("role_id"),
                        rs.getDate("registration_date").toString());

            } else {
                // user not found
                throw new SQLException("User not found");
            }

            LOGGER.info("User with id %d successfully found.", user_id);

        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }
        }
        this.outputParam = user;
    }
}
