package it.unipd.dei.webapp.resource;

import java.io.OutputStream;

import com.fasterxml.jackson.core.JsonGenerator;

/**
 * Represents the data about an ingredient.
 */
public class Ingredient extends AbstractResource {
    /**
     * The identifier of the ingredient
     */
    private int id;

    /**
     * The name of the ingredient
     */
    private String name;

    /**
     * Creates a new ingredient
     * 
     * @param id
     *             the identifier of the ingredient
     * @param name
     *             the name of the ingredient
     */
    public Ingredient(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Prints to string all the info about the ingredient.
     */
    public String toString() {
        return "Ingredient %s : %s".formatted(id, name);
    }

    /**
     * Returns the identifier of the ingredient.
     * 
     * @return the identifier of the ingredient.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the name of the ingredient.
     * 
     * @return the name of the ingredient.
     */
    public String getName() {
        return name;
    }

    /**
     * Set an identifier for the ingredient
     * 
     * @param id
     *           the identifier of the ingredient
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set a name for the ingredient
     * 
     * @param id
     *           the name of the ingredient
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected void writeJSON(OutputStream out) throws Exception {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();
        jg.writeFieldName("ingredient");
        jg.writeStartArray();
        jg.flush();

        jg.writeRaw("id: %s,\n".formatted(id));
        jg.writeRaw("name: %s,\n".formatted(name));

        jg.flush();
        jg.writeEndArray();
        jg.writeEndObject();
        jg.flush();
    }

}
