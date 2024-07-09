package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.utils.QueryLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.codec.digest.DigestUtils;

import it.unipd.dei.webapp.resource.User;

/**
 * Insert a new user in the database.
 */
public class RegisterUserDAO extends AbstractDAO<Object> {
    /**
     * Creates a new object for inserting users in the database.
     *
     * @param con  the connection to the database.
     * @param user the user to be inserted in the db.
     */
    public RegisterUserDAO(Connection con, User user) {
        super(con);

        if (user == null) {
            LOGGER.error("The user cannot be null.");
            throw new NullPointerException("The user cannot be null.");
        }

        this.user = user;
    }

    /**
     * Load the query from the query.xml file
     */
    private static final QueryLoader queryLoader = new QueryLoader();
    private static String STATEMENT = queryLoader.loadQuery("RegisterUserQuery");

    /**
     * The user to be inserted
     */
    private final User user;

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, DigestUtils.sha256Hex(user.getPassword()));
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getSurname());
            pstmt.setInt(5, (short) user.getRole_id());
            pstmt.setDate(6, java.sql.Date.valueOf(user.getRegistration_date()));

            pstmt.execute();

            LOGGER.info("User with email %s successfully inserted in the database.", user.getEmail());

        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

    }

}
