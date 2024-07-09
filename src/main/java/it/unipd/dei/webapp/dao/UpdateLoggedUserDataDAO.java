package it.unipd.dei.webapp.dao;

import it.unipd.dei.webapp.utils.QueryLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Change name and/or surname of a user given the id.
 */
public class UpdateLoggedUserDataDAO extends AbstractDAO<Object> {
    /**
     * Creates a new object for changing name and/or surname of a user.
     *
     * @param con     the connection to the database.
     * @param name    the name of the user.
     * @param surname the surname of the user.
     * @param user_id the identifier of the user.
     */
    public UpdateLoggedUserDataDAO(Connection con, String name, String surname, int user_id) {
        super(con);

        if (name == null || surname == null) {
            LOGGER.error("The name, surname and email cannot be null.");
            throw new NullPointerException("The name, surname and email cannot be null.");
        }

        this.user_id = user_id;
        this.name = name;
        this.surname = surname;
    }

    /**
     * Load the query from the query.xml file
     */
    private static final QueryLoader queryLoader = new QueryLoader();
    private static String STATEMENT = queryLoader.loadQuery("UpdateLoggedUserDataQuery");

    /**
     * The identifier of the user
     */
    private final int user_id;

    /**
     * The name of the user
     */
    private final String name;

    /**
     * The surname of the user
     */
    private final String surname;

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, name);
            pstmt.setString(2, surname);
            pstmt.setInt(3, user_id);

            pstmt.execute();

            LOGGER.info("User %d successfully inserted in the database.", user_id);

        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

    }
}
