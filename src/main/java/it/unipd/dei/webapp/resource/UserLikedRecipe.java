package it.unipd.dei.webapp.resource;

/**
 * Represents the a user-recipe relation (recipes liked by the user).
 */
public class UserLikedRecipe {
    /**
     * The identifier of the user
     */
    private int user_id;

    /**
     * The identifier of the recipe
     */
    private int recipe_id;

    /**
     * Creates a new object for managing user-recipe relation data
     *
     * @param user_id
     *                  the identifier of the user
     * @param recipe_id
     *                  the identifier of the recipe
     */
    public UserLikedRecipe(int user_id, int recipe_id) {
        this.user_id = user_id;
        this.recipe_id = recipe_id;
    }

    /**
     * Prints to string all the info about the user-recipe relation.
     */
    public String toString() {
        return "UserLikedRecipe %s : %s".formatted(user_id, recipe_id);
    }

    /**
     * Returns the identifier of the user.
     * 
     * @return the identifier of the user.
     */
    public int getUser_id() {
        return user_id;
    }

    /**
     * Returns the identifier of the recipe.
     * 
     * @return the identifier of the recipe.
     */
    public int getRecipe_id() {
        return recipe_id;
    }

    /**
     * Set an indentifier for the user
     * 
     * @param user_id
     *                the identifier of the user
     */
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    /**
     * Set an indentifier for the recipe
     * 
     * @param recipe_id
     *                  the identifier of the recipe
     */
    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

}
