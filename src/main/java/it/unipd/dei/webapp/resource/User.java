package it.unipd.dei.webapp.resource;

import java.util.List;

/**
 * Represents the data about an user.
 */
public class User {
    /**
     * The identifier of the user
     */
    private int id;

    /**
     * The email of the user
     */
    private String email;

    /**
     * The password of the user
     */
    private String password;

    /**
     * The name of the user
     */
    private String name;

    /**
     * The surname of the user
     */
    private String surname;

    /**
     * The id representing the role of the user (0 = admin, 1 = user, 2 = banned)
     */
    private int role_id;

    /**
     * The registration date of the user (format is YYYY-MM-DD)
     */
    private String registration_date;

    /**
     * The list of recipes inserted by the user
     */
    private List<Recipe> recipes;

    /**
     * The list of recipes liked by the user
     */
    private List<Recipe> liked_recipes;

    /**
     * Creates a new user
     * 
     * @param id
     *                          the identifier of the user
     * @param email
     *                          the email of the user
     * @param password
     *                          the password of the user
     * @param name
     *                          the name of the user
     * @param surname
     *                          the surname of the user
     * @param role_id
     *                          the role identifier of the user
     * @param registration_date
     *                          the registration date of the user
     */
    public User(int id, String email, String password, String name, String surname, int role_id,
            String registration_date) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.role_id = role_id;
        this.registration_date = registration_date;
    }

    /**
     * Creates a new user without the identifier (used for registration)
     * 
     * @param email
     *                          the email of the user
     * @param password
     *                          the password of the user
     * @param name
     *                          the name of the user
     * @param surname
     *                          the surname of the user
     * @param role_id
     *                          the role identifier of the user
     * @param registration_date
     *                          the registration date of the user
     */
    public User(String email, String password, String name, String surname, int role_id, String registration_date) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.role_id = role_id;
        this.registration_date = registration_date;
    }

    /**
     * Prints to string all the info about the user.
     */
    public String toString() {
        return "User %s : %s : %s : %s : %s : %s : %s".formatted(id, email, password, name, surname, role_id,
                registration_date);
    }

    /**
     * Returns the identifier of the user.
     * 
     * @return the identifier of the user.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the email of the user.
     * 
     * @return the email of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the password of the user.
     * 
     * @return the password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the name of the user.
     * 
     * @return the name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the surname of the user.
     * 
     * @return the surname of the user.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Returns the role identifier of the user.
     * 
     * @return the role identifier of the user.
     */
    public int getRole_id() {
        return role_id;
    }

    /**
     * Returns the registration date of the user.
     * 
     * @return the registration date of the user.
     */
    public String getRegistration_date() {
        return registration_date;
    }

    /**
     * Returns the list of inserted recipes of the user.
     * 
     * @return the list of inserted recipes of the user.
     */
    public List<Recipe> getRecipes() {
        return recipes;
    }

    /**
     * Returns the list of liked recipes of the user.
     * 
     * @return the list of liked recipes of the user.
     */
    public List<Recipe> getLiked_recipes() {
        return liked_recipes;
    }

    /**
     * Set an identifier for the User
     * 
     * @param id
     *           the identifier of the user
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set an email for the user
     * 
     * @param email
     *              the email of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set a password for the user
     * 
     * @param password
     *                 the password of the user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Set a name for the user
     * 
     * @param name
     *             the name of the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set a surname for the user
     * 
     * @param surname
     *                the surname of the user
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Set a role identifier for the user
     * 
     * @param role_id
     *                the role identifier of the user
     */
    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    /**
     * Set a registration date for the user
     * 
     * @param registration_date
     *                          the registration date of the user
     */
    public void setRegistration_date(String registration_date) {
        this.registration_date = registration_date;
    }

    /**
     * Set a list of inserted recipes for the user
     * 
     * @param recipes
     *                the list of inserted recipes of the user
     */
    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    /**
     * Set a list of inserted recipes for the user
     * 
     * @param liked_recipes
     *                      the list of liked recipes of the user
     */
    public void setLiked_recipes(List<Recipe> liked_recipes) {
        this.liked_recipes = liked_recipes;
    }

}
