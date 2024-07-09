package it.unipd.dei.webapp.rest;

import it.unipd.dei.webapp.dao.GetSuggestionForSearchDAO;

import it.unipd.dei.webapp.resource.Actions;
import it.unipd.dei.webapp.resource.Message;
import it.unipd.dei.webapp.resource.ResourceList;
import it.unipd.dei.webapp.resource.CacheEntry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.http.HttpSession;

import it.unipd.dei.webapp.resource.Recipe;
import it.unipd.dei.webapp.resource.Ingredient;
import it.unipd.dei.webapp.resource.Tag;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import java.util.TreeSet;
import java.util.Iterator;

import java.util.ConcurrentModificationException;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * A REST resource for listing items (e.g., recipes, products, etc.).
 *
 * @author Nicola Ferro (ferro@dei.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class SuggestedRecipeRR extends AbstractRR {

    private final String CacheTag = "SuggestedRecipeRR";
    // 1 minute (poll on client is every 5 sec)
    private final long expirationTime = 60000;

    public SuggestedRecipeRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.RQ_FOR_SEARCH, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {

        Message m = null;
        Map<String, CacheEntry<List<Recipe>>> cache = new HashMap<>();

        List<Recipe> rec = null;
        List<Ingredient> ingredients = new ArrayList<>();
        List<Tag> tags = new ArrayList<>();

        // stores in order fashion to order them
        TreeSet<Integer> ingIds = new TreeSet<>();
        TreeSet<Integer> tagsIds = new TreeSet<>();

        int size = 15;
        HttpSession session = req.getSession();

        try {
            if (req.getParameter("quelen") != null & req.getParameter("quelen") != "") {
                try {
                    size = Integer.parseInt(req.getParameter("quelen"));
                } catch (NumberFormatException e) {
                    LOGGER.error("parameter queryLength is not an int " + req.getParameter("quelen"));
                }
            }
            // retrive the arguments
            String usr_src_ing = req.getParameter("usr_src_ing");
            String usr_src_tag = req.getParameter("usr_src_tag");

            // checks for the arguments of the servlet
            if (usr_src_ing != null & usr_src_tag != null) {

                // Split usr_src_ing into smaller strings
                String[] ingArray = usr_src_ing.split(",");
                // Split usr_src_tag into smaller strings
                String[] tagArray = usr_src_tag.split(",");

                // Iterate over ingArray, excluding the last element
                for (int i = 0; i < ingArray.length; i++) {
                    String ing = ingArray[i].trim(); // Trim to remove leading and trailing spaces
                    if (!ing.isEmpty()) {
                        // appending tags
                        int tmp = Integer.parseInt(ing);
                        ingredients.add(new Ingredient(tmp, ""));
                        ingIds.add(tmp);
                    }
                }

                // Iterate over tagArray, excluding the last element
                for (int i = 0; i < tagArray.length; i++) {
                    String tag = tagArray[i].trim(); // Trim to remove leading and trailing spaces
                    if (!tag.isEmpty()) {
                        // appending tags
                        int tmp = Integer.parseInt(tag);
                        tags.add(new Tag(tmp, ""));
                        tagsIds.add(tmp);

                    }
                }
            }

            // check if there is not object cash
            if (session.getAttribute("cache") != null) {
                LOGGER.info("Retriving cache " + session.getAttribute("cache"));
                cache = (Map<String, CacheEntry<List<Recipe>>>) req.getSession().getAttribute("cache");
                session.setAttribute("cache", cache);
            }

            // Construct the searchCacheTag
            // FORMAT IS CacheTag+ingIds+tagIds
            StringBuilder sb = new StringBuilder();
            sb.append(CacheTag);

            // Add ordered ingredients
            for (Integer ingId : ingIds) {
                sb.append(ingId).append(",");
            }

            // Add ordered tags
            for (Integer tagId : tagsIds) {
                sb.append(tagId).append(",");
            }

            // Generate the hash
            String searchCacheTag = DigestUtils.sha256Hex(sb.toString());

            LOGGER.info(String.format("Hash string is %s \nwith resulting hash %s", sb.toString(), searchCacheTag));

            CacheEntry<List<Recipe>> cachedResult = cache.get(searchCacheTag);

            if (cachedResult != null) {

                try {
                    LOGGER.info("We have cached results");
                    // Create an iterator over the entry set of the cache map
                    Iterator<Map.Entry<String, CacheEntry<List<Recipe>>>> iterator = cache.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, CacheEntry<List<Recipe>>> entry = iterator.next(); // Get the current entry
                        // Get the cache entry associated with the current key
                        CacheEntry<List<Recipe>> cacheEntry = entry.getValue();
                        // Check if the cache entry is expired
                        if (cacheEntry.isCacheExpired()) {
                            LOGGER.info("Found expired entry " + entry.getKey());
                            iterator.remove(); // Remove the current entry using the iterator
                        }
                    }
                } catch (ConcurrentModificationException e) {

                    // maybe send back a response that says that we can't handle
                    // the request or serve directly from the db
                    LOGGER.info(e);
                }

                rec = cachedResult.getData();
                // if is bigger we resize

                //
                //
                //
                // TODO FIX EDGE CASE
                // if the query returns less than what a less than asked size is memorized in
                // the cache
                // this cause this if to not get triggered because the cashed data is not enogh
                // long and it seem that
                // there is not enight thata in the cash triggering an update of the cash when
                // in reality is not necessary
                // since the resoult of the query would be the same
                //
                //
                // the solution would be to store in the cash the length of the previous search
                // and not using the length
                // of the memorized object since this could be lower but still right meanwhile
                // if the
                // requested is lower it is for sure the right choice (it would be added in
                // cachedResult())
                //
                //
                if (rec.size() >= size) {
                    // if is asked again we reset the timer of the cache
                    // by exchanging the object
                    cache.put(searchCacheTag, new CacheEntry<>(rec, System.currentTimeMillis(), expirationTime));
                    // resize to the requested length
                    rec = rec.subList(0, size);
                    LOGGER.info("Using cached result for cached size %d request size %d", rec.size(), size);

                } else {
                    // if require a bigger number of samples we need do redo the search
                    rec = new GetSuggestionForSearchDAO(con, ingredients, tags, size).access().getOutputParam();
                    cache.put(searchCacheTag, new CacheEntry<>(rec, System.currentTimeMillis(), expirationTime));
                    session.setAttribute("cache", cache);
                    LOGGER.info("Using database result for cached size %d request size %d", rec.size(), size);
                }
            } else {
                rec = new GetSuggestionForSearchDAO(con, ingredients, tags, size).access().getOutputParam();
                cache.put(searchCacheTag, new CacheEntry<>(rec, System.currentTimeMillis(), expirationTime));
                session.setAttribute("cache", cache);
                LOGGER.info("Using database result for cached size %d request size %d", rec.size(), size);
            }

            // Converting to a ResourceList
            ResourceList<Recipe> resourceList = new ResourceList<>(rec);
            if (rec != null || resourceList != null) {
                res.setStatus(HttpServletResponse.SC_OK);
                resourceList.toJSON(res.getOutputStream());
            } else { // it should not happen
                m = new Message("Somenthing went wrong in SearchRecipeRR", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }

        } catch (SQLException ex) {
            LOGGER.error("Cannot list items: unexpected database error.", ex);
            m = new Message("Cannot find Recipe: unexpected error.", "E5A1", null);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}
