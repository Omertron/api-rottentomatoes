/*
 *      Copyright (c) 2004-2013 Stuart Boston
 *
 *      This file is part of the RottenTomatoes API.
 *
 *      The RottenTomatoes API is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      The RottenTomatoes API is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with the RottenTomatoes API.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.omertron.rottentomatoesapi;

import com.omertron.rottentomatoesapi.model.RTCast;
import com.omertron.rottentomatoesapi.model.RTClip;
import com.omertron.rottentomatoesapi.model.RTMovie;
import com.omertron.rottentomatoesapi.model.Review;
import com.omertron.rottentomatoesapi.tools.FilteringLayout;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class RottenTomatoesApiTest extends TestCase {

    private static final Logger logger = Logger.getLogger(RottenTomatoesApiTest.class);
    public RottenTomatoesApi rt;
    // Test values
    private static final String country = "us";
    private static final int limit = 5;
    private static final int page = 1;
    private static final int pageLimit = 2;
    private static final int movieId = 12886;
    private static final String altMovieId = "tt0083658";
    private static final String type = "imdb";
    private static final String query = "Blade Runner 1982";

    @Before
    @Override
    public void setUp() throws Exception {
        rt = new RottenTomatoesApi("rnt8xak564a8sxkts5xkqj5z");
        // Set the logger level to TRACE
        Logger.getRootLogger().setLevel(Level.TRACE);
        // Make sure the filter isn't applied to the test output
        FilteringLayout.addApiKey("DO_NOT_MATCH");
    }

    /**
     * Test of getBoxOffice method, of class RottenTomatoesApi.
     */
    @Test
    public void testGetBoxOffice() throws Exception {
        logger.info("getBoxOffice");
        List<RTMovie> result = rt.getBoxOffice(country, limit);
        assertEquals("Wrong number of results", limit, result.size());
    }

    /**
     * Test of getInTheaters method, of class RottenTomatoesApi.
     */
    @Test
    public void testGetInTheaters() throws Exception {
        logger.info("getInTheaters");
        List<RTMovie> result = rt.getInTheaters(country, page, pageLimit);
        assertEquals("Wrong number of results", pageLimit, result.size());
    }

    /**
     * Test of getOpeningMovies method, of class RottenTomatoesApi.
     */
    @Test
    public void testGetOpeningMovies() throws Exception {
        logger.info("getOpeningMovies");
        List<RTMovie> result = rt.getOpeningMovies(country, limit);
        assertEquals("Wrong number of results", limit, result.size());
    }

    /**
     * Test of getUpcomingMovies method, of class RottenTomatoesApi.
     */
    @Test
    public void testGetUpcomingMovies() throws Exception {
        logger.info("getUpcomingMovies");
        List<RTMovie> result = rt.getUpcomingMovies(country, page, pageLimit);
        assertEquals("Wrong number of results", pageLimit, result.size());
    }

    /**
     * Test of getTopRentals method, of class RottenTomatoesApi.
     */
    @Test
    public void testGetTopRentals() throws Exception {
        logger.info("getTopRentals");
        List<RTMovie> result = rt.getTopRentals(country, limit);
        assertEquals("Wrong number of results", limit, result.size());
    }

    /**
     * Test of getCurrentReleaseDvds method, of class RottenTomatoesApi.
     */
    @Test
    public void testGetCurrentReleaseDvds() throws Exception {
        logger.info("getCurrentReleaseDvds");
        List<RTMovie> result = rt.getCurrentReleaseDvds(country, page, pageLimit);
        assertEquals("Wrong number of results", pageLimit, result.size());
    }

    /**
     * Test of getNewReleaseDvds method, of class RottenTomatoesApi.
     */
    @Test
    public void testGetNewReleaseDvds() throws Exception {
        logger.info("getNewReleaseDvds");
        List<RTMovie> result = rt.getNewReleaseDvds(country, page, pageLimit);
        assertEquals("Wrong number of results", pageLimit, result.size());
    }

    /**
     * Test of getUpcomingDvds method, of class RottenTomatoesApi.
     */
    @Test
    public void testGetUpcomingDvds() throws Exception {
        logger.info("getUpcomingDvds");
        List<RTMovie> result = rt.getUpcomingDvds(country, page, pageLimit);
        assertNotNull("Null object returned", result);
    }

    /**
     * Test of getDetailedInfo method, of class RottenTomatoesApi.
     */
    @Test
    public void testGetDetailedInfo() throws Exception {
        logger.info("getDetailedInfo");
        RTMovie result = rt.getDetailedInfo(movieId);
        assertEquals("Incorrect movie returned", "Blade Runner", result.getTitle());
    }

    /**
     * Test of getCastInfo method, of class RottenTomatoesApi.
     */
    @Test
    public void testGetCastInfo() throws Exception {
        logger.info("getCastInfo");
        List<RTCast> result = rt.getCastInfo(movieId);
        assertFalse("No cast information!", result.isEmpty());
    }

    /**
     * Test of getMovieClips method, of class RottenTomatoesApi.
     */
    @Test
    public void testGetMovieClips() throws Exception {
        logger.info("getMovieClips");
        List<RTClip> result = rt.getMovieClips(movieId);
        assertFalse("No clip information!", result.isEmpty());
    }

    /**
     * Test of getMoviesReviews method, of class RottenTomatoesApi.
     */
    @Test
    public void testGetMoviesReviews() throws Exception {
        logger.info("getMoviesReviews");
        List<Review> result = rt.getMoviesReviews(movieId, "", pageLimit, page, country);
        assertFalse("No review information!", result.isEmpty());
    }

    /**
     * Test of getMoviesSimilar method, of class RottenTomatoesApi.
     */
    @Test
    public void testGetMoviesSimilar() throws Exception {
        logger.info("getMoviesSimilar");
        List<RTMovie> result = rt.getMoviesSimilar(movieId, limit);
        assertFalse("No similar movies information!", result.isEmpty());
    }

    /**
     * Test of getMoviesAlias method, of class RottenTomatoesApi.
     */
    @Test
    public void testGetMoviesAlias() throws Exception {
        logger.info("getMoviesAlias - This is a very buggy method");
        RTMovie result = rt.getMoviesAlias(altMovieId, type);
        assertFalse("Something really wrong here!", result == null);
    }

    /**
     * Test of getMoviesSearch method, of class RottenTomatoesApi.
     */
    @Test
    public void testGetMoviesSearch() throws Exception {
        logger.info("getMoviesSearch");
        List<RTMovie> result = rt.getMoviesSearch(query, pageLimit, page);
        assertFalse("No movies found!", result.isEmpty());
    }

    /**
     * Test of getListsDirectory method, of class RottenTomatoesApi.
     */
    @Test
    public void testGetListsDirectory() throws Exception {
        logger.info("getListsDirectory");
        Map result = rt.getListsDirectory();
        assertFalse("No lists found!", result.isEmpty());
    }

    /**
     * Test of getMovieListsDirectory method, of class RottenTomatoesApi.
     */
    @Test
    public void testGetMovieListsDirectory() throws Exception {
        logger.info("getMovieListsDirectory");
        Map result = rt.getMovieListsDirectory();
        assertFalse("No lists found!", result.isEmpty());
    }

    /**
     * Test of getDvdListsDirectory method, of class RottenTomatoesApi.
     */
    @Test
    public void testGetDvdListsDirectory() throws Exception {
        logger.info("getDvdListsDirectory");
        Map result = rt.getDvdListsDirectory();
        assertFalse("No lists found!", result.isEmpty());
    }
}
