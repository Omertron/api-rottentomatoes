/*
 *      Copyright (c) 2004-2014 Stuart Boston
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

import com.omertron.rottentomatoesapi.RottenTomatoesException.RottenTomatoesExceptionType;
import com.omertron.rottentomatoesapi.model.RTCast;
import com.omertron.rottentomatoesapi.model.RTClip;
import com.omertron.rottentomatoesapi.model.RTMovie;
import com.omertron.rottentomatoesapi.model.Review;
import com.omertron.rottentomatoesapi.tools.ApiBuilder;
import com.omertron.rottentomatoesapi.tools.ResponseBuilder;
import com.omertron.rottentomatoesapi.wrapper.WrapperLists;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.yamj.api.common.http.CommonHttpClient;
import org.yamj.api.common.http.DefaultPoolingHttpClient;

/**
 * Connect to the Rotten Tomatoes web site and get the rating for a specific
 * movie
 *
 * @author Stuart.Boston
 *
 */
public class RottenTomatoesApi {

    private CommonHttpClient httpClient;
    private static final String ENCODING_UTF8 = "UTF-8";
    private ResponseBuilder response;
    /*
     * Properties map
     */
    private final Map<String, String> properties = new HashMap<String, String>();

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
    private static final String BASE_MOVIES = "/movies/";
    private static final String URL_MOVIES_INFO = BASE_MOVIES + ApiBuilder.MOVIE_ID;
    private static final String URL_CAST_INFO = BASE_MOVIES + ApiBuilder.MOVIE_ID + "/cast";
    private static final String URL_MOVIE_CLIPS = BASE_MOVIES + ApiBuilder.MOVIE_ID + "/clips";
    private static final String URL_MOVIES_REVIEWS = BASE_MOVIES + ApiBuilder.MOVIE_ID + "/reviews";
    private static final String URL_MOVIES_SIMILAR = BASE_MOVIES + ApiBuilder.MOVIE_ID + "/similar";
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
     * Defaults
     */
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_LIMIT = 0;
    private static final int DEFAULT_LIMIT = 0;
    private static final String DEFAULT_COUNTRY = "";
    private static final String DEFAULT_REVIEW = "";
    /*
     * Constants
     */
    private static final int LENGTH_OF_IMDB_PREFIX = 2;

    public RottenTomatoesApi(String apiKey) throws RottenTomatoesException {
        this(apiKey, new DefaultPoolingHttpClient());
    }

    public RottenTomatoesApi(String apiKey, CommonHttpClient httpClient) throws RottenTomatoesException {
        if (StringUtils.isBlank(apiKey)) {
            throw new RottenTomatoesException(RottenTomatoesExceptionType.NO_API_KEY, "No API Key provided!");
        }

        ApiBuilder.addApiKey(apiKey);
        this.httpClient = httpClient;
        this.response = new ResponseBuilder(httpClient);
    }

    /**
     * Set the web browser proxy information
     *
     * @param host
     * @param port
     * @param username
     * @param password
     */
    public void setProxy(String host, int port, String username, String password) {
        httpClient.setProxy(host, port, username, password);
    }

    /**
     * Set the web browser timeout settings
     *
     * @param webTimeoutConnect
     * @param webTimeoutRead
     */
    public void setTimeout(int webTimeoutConnect, int webTimeoutRead) {
        httpClient.setTimeouts(webTimeoutConnect, webTimeoutRead);
    }

    /**
     * Set the delay time between API retries when the account is over it's
     * limit
     *
     * @param retryDelay milliseconds to delay for, default is 500ms
     */
    public void setRetryDelay(long retryDelay) {
        response.setRetryDelay(retryDelay);
    }

    /**
     * Number of times to retry the API call when the account limit is hit.
     *
     * Once this limit is hit an exception is thrown.
     *
     * @param retryLimit Number of retries, default is 5
     */
    public void setRetryLimit(int retryLimit) {
        response.setRetryLimit(retryLimit);
    }

    /**
     * Displays top box office earning movies, sorted by most recent weekend
     * gross ticket sales.
     *
     * @param limit Limits the number of movies returned
     * @param country Provides localized data for the selected country
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getBoxOffice(String country, int limit) throws RottenTomatoesException {
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_BOX_OFFICE);
        properties.put(ApiBuilder.PROPERTY_LIMIT, ApiBuilder.validateLimit(limit));
        properties.put(ApiBuilder.PROPERTY_COUNTRY, ApiBuilder.validateCountry(country));

        WrapperLists wrapper = response.getResponse(WrapperLists.class, properties);
        if (wrapper != null && wrapper.getMovies() != null) {
            return wrapper.getMovies();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Displays top box office earning movies, sorted by most recent weekend
     * gross ticket sales.
     *
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getBoxOffice() throws RottenTomatoesException {
        return getBoxOffice(DEFAULT_COUNTRY, DEFAULT_LIMIT);
    }

    /**
     * Displays top box office earning movies, sorted by most recent weekend
     * gross ticket sales.
     *
     * @param country Provides localized data for the selected country
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getBoxOffice(String country) throws RottenTomatoesException {
        return getBoxOffice(country, DEFAULT_LIMIT);
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

        WrapperLists wrapper = response.getResponse(WrapperLists.class, properties);
        if (wrapper != null && wrapper.getMovies() != null) {
            return wrapper.getMovies();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves movies currently in theaters
     *
     * @param country Provides localized data for the selected country
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getInTheaters(String country) throws RottenTomatoesException {
        return getInTheaters(country, DEFAULT_PAGE, DEFAULT_PAGE_LIMIT);
    }

    /**
     * Retrieves movies currently in theaters
     *
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getInTheaters() throws RottenTomatoesException {
        return getInTheaters(DEFAULT_COUNTRY);
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

        WrapperLists wrapper = response.getResponse(WrapperLists.class, properties);
        if (wrapper != null && wrapper.getMovies() != null) {
            return wrapper.getMovies();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves current opening movies
     *
     * @param country Provides localized data for the selected country
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getOpeningMovies(String country) throws RottenTomatoesException {
        return getOpeningMovies(country, DEFAULT_LIMIT);
    }

    /**
     * Retrieves current opening movies
     *
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getOpeningMovies() throws RottenTomatoesException {
        return getOpeningMovies(DEFAULT_COUNTRY);
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

        WrapperLists wrapper = response.getResponse(WrapperLists.class, properties);
        if (wrapper != null && wrapper.getMovies() != null) {
            return wrapper.getMovies();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves upcoming movies
     *
     * @param country Provides localized data for the selected country
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getUpcomingMovies(String country) throws RottenTomatoesException {
        return getUpcomingMovies(country, DEFAULT_PAGE, DEFAULT_PAGE_LIMIT);
    }

    /**
     * Retrieves upcoming movies
     *
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getUpcomingMovies() throws RottenTomatoesException {
        return getUpcomingMovies(DEFAULT_COUNTRY);
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

        WrapperLists wrapper = response.getResponse(WrapperLists.class, properties);
        if (wrapper != null && wrapper.getMovies() != null) {
            return wrapper.getMovies();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves the current top DVD rentals
     *
     * @param country Provides localized data for the selected country
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getTopRentals(String country) throws RottenTomatoesException {
        return getTopRentals(country, DEFAULT_LIMIT);
    }

    /**
     * Retrieves the current top DVD rentals
     *
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getTopRentals() throws RottenTomatoesException {
        return getTopRentals(DEFAULT_COUNTRY);
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

        WrapperLists wrapper = response.getResponse(WrapperLists.class, properties);
        if (wrapper != null && wrapper.getMovies() != null) {
            return wrapper.getMovies();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves current release DVDs
     *
     * @param country Provides localized data for the selected country
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getCurrentReleaseDvds(String country) throws RottenTomatoesException {
        return getCurrentReleaseDvds(country, DEFAULT_PAGE, DEFAULT_PAGE_LIMIT);
    }

    /**
     * Retrieves current release DVDs
     *
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getCurrentReleaseDvds() throws RottenTomatoesException {
        return getCurrentReleaseDvds(DEFAULT_COUNTRY);
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

        WrapperLists wrapper = response.getResponse(WrapperLists.class, properties);
        if (wrapper != null && wrapper.getMovies() != null) {
            return wrapper.getMovies();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves new release DVDs
     *
     * @param country Provides localized data for the selected country
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getNewReleaseDvds(String country) throws RottenTomatoesException {
        return getNewReleaseDvds(country, DEFAULT_PAGE, DEFAULT_PAGE_LIMIT);
    }

    /**
     * Retrieves new release DVDs
     *
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getNewReleaseDvds() throws RottenTomatoesException {
        return getNewReleaseDvds(DEFAULT_COUNTRY);
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

        WrapperLists wrapper = response.getResponse(WrapperLists.class, properties);
        if (wrapper != null && wrapper.getMovies() != null) {
            return wrapper.getMovies();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves current release DVDs
     *
     * @param country Provides localized data for the selected country
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getUpcomingDvds(String country) throws RottenTomatoesException {
        return getUpcomingDvds(country, DEFAULT_PAGE, DEFAULT_PAGE_LIMIT);
    }

    /**
     * Retrieves current release DVDs
     *
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getUpcomingDvds() throws RottenTomatoesException {
        return getUpcomingDvds(DEFAULT_COUNTRY);
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
        properties.put(ApiBuilder.PROPERTY_ID, String.valueOf(movieId));
        properties.put(ApiBuilder.PROPERTY_URL, URL_MOVIES_INFO);

        return response.getResponse(RTMovie.class, properties);
    }

    /**
     * Pulls the complete movie cast for a movie
     *
     * @param movieId RT Movie ID
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTCast> getCastInfo(int movieId) throws RottenTomatoesException {
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_ID, String.valueOf(movieId));
        properties.put(ApiBuilder.PROPERTY_URL, URL_CAST_INFO);

        WrapperLists wrapper = response.getResponse(WrapperLists.class, properties);
        if (wrapper != null && wrapper.getCast() != null) {
            return wrapper.getCast();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Related movie clips and trailers for a movie
     *
     * @param movieId RT Movie ID
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTClip> getMovieClips(int movieId) throws RottenTomatoesException {
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_ID, String.valueOf(movieId));
        properties.put(ApiBuilder.PROPERTY_URL, URL_MOVIE_CLIPS);

        WrapperLists wrapper = response.getResponse(WrapperLists.class, properties);
        if (wrapper != null && wrapper.getClass() != null) {
            return wrapper.getClips();
        } else {
            return Collections.emptyList();
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
        properties.put(ApiBuilder.PROPERTY_ID, String.valueOf(movieId));
        properties.put(ApiBuilder.PROPERTY_URL, URL_MOVIES_REVIEWS);
        properties.put(ApiBuilder.PROPERTY_REVIEW_TYPE, reviewType);
        properties.put(ApiBuilder.PROPERTY_PAGE_LIMIT, ApiBuilder.validatePageLimit(pageLimit));
        properties.put(ApiBuilder.PROPERTY_PAGE, ApiBuilder.validatePage(page));
        properties.put(ApiBuilder.PROPERTY_COUNTRY, ApiBuilder.validateCountry(country));

        WrapperLists wrapper = response.getResponse(WrapperLists.class, properties);
        if (wrapper != null && wrapper.getReviews() != null) {
            return wrapper.getReviews();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves the reviews for a movie
     *
     * @param movieId
     * @param reviewType
     * @param country
     * @return
     * @throws RottenTomatoesException
     */
    public List<Review> getMoviesReviews(int movieId, String reviewType, String country) throws RottenTomatoesException {
        return getMoviesReviews(movieId, reviewType, DEFAULT_PAGE_LIMIT, DEFAULT_PAGE, country);
    }

    /**
     * Retrieves the reviews for a movie
     *
     * @param movieId
     * @param country
     * @return
     * @throws RottenTomatoesException
     */
    public List<Review> getMoviesReviews(int movieId, String country) throws RottenTomatoesException {
        return getMoviesReviews(movieId, DEFAULT_REVIEW, DEFAULT_PAGE_LIMIT, DEFAULT_PAGE, country);
    }

    /**
     * Retrieves the reviews for a movie
     *
     * @param movieId
     * @return
     * @throws RottenTomatoesException
     */
    public List<Review> getMoviesReviews(int movieId) throws RottenTomatoesException {
        return getMoviesReviews(movieId, DEFAULT_COUNTRY);
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
        properties.put(ApiBuilder.PROPERTY_ID, String.valueOf(movieId));
        properties.put(ApiBuilder.PROPERTY_URL, URL_MOVIES_SIMILAR);
        properties.put(ApiBuilder.PROPERTY_LIMIT, ApiBuilder.validateLimit(limit));

        WrapperLists wrapper = response.getResponse(WrapperLists.class, properties);
        if (wrapper != null && wrapper.getMovies() != null) {
            return wrapper.getMovies();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Returns similar movies to a movie
     *
     * @param movieId RT Movie ID
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getMoviesSimilar(int movieId) throws RottenTomatoesException {
        return getMoviesSimilar(movieId, DEFAULT_LIMIT);
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
            properties.put(ApiBuilder.PROPERTY_ID, altMovieId.substring(LENGTH_OF_IMDB_PREFIX));
        } else {
            properties.put(ApiBuilder.PROPERTY_ID, altMovieId);
        }
        properties.put(ApiBuilder.PROPERTY_TYPE, type);

        return response.getResponse(RTMovie.class, properties);
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
            properties.put(ApiBuilder.PROPERTY_QUERY, URLEncoder.encode(query, ENCODING_UTF8));
        } catch (UnsupportedEncodingException ex) {
            throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, ex);
        }

        WrapperLists wrapper = response.getResponse(WrapperLists.class, properties);
        if (wrapper != null && wrapper.getMovies() != null) {
            return wrapper.getMovies();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * The movies search endpoint for plain text queries. Let's you search for
     * movies!
     *
     * @param query
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getMoviesSearch(String query) throws RottenTomatoesException {
        return getMoviesSearch(query, DEFAULT_PAGE_LIMIT, DEFAULT_PAGE);
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

        WrapperLists wrapper = response.getResponse(WrapperLists.class, properties);
        if (wrapper != null && wrapper.getLinks() != null) {
            return wrapper.getLinks();
        } else {
            return Collections.emptyMap();
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

        WrapperLists wrapper = response.getResponse(WrapperLists.class, properties);
        if (wrapper != null && wrapper.getLinks() != null) {
            return wrapper.getLinks();
        } else {
            return Collections.emptyMap();
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

        WrapperLists wrapper = response.getResponse(WrapperLists.class, properties);
        if (wrapper != null && wrapper.getLinks() != null) {
            return wrapper.getLinks();
        } else {
            return Collections.emptyMap();
        }
    }

}
