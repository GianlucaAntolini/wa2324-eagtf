package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.utils.QueryLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Set a new role for a user.
 */
public class BanUserByIdDAO extends AbstractDAO<Object> {
    /**
     * Creates a new object for changing role to a user.
     *
     * @param con     the connection to the database.
     * @param role_id the identifier of the role.
     * @param user_id the identifier of the user.
     */
    public BanUserByIdDAO(Connection con, Integer role_id, Integer user_id) {
        super(con);

        if (user_id == null) {
            LOGGER.error("The user id cannot be null.");
            throw new NullPointerException("The user id cannot be null.");
        }

        this.role_id = role_id;
        this.user_id = user_id;
    }

    /**
     * Load the query from the query.xml file
     */
    private static final QueryLoader queryLoader = new QueryLoader();
    private static String STATEMENT = queryLoader.loadQuery("BanUserByIdQuery");

    /**
     * The identifier of the user
     */
    private final Integer user_id;

    /**
     * The identifier of the role
     */
    private final Integer role_id;

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String new_role = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, role_id);
            pstmt.setInt(2, user_id);

            rs = pstmt.executeQuery();

            if (rs.next())
                new_role = Integer.toString(rs.getInt("role_id"));

            LOGGER.info("User %d succesfully (un)banned.", user_id);

        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
        this.outputParam = new_role;
    }
}
