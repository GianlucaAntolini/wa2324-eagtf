package it.unipd.dei.webapp.resource;

/**
 * Represents the data about the role of a user.
 */
public class UserRole {
    /**
     * The identifier of the role
     */
    private int id;

    /**
     * The description of the role
     */
    private String description;

    /**
     * Creates a new ingredient
     * 
     * @param id
     *                    the identifier of the role
     * @param description
     *                    the description of the role
     */
    public UserRole(int id, String description) {
        this.id = id;
        this.description = description;
    }

    /**
     * Prints to string all the info about the role.
     */
    public String toString() {
        return "UserRole %s : %s".formatted(id, description);
    }

    /**
     * Returns the identifier of the role.
     * 
     * @return the identifier of the role.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the description of the role.
     * 
     * @return the description of the role.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set an identifier for the role
     * 
     * @param id
     *           the identifier of the role
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set an description for the role
     * 
     * @param id
     *           the description of the role
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
