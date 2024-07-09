
package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.utils.QueryLoader;

import it.unipd.dei.webapp.resource.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Search a user by his email.
 */
public final class SearchUserByEmailDAO extends AbstractDAO<List<User>> {

    /**
     * Load the query from the query.xml file
     */
    private static final QueryLoader queryLoader = new QueryLoader();
    private static String STATEMENT = queryLoader.loadQuery("SearchUserByEmailQuery");

    /**
     * The email of the user
     */
    private final String email;

    /**
     * Creates a new object for searching users.
     *
     * @param con   the connection to the database.
     * @param email the email of the user.
     */
    public SearchUserByEmailDAO(final Connection con, final String email) {
        super(con);
        this.email = email;
    }

    @Override
    public final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the result of the search
        final List<User> users = new ArrayList<User>();

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, "%" + email + "%");

            rs = pstmt.executeQuery();

            while (rs.next()) {
                users.add(new User(rs.getInt("id"), rs.getString("email"), rs.getString("password"),
                        rs.getString("name"), rs.getString("surname"), rs.getInt("role_id"),
                        rs.getDate("registration_date").toString()));
            }

            LOGGER.info("User(s) with email %s successfully listed.", email);
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

        }

        this.outputParam = users;
    }
}
