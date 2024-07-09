package it.unipd.dei.webapp.resource;

/**
 * Represents the a recipe-tag relation.
 */
public class RecipeTag {
	/**
	 * The identifier of the recipe
	 */
	private int recipe_id;

	/**
	 * The identifier of the tag
	 */
	private int tag_id;

	/**
	 * Creates a new object for managing recipe-tag relation data
	 * 
	 * @param recipe_id
	 *                  the identifier of the recipe
	 * @param tag_id
	 *                  the identifier of the tag
	 */
	public RecipeTag(int recipe_id, int tag_id) {
		this.recipe_id = recipe_id;
		this.tag_id = tag_id;
	}

	/**
	 * Prints to string all the info about the recipe-tag relation.
	 */
	public String toString() {
		return "RecipeTag %s : %s".formatted(recipe_id, tag_id);
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
	 * Returns the identifier of the tag.
	 * 
	 * @return the identifier of the tag.
	 */
	public int getTag_id() {
		return tag_id;
	}

}
