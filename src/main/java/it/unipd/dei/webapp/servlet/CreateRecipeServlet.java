
package it.unipd.dei.webapp.servlet;

import it.unipd.dei.webapp.dao.GetAllIngredientsDAO;
import it.unipd.dei.webapp.dao.GetAllTagsDAO;
import it.unipd.dei.webapp.dao.InsertRecipeDAO;
import it.unipd.dei.webapp.resource.Actions;
import it.unipd.dei.webapp.resource.Difficulty;
import it.unipd.dei.webapp.resource.Ingredient;
import it.unipd.dei.webapp.resource.Recipe;
import it.unipd.dei.webapp.resource.Tag;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Create a recipe in the database.
 */
public final class CreateRecipeServlet extends AbstractDatabaseServlet {

    /**
     * Handles the HTTP GET request from the client.
     * 
     * @param req
     *            the HTTP request from the client.
     * @param res
     *            the HTTP response from the server.
     * 
     * @throws ServletException
     *                          if any error occurs while executing the servlet.
     * @throws IOException
     *                          if any error occurs in the client/server
     *                          communication.
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setAction(Actions.CREATE_RECIPE);

        List<Ingredient> ingredients = null;
        List<Tag> tags = null;

        // get the current user's session
        HttpSession session = req.getSession(false);

        // check whether the use is logged
        if (session == null || session.getAttribute("user_id") == null) {
            // not logged, redirect to index.html
            res.sendRedirect(req.getContextPath());
            return;
        }

        try {
            // get list of ingredients
            ingredients = new GetAllIngredientsDAO(getConnection()).access().getOutputParam();

            // get list of ingredients
            tags = new GetAllTagsDAO(getConnection()).access().getOutputParam();

            // set the attribute to auto update the jsp
            req.setAttribute("usr_src_ing", req.getParameter("usr_src_ing"));
            req.setAttribute("usr_src_tag", req.getParameter("usr_src_tag"));
            req.setAttribute("ingredients", ingredients);
            req.setAttribute("tags", tags);
        } catch (SQLException ex) {
            LOGGER.error("Cannot get ingredients list: database error.", ex);
        }

        try {
            req.getRequestDispatcher("/jsp/create-recipe.jsp").forward(req, res);

        } catch (Exception ex) {
            LOGGER.error("An error occurred while forwarding the request to create-recipe.jsp", ex);
            throw ex;
        }
    }

    /**
     * Create a new recipe in the database.
     * 
     * @param req
     *            the HTTP request from the client.
     * @param res
     *            the HTTP response from the server.
     * 
     * @throws ServletException
     *                          if any error occurs while executing the servlet.
     * @throws IOException
     *                          if any error occurs in the client/server
     *                          communication.
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        try {
            // get the current user's session
            HttpSession session = req.getSession(false);

            // check whether the use is logged
            if (session == null || session.getAttribute("user_id") == null) {
                // not logged, redirect to index.html
                res.sendRedirect(req.getContextPath());
                return;
            }

            // get the id of the user
            Integer user_id = (int) session.getAttribute("user_id");

            // get list of all ingredients in the db
            List<Ingredient> dbIngredients = new GetAllIngredientsDAO(getConnection()).access().getOutputParam();

            // get list of all ingredients in the db
            List<Tag> dbTags = new GetAllTagsDAO(getConnection()).access().getOutputParam();

            // get the parameters from the request
            String recipeName = req.getParameter("recipeName");
            String recipeIngredientsString = req.getParameter("usr_src_ing");
            String[] recipeIngredients = recipeIngredientsString.split(",\\s*");

            String recipeTagsString = req.getParameter("usr_src_tag");
            String[] recipeTags = recipeTagsString.split(",\\s*");

            // Get the string containing the new ingredients (not present in the database)
            String newIngredientsString = req.getParameter("newIngredients");
            // Split the string into an array of strings (ignore every all the space after a
            // comma)
            String[] newIngredientStrings = newIngredientsString.split(",\\s*");

            String description = req.getParameter("comment");

            String difficultyString = req.getParameter("difficulty"); // read

            Difficulty difficulty = Difficulty.valueOf(difficultyString.toLowerCase());// convert to Difficulty object

            String image_url = req.getParameter("image_url");

            String minutesString = req.getParameter("time_minutes"); // read
            int time_minutes = Integer.parseInt(minutesString); // convert to int

            // create the list of ingredients objects
            List<Ingredient> ingredients = new ArrayList<>();

            // Add to the recipe the ingredients already present in the database
            for (String recipeIngredient : recipeIngredients)
                for (Ingredient ing : dbIngredients)
                    if (ing.getId() == Integer.valueOf(recipeIngredient)) {
                        ingredients.add(ing);
                        break;
                    }

            // Add to the recipe the ingredients that the user classified as "new"
            // NOTE THAT the check wether the ingredients are already present in the db
            // is done in the InsertRecipeDAO
            for (String newIngredient : newIngredientStrings)
                if (!newIngredient.isBlank() && newIngredient != "") {
                    Ingredient ingredient = new Ingredient(-1, newIngredient.trim());
                    ingredients.add(ingredient);
                }

            // create the list of ingredients objects
            List<Tag> tags = new ArrayList<>();

            // Add to the recipe the ingredients already present in the database
            for (String recipeTag : recipeTags)
                for (Tag tag : dbTags)
                    if (tag.getId() == Integer.valueOf(recipeTag)) {
                        tags.add(tag);
                        break;
                    }

            Date creation_date = new Date(System.currentTimeMillis());

            // log the ingredients strings and the ingredients for debug
            LOGGER.info("Ingredients strings: %s", ingredients.toString());

            // create the recipe in the database (with "fake" id -1)
            Recipe recipe = new Recipe(-1, recipeName, description, time_minutes, difficulty, image_url, creation_date,
                    user_id, ingredients);
            recipe.setTags(tags);

            // log the to string of the recipe
            LOGGER.info("Recipe to insert: %s", recipe.toString());

            // create and access createRecipeDAO
            new InsertRecipeDAO(getConnection(), recipe).access();

            // redirect to index.html
            res.sendRedirect(req.getContextPath());

            LOGGER.info("User with id %d successfully created a recipe.", user_id);
        } catch (SQLException e) {
            LOGGER.error("Cannot retrive ingredients from the database or cannot insert recipe in the database.", e);
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
        }

    }

}
