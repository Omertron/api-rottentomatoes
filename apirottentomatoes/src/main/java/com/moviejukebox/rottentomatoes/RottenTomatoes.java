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

import com.moviejukebox.rottentomatoes.RottenTomatoesException.RottenTomatoesExceptionType;
import com.moviejukebox.rottentomatoes.model.Cast;
import com.moviejukebox.rottentomatoes.model.Clip;
import com.moviejukebox.rottentomatoes.model.RTMovie;
import com.moviejukebox.rottentomatoes.model.Review;
import com.moviejukebox.rottentomatoes.tools.ApiBuilder;
import com.moviejukebox.rottentomatoes.tools.FilteringLayout;
import com.moviejukebox.rottentomatoes.tools.WebBrowser;
import com.moviejukebox.rottentomatoes.wrapper.WrapperLists;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Connect to the Rotten Tomatoes web site and get the rating for a specific
 * movie
 *
 * @author Stuart.Boston
 *
 */
public class RottenTomatoes {

    // Logger
    private static final Logger LOGGER = Logger.getLogger(RottenTomatoes.class);
    // Properties map
    HashMap<String, String> properties = new HashMap<String, String>();
    // Main API site (Can't imagine why you need it!)
    private static final String URL_LISTS = ".json?apikey=";

    /*
     * RTMovie Lists
     */
    private static final String URL_BOX_OFFICE = "/lists/movies/box_office";
    private static final String URL_IN_THEATERS = "/lists/movies/in_theaters";
    private static final String URL_OPENING_MOVIES = "/lists/movies/opening";
    private static final String URL_UPCOMING_MOVIES = "/lists/movies/upcoming";

    /*
     * DVD Lists
     */
    private static final String URL_TOP_RENTALS = "/lists/dvds/top_rentals";
    private static final String URL_CURRENT_RELEASE_DVDS = "/lists/dvds/current_releases";
    private static final String URL_NEW_RELEASE_DVDS = "/lists/dvds/new_releases";
    private static final String URL_UPCOMING_DVDS = "/lists/dvds/upcoming";

    /*
     * Detailed Info
     */
    private static final String URL_MOVIES_INFO = "/movies/{movie-id}";
    private static final String URL_CAST_INFO = "/movies/{movie-id}/cast";
    private static final String URL_MOVIE_CLIPS = "/movies/{movie-id}/clips";
    private static final String URL_MOVIES_REVIEWS = "/movies/{movie-id}/reviews";
    private static final String URL_MOVIES_SIMILAR = "/movies/{movie-id}/similar";
    private static final String URL_MOVIES_ALIAS = "/movie_alias";

    /*
     * Search
     */
    private static final String URL_MOVIES_SEARCH = "/movies";

    /*
     * Top Level Lists
     */
    private static final String URL_LISTS_DIRECTORY = "/lists";
    private static final String URL_MOVIE_LISTS = "/lists/movies";
    private static final String URL_DVD_LISTS = "/lists/dvds";
    /*
     * Jackson JSON configuration
     */
    private static ObjectMapper mapper = new ObjectMapper();

    public RottenTomatoes(String apiKey) throws RottenTomatoesException {
        if (StringUtils.isBlank(apiKey)) {
            throw new RottenTomatoesException(RottenTomatoesExceptionType.NO_API_KEY, "No API Key provided!");
        }

        FilteringLayout.addApiKey(apiKey);
        ApiBuilder.addApiKey(apiKey);
    }

    /**
     * Set the web browser proxy information
     *
     * @param host
     * @param port
     * @param username
     * @param password
     */
    public void setProxy(String host, String port, String username, String password) {
        WebBrowser.setProxyHost(host);
        WebBrowser.setProxyPort(port);
        WebBrowser.setProxyUsername(username);
        WebBrowser.setProxyPassword(password);
    }

    /**
     * Set the web browser timeout settings
     *
     * @param webTimeoutConnect
     * @param webTimeoutRead
     */
    public void setTimeout(int webTimeoutConnect, int webTimeoutRead) {
        WebBrowser.setWebTimeoutConnect(webTimeoutConnect);
        WebBrowser.setWebTimeoutRead(webTimeoutRead);
    }

    /**
     * Displays top box office earning movies, sorted by most recent weekend
     * gross ticket sales.
     *
     * @param limit Limits the number of movies returned
     * @param country Provides localized data for the selected country
     */
    public List<RTMovie> getBoxOffice(String country, int limit) throws RottenTomatoesException {
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_BOX_OFFICE);
        properties.put(ApiBuilder.PROPERTY_LIMIT, ApiBuilder.validateLimit(limit));
        properties.put(ApiBuilder.PROPERTY_COUNTRY, ApiBuilder.validateCountry(country));

        try {
            String urlString = ApiBuilder.create(properties);
            String webPage = WebBrowser.request(urlString);

            WrapperLists wl = mapper.readValue(webPage, WrapperLists.class);
            if (wl.isValid()) {
                return wl.getMovies();
            } else {
                throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, wl.getError());
            }
        } catch (IOException ex) {
            throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, ex);
        }
    }

    /**
     * Retrieves movies currently in theaters
     *
     * @param country Provides localized data for the selected country
     * @param page The selected page of in theaters movies
     * @param pageLimit The amount of movies in theaters to show per page
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getInTheaters(String country, int page, int pageLimit) throws RottenTomatoesException {
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_IN_THEATERS);
        properties.put(ApiBuilder.PROPERTY_COUNTRY, ApiBuilder.validateCountry(country));
        properties.put(ApiBuilder.PROPERTY_PAGE, ApiBuilder.validatePage(page));
        properties.put(ApiBuilder.PROPERTY_PAGE_LIMIT, ApiBuilder.validatePageLimit(pageLimit));

        try {
            String urlString = ApiBuilder.create(properties);
            String webPage = WebBrowser.request(urlString);

            WrapperLists wl = mapper.readValue(webPage, WrapperLists.class);
            if (wl.isValid()) {
                return wl.getMovies();
            } else {
                throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, wl.getError());
            }
        } catch (IOException ex) {
            throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, ex);
        }
    }

    /**
     * Retrieves current opening movies
     *
     * @param country Provides localized data for the selected country
     * @param limit Limits the number of opening movies returned
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getOpeningMovies(String country, int limit) throws RottenTomatoesException {
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_OPENING_MOVIES);
        properties.put(ApiBuilder.PROPERTY_LIMIT, ApiBuilder.validateLimit(limit));
        properties.put(ApiBuilder.PROPERTY_COUNTRY, ApiBuilder.validateCountry(country));

        try {
            String urlString = ApiBuilder.create(properties);
            String webPage = WebBrowser.request(urlString);

            WrapperLists wl = mapper.readValue(webPage, WrapperLists.class);
            if (wl.isValid()) {
                return wl.getMovies();
            } else {
                throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, wl.getError());
            }
        } catch (IOException ex) {
            throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, ex);
        }
    }

    /**
     * Retrieves upcoming movies
     *
     * @param country Provides localized data for the selected country
     * @param page The selected page of in theaters movies
     * @param pageLimit The amount of movies in theaters to show per page
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getUpcomingMovies(String country, int page, int pageLimit) throws RottenTomatoesException {
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_UPCOMING_MOVIES);
        properties.put(ApiBuilder.PROPERTY_COUNTRY, ApiBuilder.validateCountry(country));
        properties.put(ApiBuilder.PROPERTY_PAGE, ApiBuilder.validatePage(page));
        properties.put(ApiBuilder.PROPERTY_PAGE_LIMIT, ApiBuilder.validatePageLimit(pageLimit));

        try {
            String urlString = ApiBuilder.create(properties);
            String webPage = WebBrowser.request(urlString);

            WrapperLists wl = mapper.readValue(webPage, WrapperLists.class);
            if (wl.isValid()) {
                return wl.getMovies();
            } else {
                throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, wl.getError());
            }
        } catch (IOException ex) {
            throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, ex);
        }
    }

    /**
     * Retrieves the current top DVD rentals
     *
     * @param country Provides localized data for the selected country
     * @param limit Limits the number of opening movies returned
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getTopRentals(String country, int limit) throws RottenTomatoesException {
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_TOP_RENTALS);
        properties.put(ApiBuilder.PROPERTY_LIMIT, ApiBuilder.validateLimit(limit));
        properties.put(ApiBuilder.PROPERTY_COUNTRY, ApiBuilder.validateCountry(country));

        try {
            String urlString = ApiBuilder.create(properties);
            String webPage = WebBrowser.request(urlString);

            WrapperLists wl = mapper.readValue(webPage, WrapperLists.class);
            if (wl.isValid()) {
                return wl.getMovies();
            } else {
                throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, wl.getError());
            }
        } catch (IOException ex) {
            throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, ex);
        }
    }

    /**
     * Retrieves current release DVDs
     *
     * @param country Provides localized data for the selected country
     * @param page The selected page of in theaters movies
     * @param pageLimit The amount of movies in theaters to show per page
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getCurrentReleaseDvds(String country, int page, int pageLimit) throws RottenTomatoesException {
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_CURRENT_RELEASE_DVDS);
        properties.put(ApiBuilder.PROPERTY_COUNTRY, ApiBuilder.validateCountry(country));
        properties.put(ApiBuilder.PROPERTY_PAGE, ApiBuilder.validatePage(page));
        properties.put(ApiBuilder.PROPERTY_PAGE_LIMIT, ApiBuilder.validatePageLimit(pageLimit));

        try {
            String urlString = ApiBuilder.create(properties);
            String webPage = WebBrowser.request(urlString);

            WrapperLists wl = mapper.readValue(webPage, WrapperLists.class);
            if (wl.isValid()) {
                return wl.getMovies();
            } else {
                throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, wl.getError());
            }
        } catch (IOException ex) {
            throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, ex);
        }
    }

    /**
     * Retrieves new release DVDs
     *
     * @param country Provides localized data for the selected country
     * @param page The selected page of in theaters movies
     * @param pageLimit The amount of movies in theaters to show per page
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getNewReleaseDvds(String country, int page, int pageLimit) throws RottenTomatoesException {
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_NEW_RELEASE_DVDS);
        properties.put(ApiBuilder.PROPERTY_COUNTRY, ApiBuilder.validateCountry(country));
        properties.put(ApiBuilder.PROPERTY_PAGE, ApiBuilder.validatePage(page));
        properties.put(ApiBuilder.PROPERTY_PAGE_LIMIT, ApiBuilder.validatePageLimit(pageLimit));

        try {
            String urlString = ApiBuilder.create(properties);
            String webPage = WebBrowser.request(urlString);

            WrapperLists wl = mapper.readValue(webPage, WrapperLists.class);
            if (wl.isValid()) {
                return wl.getMovies();
            } else {
                throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, wl.getError());
            }
        } catch (IOException ex) {
            throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, ex);
        }
    }

    /**
     * Retrieves current release DVDs
     *
     * @param country Provides localized data for the selected country
     * @param page The selected page of in theaters movies
     * @param pageLimit The amount of movies in theaters to show per page
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getUpcomingDvds(String country, int page, int pageLimit) throws RottenTomatoesException {
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_UPCOMING_DVDS);
        properties.put(ApiBuilder.PROPERTY_COUNTRY, ApiBuilder.validateCountry(country));
        properties.put(ApiBuilder.PROPERTY_PAGE, ApiBuilder.validatePage(page));
        properties.put(ApiBuilder.PROPERTY_PAGE_LIMIT, ApiBuilder.validatePageLimit(pageLimit));

        try {
            String urlString = ApiBuilder.create(properties);
            String webPage = WebBrowser.request(urlString);

            WrapperLists wl = mapper.readValue(webPage, WrapperLists.class);
            if (wl.isValid()) {
                return wl.getMovies();
            } else {
                throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, wl.getError());
            }
        } catch (IOException ex) {
            throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, ex);
        }
    }

    /**
     * Detailed information on a specific movie specified by Id.
     *
     * @param movieId RT Movie ID to locate
     * @return
     * @throws RottenTomatoesException
     */
    public RTMovie getDetailedInfo(int movieId) throws RottenTomatoesException {
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_MOVIES_INFO);

        try {
            String urlString = ApiBuilder.create(properties, movieId);
            String webPage = WebBrowser.request(urlString);

            RTMovie rtMovie = mapper.readValue(webPage, RTMovie.class);
            if (rtMovie.isValid()) {
                return rtMovie;
            } else {
                throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, rtMovie.getError());
            }
        } catch (IOException ex) {
            throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, ex);
        }
    }

    /**
     * Pulls the complete movie cast for a movie
     *
     * @param movieId RT Movie ID
     * @return
     * @throws RottenTomatoesException
     */
    public List<Cast> getCastInfo(int movieId) throws RottenTomatoesException {
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_CAST_INFO);

        try {
            String urlString = ApiBuilder.create(properties, movieId);
            String webPage = WebBrowser.request(urlString);

            WrapperLists wl = mapper.readValue(webPage, WrapperLists.class);

            if (wl.isValid()) {
                return wl.getCast();
            } else {
                throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, wl.getError());
            }
        } catch (IOException ex) {
            throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, ex);
        }
    }

    /**
     * Related movie clips and trailers for a movie
     *
     * @param movieId RT Movie ID
     * @return
     * @throws RottenTomatoesException
     */
    public List<Clip> getMovieClips(int movieId) throws RottenTomatoesException {
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_MOVIE_CLIPS);

        try {
            String urlString = ApiBuilder.create(properties, movieId);
            String webPage = WebBrowser.request(urlString);

            WrapperLists wl = mapper.readValue(webPage, WrapperLists.class);

            if (wl.isValid()) {
                return wl.getClips();
            } else {
                throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, wl.getError());
            }
        } catch (IOException ex) {
            throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, ex);
        }
    }

    /**
     * Retrieves the reviews for a movie
     *
     * @param movieId
     * @param reviewType
     * @param pageLimit
     * @param page
     * @param country
     * @return
     * @throws RottenTomatoesException
     */
    public List<Review> getMoviesReviews(int movieId, String reviewType, int pageLimit, int page, String country) throws RottenTomatoesException {
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_MOVIES_REVIEWS);
        properties.put(ApiBuilder.PROPERTY_REVIEW_TYPE, reviewType);
        properties.put(ApiBuilder.PROPERTY_PAGE_LIMIT, ApiBuilder.validatePageLimit(pageLimit));
        properties.put(ApiBuilder.PROPERTY_PAGE, ApiBuilder.validatePage(page));
        properties.put(ApiBuilder.PROPERTY_COUNTRY, ApiBuilder.validateCountry(country));

        try {
            String urlString = ApiBuilder.create(properties, movieId);
            String webPage = WebBrowser.request(urlString);

            WrapperLists wl = mapper.readValue(webPage, WrapperLists.class);

            if (wl.isValid()) {
                return wl.getReviews();
            } else {
                throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, wl.getError());
            }
        } catch (IOException ex) {
            throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, ex);
        }
    }

    /**
     * Returns similar movies to a movie
     *
     * @param movieId RT Movie ID
     * @param limit Limit number of returned movies
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getMoviesSimilar(int movieId, int limit) throws RottenTomatoesException {
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_MOVIES_SIMILAR);
        properties.put(ApiBuilder.PROPERTY_LIMIT, ApiBuilder.validateLimit(limit));

        try {
            String urlString = ApiBuilder.create(properties, movieId);
            String webPage = WebBrowser.request(urlString);

            WrapperLists wl = mapper.readValue(webPage, WrapperLists.class);

            if (wl.isValid()) {
                return wl.getMovies();
            } else {
                throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, wl.getError());
            }
        } catch (IOException ex) {
            throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, ex);
        }
    }

    /**
     * Provides a movie lookup by an id from a different vendor
     *
     * @param altMovieId
     * @param type
     * @return
     * @throws RottenTomatoesException
     */
    public RTMovie getMoviesAlias(String altMovieId, String type) throws RottenTomatoesException {
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_MOVIES_ALIAS);
        // remove the "tt" from the start of the ID if it's imdb
        if ("imdb".equalsIgnoreCase(type) && altMovieId.toLowerCase().startsWith("tt")) {
            properties.put(ApiBuilder.PROPERTY_ID, new String(altMovieId.substring(2)));
        } else {
            properties.put(ApiBuilder.PROPERTY_ID, altMovieId);
        }
        properties.put(ApiBuilder.PROPERTY_TYPE, type);

        try {
            String urlString = ApiBuilder.create(properties);
            String webPage = WebBrowser.request(urlString);

            RTMovie rtMovie = mapper.readValue(webPage, RTMovie.class);
            if (rtMovie.isValid()) {
                return rtMovie;
            } else {
                throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, rtMovie.getError());
            }
        } catch (IOException ex) {
            throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, ex);
        }
    }

    /**
     * The movies search endpoint for plain text queries. Let's you search for
     * movies!
     *
     * @param query
     * @param pageLimit
     * @param page
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getMoviesSearch(String query, int pageLimit, int page) throws RottenTomatoesException {
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_MOVIES_SEARCH);
        properties.put(ApiBuilder.PROPERTY_PAGE_LIMIT, ApiBuilder.validatePageLimit(pageLimit));
        properties.put(ApiBuilder.PROPERTY_PAGE, ApiBuilder.validatePage(page));

        try {
            properties.put(ApiBuilder.PROPERTY_QUERY, URLEncoder.encode(query, "UTF-8"));
            String urlString = ApiBuilder.create(properties);
            String webPage = WebBrowser.request(urlString);

            WrapperLists wl = mapper.readValue(webPage, WrapperLists.class);

            if (wl.isValid()) {
                return wl.getMovies();
            } else {
                throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, wl.getError());
            }
        } catch (UnsupportedEncodingException ex) {
            throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, ex);
        } catch (IOException ex) {
            throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, ex);
        }
    }

    /**
     * Displays the top level lists available in the API
     *
     * @return
     * @throws RottenTomatoesException
     */
    public Map<String, String> getListsDirectory() throws RottenTomatoesException {
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_LISTS_DIRECTORY);

        try {
            String urlString = ApiBuilder.create(properties);
            String webPage = WebBrowser.request(urlString);

            WrapperLists wl = mapper.readValue(webPage, WrapperLists.class);

            if (wl.isValid()) {
                return wl.getLinks();
            } else {
                throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, wl.getError());
            }
        } catch (IOException ex) {
            throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, ex);
        }
    }

    /**
     * Shows the movie lists available
     *
     * @return
     * @throws RottenTomatoesException
     */
    public Map<String, String> getMovieListsDirectory() throws RottenTomatoesException {
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_MOVIE_LISTS);

        try {
            String urlString = ApiBuilder.create(properties);
            String webPage = WebBrowser.request(urlString);

            WrapperLists wl = mapper.readValue(webPage, WrapperLists.class);

            if (wl.isValid()) {
                return wl.getLinks();
            } else {
                throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, wl.getError());
            }
        } catch (IOException ex) {
            throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, ex);
        }
    }

    /**
     * Shows the DVD lists available
     *
     * @return
     * @throws RottenTomatoesException
     */
    public Map<String, String> getDvdListsDirectory() throws RottenTomatoesException {
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_DVD_LISTS);

        try {
            String urlString = ApiBuilder.create(properties);
            String webPage = WebBrowser.request(urlString);

            WrapperLists wl = mapper.readValue(webPage, WrapperLists.class);

            if (wl.isValid()) {
                return wl.getLinks();
            } else {
                throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, wl.getError());
            }
        } catch (IOException ex) {
            throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, ex);
        }
    }
}