/*
 *      Copyright (c) 2004-2016 Stuart Boston
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
import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RottenTomatoesApiTest {

    private static final Logger LOG = LoggerFactory.getLogger(RottenTomatoesApiTest.class);
    private static final String API_KEY = "rnt8xak564a8sxkts5xkqj5z";
    private static RottenTomatoesApi rt;
    private static final boolean SHORT_TEST = false;
    private static final boolean FULL_TEST = true;
    // Test values
    private static final String COUNTRY_US = "us";
    private static final int LIMIT = 5;
    private static final int PAGE = 1;
    private static final int PAGE_LIMIT = 2;
    private static final int MOVIE_ID = 770672122;
    private static final String ALT_MOVIE_ID = "tt0435761";
    private static final String SEARCH_IMDB = "imdb";
    private static final String SEARCH_QUERY = "Toy Story 3 2010";
    private static final String WRONG_NUMBER_OF_RESULTS = "Wrong number of results";
    private static final String NO_LISTS_FOUND = "No lists found!";

    @BeforeClass
    public static void setUpClass() throws RottenTomatoesException {
        TestLogger.configure();
        rt = new RottenTomatoesApi(API_KEY);
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
        assertEquals(WRONG_NUMBER_OF_RESULTS, LIMIT, result.size());
        // Do the list tests
        assertMovieList(result, SHORT_TEST);
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
        assertEquals(WRONG_NUMBER_OF_RESULTS, PAGE_LIMIT, result.size());
        // Do the list tests
        assertMovieList(result, SHORT_TEST);
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
        assertEquals(WRONG_NUMBER_OF_RESULTS, LIMIT, result.size());
        // Do the list tests
        assertMovieList(result, SHORT_TEST);
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
        assertEquals(WRONG_NUMBER_OF_RESULTS, PAGE_LIMIT, result.size());
        // Do the list tests
        assertMovieList(result, SHORT_TEST);
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
        assertEquals(WRONG_NUMBER_OF_RESULTS, LIMIT, result.size());
        // Do the list tests
        assertMovieList(result, SHORT_TEST);
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
        assertEquals(WRONG_NUMBER_OF_RESULTS, PAGE_LIMIT, result.size());
        // Do the list tests
        assertMovieList(result, SHORT_TEST);
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
        assertEquals(WRONG_NUMBER_OF_RESULTS, PAGE_LIMIT, result.size());
        // Do the list tests
        assertMovieList(result, SHORT_TEST);
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
        // Do the list tests
        assertMovieList(result, SHORT_TEST);
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
        assertEquals("Incorrect movie returned", "Toy Story 3", result.getTitle());
        assertMovie(result, FULL_TEST);
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
        // Do the list tests
        assertMovieList(result, SHORT_TEST);
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
        // Do the list tests
        assertMovieList(result, SHORT_TEST);
    }

    /**
     * Test of getListsDirectory method, of class RottenTomatoesApi.
     *
     * @throws RottenTomatoesException
     */
    @Test
    public void testGetListsDirectory() throws RottenTomatoesException {
        LOG.info("getListsDirectory");

        Map<String, String> result = rt.getListsDirectory();
        assertFalse(NO_LISTS_FOUND, result.isEmpty());
    }

    /**
     * Test of getMovieListsDirectory method, of class RottenTomatoesApi.
     *
     * @throws RottenTomatoesException
     */
    @Test
    public void testGetMovieListsDirectory() throws RottenTomatoesException {
        LOG.info("getMovieListsDirectory");

        Map<String, String> result = rt.getMovieListsDirectory();
        assertFalse(NO_LISTS_FOUND, result.isEmpty());
    }

    /**
     * Test of getDvdListsDirectory method, of class RottenTomatoesApi.
     *
     * @throws RottenTomatoesException
     */
    @Test
    public void testGetDvdListsDirectory() throws RottenTomatoesException {
        LOG.info("getDvdListsDirectory");

        Map<String, String> result = rt.getDvdListsDirectory();
        assertFalse(NO_LISTS_FOUND, result.isEmpty());
    }

    /**
     * Scan through the movie list and test each one
     *
     * @param movieList
     * @param fullTests
     */
    private void assertMovieList(final List<RTMovie> movieList, final boolean fullTests) {
        assertNotNull("Movie list is null", movieList);
        assertFalse("Movie list is empty", movieList.isEmpty());

        for (RTMovie movie : movieList) {
            assertMovie(movie, fullTests);
        }
    }

    /**
     * Run a set of assertions on a movie object to ensure it's populated
     * correctly
     *
     * @param movie
     * @param fullTests
     */
    private void assertMovie(final RTMovie movie, final boolean fullTests) {
        assertNotNull("Null movie object", movie);
        assertTrue("Invalid movie returned", movie.isValid());

        // Short Tests
        assertTrue("No ID", movie.getId() > 0);
        assertTrue("No title", StringUtils.isNotBlank(movie.getTitle()));
        assertTrue("No Year", movie.getYear() > 0);
        assertTrue("No MPAA", StringUtils.isNotBlank(movie.getMpaaRating()));
        assertTrue("No Runtime", movie.getRuntime() > 0);
        // Skip the critics
        assertFalse("No release dates", movie.getReleaseDates().isEmpty());
        assertFalse("No ratings", movie.getRatings().isEmpty());
        assertFalse("No artwork", movie.getArtwork().isEmpty());
        assertFalse("No cast", movie.getCast().isEmpty());

        if (fullTests) {
            assertFalse("No genres", movie.getGenres().isEmpty());
            assertFalse("No directors", movie.getDirectors().isEmpty());
            assertTrue("No studio", StringUtils.isNotBlank(movie.getStudio()));
        }
    }
}
