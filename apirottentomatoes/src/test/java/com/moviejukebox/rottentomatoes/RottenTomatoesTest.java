/*
 *      Copyright (c) 2004-2012 YAMJ Members
 *      http://code.google.com/p/moviejukebox/people/list
 *
 *      Web: http://code.google.com/p/moviejukebox/
 *
 *      This software is licensed under a Creative Commons License
 *      See this page: http://code.google.com/p/moviejukebox/wiki/License
 *
 *      For any reuse or distribution, you must make clear to others the
 *      license terms of this work.
 */
package com.moviejukebox.rottentomatoes;

import com.moviejukebox.rottentomatoes.model.Cast;
import com.moviejukebox.rottentomatoes.model.Clip;
import com.moviejukebox.rottentomatoes.model.RTMovie;
import com.moviejukebox.rottentomatoes.model.Review;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class RottenTomatoesTest extends TestCase {

    private static final Logger LOGGER = Logger.getLogger(RottenTomatoesTest.class);
    public RottenTomatoes rt;
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
        rt = new RottenTomatoes("rnt8xak564a8sxkts5xkqj5z");
    }

    /**
     * Test of getBoxOffice method, of class RottenTomatoes.
     */
    @Test
    public void testGetBoxOffice() throws Exception {
        LOGGER.info("getBoxOffice");
        List<RTMovie> result = rt.getBoxOffice(country, limit);
        assertEquals("Wrong number of results", limit, result.size());
    }

    /**
     * Test of getInTheaters method, of class RottenTomatoes.
     */
    @Test
    public void testGetInTheaters() throws Exception {
        LOGGER.info("getInTheaters");
        List<RTMovie> result = rt.getInTheaters(country, page, pageLimit);
        assertEquals("Wrong number of results", pageLimit, result.size());
    }

    /**
     * Test of getOpeningMovies method, of class RottenTomatoes.
     */
    @Test
    public void testGetOpeningMovies() throws Exception {
        LOGGER.info("getOpeningMovies");
        List<RTMovie> result = rt.getOpeningMovies(country, limit);
        assertEquals("Wrong number of results", limit, result.size());
    }

    /**
     * Test of getUpcomingMovies method, of class RottenTomatoes.
     */
    @Test
    public void testGetUpcomingMovies() throws Exception {
        LOGGER.info("getUpcomingMovies");
        List<RTMovie> result = rt.getUpcomingMovies(country, page, pageLimit);
        assertEquals("Wrong number of results", pageLimit, result.size());
    }

    /**
     * Test of getTopRentals method, of class RottenTomatoes.
     */
    @Test
    public void testGetTopRentals() throws Exception {
        LOGGER.info("getTopRentals");
        List<RTMovie> result = rt.getTopRentals(country, limit);
        assertEquals("Wrong number of results", limit, result.size());
    }

    /**
     * Test of getCurrentReleaseDvds method, of class RottenTomatoes.
     */
    @Test
    public void testGetCurrentReleaseDvds() throws Exception {
        LOGGER.info("getCurrentReleaseDvds");
        List<RTMovie> result = rt.getCurrentReleaseDvds(country, page, pageLimit);
        assertEquals("Wrong number of results", pageLimit, result.size());
    }

    /**
     * Test of getNewReleaseDvds method, of class RottenTomatoes.
     */
    @Test
    public void testGetNewReleaseDvds() throws Exception {
        LOGGER.info("getNewReleaseDvds");
        List<RTMovie> result = rt.getNewReleaseDvds(country, page, pageLimit);
        assertEquals("Wrong number of results", pageLimit, result.size());
    }

    /**
     * Test of getUpcomingDvds method, of class RottenTomatoes.
     */
    @Test
    public void testGetUpcomingDvds() throws Exception {
        LOGGER.info("getUpcomingDvds");
        List<RTMovie> result = rt.getUpcomingDvds(country, page, pageLimit);
        assertEquals("Wrong number of results", pageLimit, result.size());
    }

    /**
     * Test of getDetailedInfo method, of class RottenTomatoes.
     */
    @Test
    public void testGetDetailedInfo() throws Exception {
        LOGGER.info("getDetailedInfo");
        RTMovie result = rt.getDetailedInfo(movieId);
        assertEquals("Incorrect movie returned", "Blade Runner", result.getTitle());
    }

    /**
     * Test of getCastInfo method, of class RottenTomatoes.
     */
    @Test
    public void testGetCastInfo() throws Exception {
        LOGGER.info("getCastInfo");
        List<Cast> result = rt.getCastInfo(movieId);
        assertFalse("No cast information!", result.isEmpty());
    }

    /**
     * Test of getMovieClips method, of class RottenTomatoes.
     */
    @Test
    public void testGetMovieClips() throws Exception {
        LOGGER.info("getMovieClips");
        List<Clip> result = rt.getMovieClips(movieId);
        assertFalse("No clip information!", result.isEmpty());
    }

    /**
     * Test of getMoviesReviews method, of class RottenTomatoes.
     */
    @Test
    public void testGetMoviesReviews() throws Exception {
        LOGGER.info("getMoviesReviews");
        List<Review> result = rt.getMoviesReviews(movieId, "", pageLimit, page, country);
        assertFalse("No review information!", result.isEmpty());
    }

    /**
     * Test of getMoviesSimilar method, of class RottenTomatoes.
     */
    @Test
    public void testGetMoviesSimilar() throws Exception {
        LOGGER.info("getMoviesSimilar");
        List<RTMovie> result = rt.getMoviesSimilar(movieId, limit);
        assertFalse("No similar movies information!", result.isEmpty());
    }

    /**
     * Test of getMoviesAlias method, of class RottenTomatoes.
     */
    @Test
    public void testGetMoviesAlias() throws Exception {
        LOGGER.info("getMoviesAlias - This is a very buggy method");
        RTMovie result = rt.getMoviesAlias(altMovieId, type);
        assertFalse("Something really wrong here!", result == null);
    }

    /**
     * Test of getMoviesSearch method, of class RottenTomatoes.
     */
    @Test
    public void testGetMoviesSearch() throws Exception {
        LOGGER.info("getMoviesSearch");
        List<RTMovie> result = rt.getMoviesSearch(query, pageLimit, page);
        assertFalse("No movies found!", result.isEmpty());
    }

    /**
     * Test of getListsDirectory method, of class RottenTomatoes.
     */
    @Test
    public void testGetListsDirectory() throws Exception {
        LOGGER.info("getListsDirectory");
        Map result = rt.getListsDirectory();
        assertFalse("No lists found!", result.isEmpty());
    }

    /**
     * Test of getMovieListsDirectory method, of class RottenTomatoes.
     */
    @Test
    public void testGetMovieListsDirectory() throws Exception {
        LOGGER.info("getMovieListsDirectory");
        Map result = rt.getMovieListsDirectory();
        assertFalse("No lists found!", result.isEmpty());
    }

    /**
     * Test of getDvdListsDirectory method, of class RottenTomatoes.
     */
    @Test
    public void testGetDvdListsDirectory() throws Exception {
        LOGGER.info("getDvdListsDirectory");
        Map result = rt.getDvdListsDirectory();
        assertFalse("No lists found!", result.isEmpty());
    }
}
