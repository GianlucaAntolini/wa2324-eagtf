package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.utils.QueryLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Change user password given the id or the email. Returns the user email.
 */
public class UpdateUserPswDAO extends AbstractDAO<String> {
    /**
     * Creates a new object for changing name and/or surname of a user.
     * Either the id or the email must be valid.
     *
     * @param con     the connection to the database.
     * @param psw     the new password.
     * @param email   the email of the user (optional).
     * @param user_id the identifier of the user (optional).
     */
    public UpdateUserPswDAO(Connection con, String psw, String email, int user_id) {
        super(con);

        if (psw == null) {
            LOGGER.error("The password cannot be null.");
            throw new NullPointerException("The password cannot be null.");
        }

        this.user_id = user_id;
        this.email = email;
        this.password = psw;
    }

    /**
     * Load the query from the query.xml file
     */
    private static final QueryLoader queryLoader = new QueryLoader();
    private static String STATEMENT1 = queryLoader.loadQuery("UpdateUserPswQuery1");
    private static String STATEMENT2 = queryLoader.loadQuery("UpdateUserPswQuery2");
    private static String STATEMENT3 = queryLoader.loadQuery("UpdateUserPswQuery3");

    /**
     * The identifier of the user
     */
    private final int user_id;

    /**
     * The email of the user
     */
    private String email;

    /**
     * The new password for the user
     */
    private final String password;

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            if (email == null) {
                pstmt = con.prepareStatement(STATEMENT1);
                pstmt.setString(1, DigestUtils.sha256Hex(password));
                pstmt.setInt(2, user_id);
                pstmt.execute();

                pstmt = con.prepareStatement(STATEMENT3);
                pstmt.setInt(1, user_id);
                rs = pstmt.executeQuery();
                rs.next();
                email = rs.getString("email");

                LOGGER.info("Password of user %d successfully updated in the database.", user_id);
            } else {
                pstmt = con.prepareStatement(STATEMENT2);
                pstmt.setString(1, DigestUtils.sha256Hex(password));
                pstmt.setString(2, email);
                pstmt.execute();

                LOGGER.info("Password of user %s successfully updated in the database.", email);
            }

        } finally {
            if (pstmt != null) {
                pstmt.close();
            }

            if (rs != null) {
                rs.close();
            }
        }

        this.outputParam = email;
    }
}
