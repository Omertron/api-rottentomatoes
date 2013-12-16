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
import java.util.List;
import java.util.Map;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RottenTomatoesApiTest {

    private static final Logger LOG = LoggerFactory.getLogger(RottenTomatoesApiTest.class);
    private static final String API_KEY = "rnt8xak564a8sxkts5xkqj5z";
    public static RottenTomatoesApi rt;
    // Test values
    private static final String COUNTRY_US = "us";
    private static final int LIMIT = 5;
    private static final int PAGE = 1;
    private static final int PAGE_LIMIT = 2;
    private static final int MOVIE_ID = 12886;
    private static final String ALT_MOVIE_ID = "tt0083658";
    private static final String SEARCH_IMDB = "imdb";
    private static final String SEARCH_QUERY = "Blade Runner 1982";

    @BeforeClass
    public static void setUpClass() throws Exception {
        TestLogger.Configure();
        rt = new RottenTomatoesApi(API_KEY);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getBoxOffice method, of class RottenTomatoesApi.
     *
     * @throws RottenTomatoesException
     */
    @Test
    public void testGetBoxOffice() throws RottenTomatoesException {
        LOG.info("getBoxOffice");

        List<RTMovie> result = rt.getBoxOffice(COUNTRY_US, LIMIT);
        assertEquals("Wrong number of results", LIMIT, result.size());
        sleeper();
    }

    /**
     * Test of getInTheaters method, of class RottenTomatoesApi.
     *
     * @throws RottenTomatoesException
     */
    @Test
    public void testGetInTheaters() throws RottenTomatoesException {
        LOG.info("getInTheaters");
        List<RTMovie> result = rt.getInTheaters(COUNTRY_US, PAGE, PAGE_LIMIT);
        assertEquals("Wrong number of results", PAGE_LIMIT, result.size());
        sleeper();
    }

    /**
     * Test of getOpeningMovies method, of class RottenTomatoesApi.
     *
     * @throws RottenTomatoesException
     */
    @Test
    public void testGetOpeningMovies() throws RottenTomatoesException {
        LOG.info("getOpeningMovies");
        List<RTMovie> result = rt.getOpeningMovies(COUNTRY_US, LIMIT);
        assertEquals("Wrong number of results", LIMIT, result.size());
        sleeper();
    }

    /**
     * Test of getUpcomingMovies method, of class RottenTomatoesApi.
     *
     * @throws RottenTomatoesException
     */
    @Test
    public void testGetUpcomingMovies() throws RottenTomatoesException {
        LOG.info("getUpcomingMovies");
        List<RTMovie> result = rt.getUpcomingMovies(COUNTRY_US, PAGE, PAGE_LIMIT);
        assertEquals("Wrong number of results", PAGE_LIMIT, result.size());
        sleeper();
    }

    /**
     * Test of getTopRentals method, of class RottenTomatoesApi.
     *
     * @throws RottenTomatoesException
     */
    @Test
    public void testGetTopRentals() throws RottenTomatoesException {
        LOG.info("getTopRentals");
        List<RTMovie> result = rt.getTopRentals(COUNTRY_US, LIMIT);
        assertEquals("Wrong number of results", LIMIT, result.size());
        sleeper();
    }

    /**
     * Test of getCurrentReleaseDvds method, of class RottenTomatoesApi.
     *
     * @throws RottenTomatoesException
     */
    @Test
    public void testGetCurrentReleaseDvds() throws RottenTomatoesException {
        LOG.info("getCurrentReleaseDvds");
        List<RTMovie> result = rt.getCurrentReleaseDvds(COUNTRY_US, PAGE, PAGE_LIMIT);
        assertEquals("Wrong number of results", PAGE_LIMIT, result.size());
        sleeper();
    }

    /**
     * Test of getNewReleaseDvds method, of class RottenTomatoesApi.
     *
     * @throws RottenTomatoesException
     */
    @Test
    public void testGetNewReleaseDvds() throws RottenTomatoesException {
        LOG.info("getNewReleaseDvds");
        List<RTMovie> result = rt.getNewReleaseDvds(COUNTRY_US, PAGE, PAGE_LIMIT);
        assertEquals("Wrong number of results", PAGE_LIMIT, result.size());
        sleeper();
    }

    /**
     * Test of getUpcomingDvds method, of class RottenTomatoesApi.
     *
     * @throws RottenTomatoesException
     */
    @Test
    public void testGetUpcomingDvds() throws RottenTomatoesException {
        LOG.info("getUpcomingDvds");
        List<RTMovie> result = rt.getUpcomingDvds(COUNTRY_US, PAGE, PAGE_LIMIT);
        assertNotNull("Null object returned", result);
        sleeper();
    }

    /**
     * Test of getDetailedInfo method, of class RottenTomatoesApi.
     *
     * @throws RottenTomatoesException
     */
    @Test
    public void testGetDetailedInfo() throws RottenTomatoesException {
        LOG.info("getDetailedInfo");
        RTMovie result = rt.getDetailedInfo(MOVIE_ID);
        assertEquals("Incorrect movie returned", "Blade Runner", result.getTitle());
        sleeper();
    }

    /**
     * Test of getCastInfo method, of class RottenTomatoesApi.
     *
     * @throws RottenTomatoesException
     */
    @Test
    public void testGetCastInfo() throws RottenTomatoesException {
        LOG.info("getCastInfo");
        List<RTCast> result = rt.getCastInfo(MOVIE_ID);
        assertFalse("No cast information!", result.isEmpty());
        sleeper();
    }

    /**
     * Test of getMovieClips method, of class RottenTomatoesApi.
     *
     * @throws RottenTomatoesException
     */
    @Test
    public void testGetMovieClips() throws RottenTomatoesException {
        LOG.info("getMovieClips");
        List<RTClip> result = rt.getMovieClips(MOVIE_ID);
        assertFalse("No clip information!", result.isEmpty());
        sleeper();
    }

    /**
     * Test of getMoviesReviews method, of class RottenTomatoesApi.
     *
     * @throws RottenTomatoesException
     */
    @Test
    public void testGetMoviesReviews() throws RottenTomatoesException {
        LOG.info("getMoviesReviews");
        List<Review> result = rt.getMoviesReviews(MOVIE_ID, "", PAGE_LIMIT, PAGE, COUNTRY_US);
        assertFalse("No review information!", result.isEmpty());
        sleeper();
    }

    /**
     * Test of getMoviesSimilar method, of class RottenTomatoesApi.
     *
     * @throws RottenTomatoesException
     */
    @Test
    public void testGetMoviesSimilar() throws RottenTomatoesException {
        LOG.info("getMoviesSimilar");
        List<RTMovie> result = rt.getMoviesSimilar(MOVIE_ID, LIMIT);
        assertFalse("No similar movies information!", result.isEmpty());
        sleeper();
    }

    /**
     * Test of getMoviesAlias method, of class RottenTomatoesApi.
     *
     * @throws RottenTomatoesException
     */
    @Test
    public void testGetMoviesAlias() throws RottenTomatoesException {
        LOG.info("getMoviesAlias - This is a very buggy method");
        RTMovie result = rt.getMoviesAlias(ALT_MOVIE_ID, SEARCH_IMDB);
        assertFalse("Something really wrong here!", result == null);
        sleeper();
    }

    /**
     * Test of getMoviesSearch method, of class RottenTomatoesApi.
     *
     * @throws RottenTomatoesException
     */
    @Test
    public void testGetMoviesSearch() throws RottenTomatoesException {
        LOG.info("getMoviesSearch");
        List<RTMovie> result = rt.getMoviesSearch(SEARCH_QUERY, PAGE_LIMIT, PAGE);
        assertFalse("No movies found!", result.isEmpty());
        sleeper();
    }

    /**
     * Test of getListsDirectory method, of class RottenTomatoesApi.
     *
     * @throws RottenTomatoesException
     */
    @Test
    public void testGetListsDirectory() throws RottenTomatoesException {
        LOG.info("getListsDirectory");
        Map result = rt.getListsDirectory();
        assertFalse("No lists found!", result.isEmpty());
        sleeper();
    }

    /**
     * Test of getMovieListsDirectory method, of class RottenTomatoesApi.
     *
     * @throws RottenTomatoesException
     */
    @Test
    public void testGetMovieListsDirectory() throws RottenTomatoesException {
        LOG.info("getMovieListsDirectory");
        Map result = rt.getMovieListsDirectory();
        assertFalse("No lists found!", result.isEmpty());
        sleeper();
    }

    /**
     * Test of getDvdListsDirectory method, of class RottenTomatoesApi.
     *
     * @throws RottenTomatoesException
     */
    @Test
    public void testGetDvdListsDirectory() throws RottenTomatoesException {
        LOG.info("getDvdListsDirectory");
        Map result = rt.getDvdListsDirectory();
        assertFalse("No lists found!", result.isEmpty());
        sleeper();
    }

    /**
     * We have a QoS limit of 5 queries per second, so slow the tests down
     */
    private void sleeper() {
        try {
            Thread.sleep(250);
        } catch (InterruptedException ex) {
            // No need to worry
        }
    }
}
