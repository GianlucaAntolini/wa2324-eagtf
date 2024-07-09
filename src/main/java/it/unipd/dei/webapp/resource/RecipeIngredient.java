package it.unipd.dei.webapp.resource;

/**
 * Represents the a recipe-ingredient relation.
 */
public class RecipeIngredient {
    /**
     * The identifier of the recipe
     */
    private int recipe_id;

    /**
     * The identifier of the ingredient
     */
    private int ingredient_id;

    /**
     * Creates a new object for managing recipe-ingredient relation data
     * 
     * @param recipe_id
     *                      the identifier of the recipe
     * @param ingredient_id
     *                      the identifier of the ingredient
     */
    public RecipeIngredient(int recipe_id, int ingredient_id) {
        this.recipe_id = recipe_id;
        this.ingredient_id = ingredient_id;
    }

    /**
     * Prints to string all the info about the recipe-ingredient relation.
     */
    public String toString() {
        return "RecipeIngredient %s : %s".formatted(recipe_id, ingredient_id);
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
     * Returns the identifier of the ingredient.
     * 
     * @return the identifier of the ingredient.
     */
    public int getIngredient_id() {
        return ingredient_id;
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

    /**
     * Set an indentifier for the ingredient
     * 
     * @param ingredient_id
     *                      the identifier of the ingredient
     */
    public void setIngredient_id(int ingredient_id) {
        this.ingredient_id = ingredient_id;
    }

}
