package it.unipd.dei.webapp.resource;

/**
 * Represents a tag for searching recipes.
 */
public class Tag {
	/**
	 * The identifier of the tag
	 */
	private int id;

	/**
	 * The name of the tag
	 */
	private String name;

	/**
	 * Creates a new tag
	 * 
	 * @param id
	 *             the identifier of the tag
	 * @param name
	 *             the name of the tag
	 */
	public Tag(int id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * Prints to string all the info about the tag
	 */
	public String toString() {
		// print all the info
		return "Tag %s : %s".formatted(id, name);
	}

	/**
	 * Returns the identifier of the tag.
	 * 
	 * @return the identifier of the tag.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the name of the tag.
	 * 
	 * @return the name of the tag.
	 */
	public String getName() {
		return name;
	}

}
