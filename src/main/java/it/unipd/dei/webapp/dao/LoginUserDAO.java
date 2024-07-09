
package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.utils.QueryLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.unipd.dei.webapp.resource.User;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Searches for users given email and password.
 */
public final class LoginUserDAO extends AbstractDAO<User> {

    /**
     * Load the query from the query.xml file
     */
    private static final QueryLoader queryLoader = new QueryLoader();
    private static String STATEMENT = queryLoader.loadQuery("LoginUserQuery");

    /**
     * The email of the user
     */
    private final String email;

    /**
     * The password of the user
     */
    private final String password;

    /**
     * Creates a new object for searching for users given email and password.
     *
     * @param con      the connection to the database.
     * @param email    the email of the user.
     * @param password the password of the user.
     */
    public LoginUserDAO(final Connection con, final String email, final String password) {
        super(con);
        this.email = email;
        this.password = password;
    }

    @Override
    public final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the result of the search
        User user = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, email);
            // hashing the password
            pstmt.setString(2, DigestUtils.sha256Hex(password));
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

            LOGGER.info("User with id %d successfully logged in.", user.getId());
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
