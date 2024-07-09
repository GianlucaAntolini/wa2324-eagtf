package it.unipd.dei.webapp.rest;

import it.unipd.dei.webapp.dao.GetRankedRecipesDAO;

import it.unipd.dei.webapp.resource.Actions;
import it.unipd.dei.webapp.resource.Message;
import it.unipd.dei.webapp.resource.ResourceList;
import it.unipd.dei.webapp.resource.CacheEntry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.http.HttpSession;

import it.unipd.dei.webapp.resource.Recipe;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * A REST resource for listing items (e.g., recipes, products, etc.).
 *
 * @author Nicola Ferro (ferro@dei.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class RankedRecipeRR extends AbstractRR {

    private final String CacheTag = "RankedRecipeRR";
    // 1 minute (poll on client is every 5 sec)
    private final long expirationTime = 60000;

    public RankedRecipeRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.RQ_FOR_SEARCH, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {

        List<Recipe> rec = null;
        Message m = null;
        Map<String, CacheEntry<List<Recipe>>> cache = new HashMap<>();

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
            // check if exist an object cash
            if (session.getAttribute("cache") != null) {
                LOGGER.info("Retriving cache " + session.getAttribute("cache"));
                cache = (Map<String, CacheEntry<List<Recipe>>>) req.getSession().getAttribute("cache");
                session.setAttribute("cache", cache);
            }

            // retrive the cached data
            CacheEntry<List<Recipe>> cachedResult = cache.get(CacheTag);

            // if (cachedResult != null ) {
            //
            // LOGGER.info("\n\n\nChecking if cachedResult is expired"+
            // cachedResult.isCacheExpired() +"\n\n\n\n\n\nx ");
            // }

            // check if there is cached data and if is appropiate (not expired and long
            // enough)

            if (cachedResult != null && !cachedResult.isCacheExpired()) {
                rec = cachedResult.getData();
                // if is bigger we resize
                if (rec.size() >= size) {
                    cache.put(CacheTag, new CacheEntry<>(rec, System.currentTimeMillis(), expirationTime));
                    rec = rec.subList(0, size);
                    LOGGER.info("Using cached result for cached size %d request size %d", rec.size(), size);
                } else {
                    // if require a bigger number of samples we need do redo the search
                    rec = new GetRankedRecipesDAO(con, size).access().getOutputParam();
                    cache.put(CacheTag, new CacheEntry<>(rec, System.currentTimeMillis(), expirationTime));
                    session.setAttribute("cache", cache);
                    LOGGER.info("Using database result for cached size %d request size %d", rec.size(), size);
                }
            } else {
                // check if lower than the qulen requested (trim)
                rec = new GetRankedRecipesDAO(con, size).access().getOutputParam();
                cache.put(CacheTag, new CacheEntry<>(rec, System.currentTimeMillis(), expirationTime));
                session.setAttribute("cache", cache);
                LOGGER.info("Using database result for cached size %d request size %d", rec.size(), size);
            }
            // Converting to a ResourceList
            ResourceList<Recipe> resourceList = new ResourceList<>(rec);
            if (rec != null || resourceList != null) {
                res.setStatus(HttpServletResponse.SC_OK);
                resourceList.toJSON(res.getOutputStream());
            } else { // it should not happen
                m = new Message("Somenthing went wrong in RankedRecipeRR ", "E5A1", null);
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
