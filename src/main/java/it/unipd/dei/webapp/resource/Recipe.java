package it.unipd.dei.webapp.resource;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;

/**
 * Represents the data about a recipe.
 */
public class Recipe extends AbstractResource {
    /**
     * The identifier of the recipe
     */
    private int id;

    /**
     * The name of the recipe
     */
    private String name;

    /**
     * The description of the recipe
     */
    private String description;

    /**
     * The rquired time duration of the recipe
     */
    private int time_minutes;

    /**
     * The difficulty of the recipe
     */
    private Difficulty difficulty;

    /**
     * The url of the image of the recipe
     */
    private String image_url;

    /**
     * The id of the user that inserted the recipe
     */
    private int user_id;

    /**
     * The creation date of the recipe
     */
    private Date creation_date;

    // OPTIONAL FIELDS
    /**
     * The status of the recipe (true = approved, false = rejected, null = pending)
     */
    private Boolean approved = null;

    /**
     * The name of the user that inserted the recipe
     */
    private String user_name = null;

    /**
     * The surname of the user that inserted the recipe
     */
    private String user_surname = null;

    /**
     * The number of likes recived by the recipe
     */
    private int nLikes = -1;

    /**
     * The list of ingredients required by the recipe
     */
    private List<Ingredient> ingredients;

    /**
     * The list of ingredients required by the recipe
     */
    private List<Tag> tags;

    /**
     * Creates a new recipe
     * 
     * @param id
     *                     the identifier of the recipe
     * @param name
     *                     the name of the recipe
     * @param description
     *                     the description of the recipe
     * @param time_minutes
     *                     the duration in minutes of the recipe
     * @param difficulty
     *                     the difficulty of the recipe
     * @param image_url
     *                     the url of the image of the recipe
     * @param user_id
     *                     the identifier of the user that inserted the recipe
     * @param ingredients
     *                     the list of ingredients rquired by the recipe
     */
    public Recipe(int id, String name, String description, int time_minutes, Difficulty difficulty, String image_url,
            Date creation_date,
            int user_id, List<Ingredient> ingredients) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.time_minutes = time_minutes;
        this.difficulty = difficulty;
        this.image_url = image_url;
        this.creation_date = creation_date;
        this.user_id = user_id;
        this.ingredients = ingredients;

    }

    /**
     * Creates a new recipe, with optional parameter approved
     * 
     * @param id
     *                     the identifier of the recipe
     * @param name
     *                     the name of the recipe
     * @param description
     *                     the description of the recipe
     * @param time_minutes
     *                     the duration in minutes of the recipe
     * @param difficulty
     *                     the difficulty of the recipe
     * @param image_url
     *                     the url of the image of the recipe
     * @param user_id
     *                     the identifier of the user that inserted the recipe
     * @param approved
     *                     the status of the recipe
     * @param ingredients
     *                     the list of ingredients rquired by the recipe
     */
    public Recipe(int id, String name, String description, int time_minutes, Difficulty difficulty, String image_url,
            Date creation_date,
            int user_id, Boolean approved, List<Ingredient> ingredients) {
        this(id, name, description, time_minutes, difficulty, image_url, creation_date, user_id, ingredients);
        this.approved = approved;
    }

    /**
     * Creates a new recipe, with optional parameters for user_name, user_surname
     * 
     * @param id
     *                     the identifier of the recipe
     * @param name
     *                     the name of the recipe
     * @param description
     *                     the description of the recipe
     * @param time_minutes
     *                     the duration in minutes of the recipe
     * @param difficulty
     *                     the difficulty of the recipe
     * @param image_url
     *                     the url of the image of the recipe
     * @param user_id
     *                     the identifier of the user that inserted the recipe
     * @param user_name
     *                     the name of the user tha inserted of the recipe
     * @param user_surname
     *                     the surname of the user that inserted the recipe
     * @param ingredients
     *                     the list of ingredients rquired by the recipe
     */
    public Recipe(int id, String name, String description, int time_minutes, Difficulty difficulty, String image_url,
            Date creation_date,
            int user_id, String user_name, String user_surname, List<Ingredient> ingredients) {
        this(id, name, description, time_minutes, difficulty, image_url, creation_date, user_id, ingredients);
        this.user_name = user_name;
        this.user_surname = user_surname;
    }

    /**
     * Creates a new recipe, with optional nLikes parameter
     * 
     * @param id
     *                     the identifier of the recipe
     * @param name
     *                     the name of the recipe
     * @param description
     *                     the description of the recipe
     * @param time_minutes
     *                     the duration in minutes of the recipe
     * @param difficulty
     *                     the difficulty of the recipe
     * @param image_url
     *                     the url of the image of the recipe
     * @param nLikes
     *                     the number of likes of the recipe
     * @param ingredients
     *                     the list of ingredients rquired by the recipe
     */
    public Recipe(int id, String name, String description, int time_minutes, Difficulty difficulty, String image_url,
            Date creation_date,
            int user_id, int nLikes, List<Ingredient> ingredients) {
        this(id, name, description, time_minutes, difficulty, image_url, creation_date, user_id, ingredients);
        this.nLikes = nLikes;
    }

    /**
     * Prints to string all the info about the recipe.
     */
    public String toString() {
        // print all the info
        return "Recipe %s : %s : %s : %s : %s : %s : %s : %s : %s : %s : %s : %s".formatted(id, name, description,
                time_minutes,
                difficulty, image_url, user_id, user_name, user_surname, creation_date, approved, nLikes);
    }

    @Override
    protected void writeJSON(OutputStream out) throws Exception {
        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);
        jg.writeStartObject(); // Start of the main object

        // Writing fields of the Recipe

        jg.writeNumberField("id", id);
        jg.writeStringField("name", name);
        jg.writeStringField("description", description);
        jg.writeNumberField("time_minutes", time_minutes);
        jg.writeStringField("image_url", image_url);
        jg.writeNumberField("user_id", user_id);

        if (difficulty != null)
            jg.writeStringField("difficulty", difficulty.toString());

        if (creation_date != null)
            jg.writeStringField("creation_date", creation_date.toString());

        // Optional fields
        if (user_name != null)
            jg.writeStringField("user_name", user_name);

        if (user_surname != null)
            jg.writeStringField("user_surname", user_surname);

        if (approved != null)
            jg.writeBooleanField("approved", approved);

        if (nLikes != -1)
            jg.writeNumberField("nLikes", nLikes);

        // Writing ingredients array
        jg.writeFieldName("ingredients");
        jg.writeStartArray(); // Start of the ingredients array

        // if is null no ingredients
        try {
            for (final Ingredient i : ingredients) {
                jg.writeStartObject(); // Start of an ingredient object
                jg.writeNumberField("id", i.getId());
                jg.writeStringField("name", i.getName());
                jg.writeEndObject(); // End of an ingredient object
            }
        } catch (NullPointerException e) {
            jg.writeStartObject(); // Start of an ingredient object
            jg.writeEndObject(); // End of an ingredient object
        }

        jg.writeEndArray(); // End of the ingredients array

        jg.writeEndObject(); // End of the main object
        jg.flush(); // Flush the JSON generator
    }

    /**
     * Returns the identifier of the recipe.
     * 
     * @return the identifier of the recipe.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the name of the recipe.
     * 
     * @return the name of the recipe.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the description of the recipe.
     * 
     * @return the description of the recipe.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the time duration of the recipe.
     * 
     * @return the time duration of the recipe.
     */
    public int getTime_minutes() {
        return time_minutes;
    }

    /**
     * Returns the time duration of the recipe as string.
     * If time is < 60 minutes returns string like "xx minutes" or "1 minute"
     * If time is >= 60 minutes returns string like "xx hours, xx minutes" or "1
     * hour, xx minutes"
     * If time is >= 60*24 minutes returns string like "xx days, xx hours" or "1
     * day, xx hours"
     * 
     * @return the time duration of the recipe formatted
     */
    public String getTime_minutes_formatted() {
        int days = time_minutes / (60 * 24);
        int hours = (time_minutes % (60 * 24)) / 60;
        int minutes = time_minutes % 60;
        if (days > 0) {
            if (hours > 0) {
                if (hours == 1) {
                    if (minutes > 0) {
                        if (minutes == 1) {
                            return "%d days, %d hour, %d minute".formatted(days, hours, minutes);
                        } else {
                            return "%d days, %d hour, %d minutes".formatted(days, hours, minutes);
                        }
                    } else {
                        return "%d days, %d hour".formatted(days, hours);
                    }

                } else {
                    if (minutes > 0) {
                        if (minutes == 1) {
                            return "%d days, %d hours, %d minute".formatted(days, hours, minutes);
                        } else {
                            return "%d days, %d hours, %d minutes".formatted(days, hours, minutes);
                        }
                    } else {
                        return "%d days, %d hours".formatted(days, hours);
                    }
                }

            } else {
                if (minutes > 0) {
                    if (minutes == 1) {
                        return "%d days, %d minute".formatted(days, minutes);
                    } else {
                        return "%d days, %d minutes".formatted(days, minutes);
                    }
                } else {
                    return "%d days".formatted(days);
                }
            }
        } else if (hours > 0) {
            if (hours == 1) {
                if (minutes > 0) {
                    if (minutes == 1) {
                        return "%d hour, %d minute".formatted(hours, minutes);
                    } else {
                        return "%d hour, %d minutes".formatted(hours, minutes);
                    }
                } else {
                    return "%d hour".formatted(hours);
                }

            } else {
                if (minutes > 0) {
                    if (minutes == 1) {
                        return "%d hours, %d minute".formatted(hours, minutes);
                    } else {
                        return "%d hours, %d minutes".formatted(hours, minutes);
                    }
                } else {
                    return "%d hours".formatted(hours);
                }
            }
        } else {
            if (minutes > 1) {
                return "%d minutes".formatted(minutes);
            } else {
                return "%d minute".formatted(minutes);
            }
        }

    }

    /**
     * Returns the difficulty of the recipe.
     * 
     * @return the difficulty of the recipe.
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Returns the url of the image of the recipe.
     * 
     * @return the url of the image of the recipe.
     */
    public String getImage_url() {
        return image_url;
    }

    /**
     * Returns the identifier of the user that inserted the recipe.
     * 
     * @return the identifier of the user that inserted the recipe.
     */
    public int getUser_id() {
        return user_id;
    }

    /**
     * Returns the name of the user that inserted the recipe.
     * 
     * @return the name of the user that inserted the recipe.
     */
    public String getUserName() {
        return user_name;
    }

    /**
     * Returns the surname of the user that inserted the recipe.
     * 
     * @return the surname of the user that inserted the recipe.
     */
    public String getUserSurname() {
        return user_surname;
    }

    /**
     * Returns the number of likes of the recipe.
     * 
     * @return the number of likes of the recipe.
     */
    public int getNLikes() {
        return nLikes;
    }

    /**
     * Returns the creation date of the recipe.
     * 
     * @return the creation date of the recipe.
     */
    public Date getCreationDate() {
        return creation_date;
    }

    /**
     * Returns the status of the recipe.
     * 
     * @return the status of the recipe.
     */
    public Boolean getApproved() {
        return approved;
    }

    /**
     * Returns the list of ingredients required by the recipe.
     * 
     * @return the list of ingredients required by the recipe.
     */
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * Returns the list of tags of the recipe.
     * 
     * @return the list of tags of the recipe.
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * Set an identifier for the recipe
     * 
     * @param id
     *           the identifier of the recipe
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set a name for the recipe
     * 
     * @param name
     *             the name of the recipe
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set a description for the recipe
     * 
     * @param description
     *                    the description of the recipe
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set a time duration for the recipe
     * 
     * @param time_minutes
     *                     the time duration of the recipe
     */
    public void setTime_minutes(int time_minutes) {
        this.time_minutes = time_minutes;
    }

    /**
     * Set a difficulty for the recipe
     * 
     * @param difficulty
     *                   the difficulty of the recipe
     */
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Set a url of an image for the recipe
     * 
     * @param image_url
     *                  the url of the image of the recipe
     */
    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    /**
     * Set the identifier of the user that inserted the recipe
     * 
     * @param user_id
     *                the identifier the user that inserted the recipe
     */
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    /**
     * Set the name of the user that inserted the recipe
     * 
     * @param user_name
     *                  the name the user that inserted the recipe
     */
    public void setUserName(String user_name) {
        this.user_name = name;
    }

    /**
     * Set the surname of the user that inserted the recipe
     * 
     * @param user_surname
     *                     the surname the user that inserted the recipe
     */
    public void setUserSurname(String user_surname) {
        this.user_surname = user_surname;
    }

    /**
     * Set the number of likes of the recipe
     * 
     * @param nLikes
     *               the number of likes of the recipe
     */
    public void setNLikes(int nLikes) {
        this.nLikes = nLikes;
    }

    /**
     * Set the creation date of the recipe
     * 
     * @param creation_date
     *                      the creation date of the recipe
     */
    public void setCreationDate(Date creation_date) {
        this.creation_date = creation_date;
    }

    /**
     * Set the status of the recipe
     * 
     * @param approved
     *                 the status of the recipe
     */
    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    /**
     * Set the list of ingredient required by the recipe
     * 
     * @param ingredients
     *                    the list of ingredients required by the recipe
     */
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Set the list of tags of the recipe
     * 
     * @param ingredients
     *                    the list of tags of the recipe
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
