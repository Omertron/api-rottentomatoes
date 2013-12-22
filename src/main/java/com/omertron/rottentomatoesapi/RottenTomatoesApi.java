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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omertron.rottentomatoesapi.RottenTomatoesException.RottenTomatoesExceptionType;
import com.omertron.rottentomatoesapi.model.RTCast;
import com.omertron.rottentomatoesapi.model.RTClip;
import com.omertron.rottentomatoesapi.model.RTMovie;
import com.omertron.rottentomatoesapi.model.Review;
import com.omertron.rottentomatoesapi.tools.ApiBuilder;
import com.omertron.rottentomatoesapi.tools.RequestThrottler;
import com.omertron.rottentomatoesapi.wrapper.WrapperLists;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yamj.api.common.http.CommonHttpClient;
import org.yamj.api.common.http.DefaultPoolingHttpClient;

/**
 * Connect to the Rotten Tomatoes web site and get the rating for a specific movie
 *
 * @author Stuart.Boston
 *
 */
public class RottenTomatoesApi {

    private static final Logger LOG = LoggerFactory.getLogger(RottenTomatoesApi.class);
    private CommonHttpClient httpClient;
    private static final String ENCODING_UTF8 = "UTF-8";
    /*
     * Throttler
     */
    RequestThrottler throttler;
    private static final int THROTTLE_RATE = 5;
    private static final long THROTTLE_PERIOD = 1000;
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
    private static final String URL_CAST_INFO = "/movies/" + ApiBuilder.MOVIE_ID + "/cast";
    private static final String URL_MOVIE_CLIPS = "/movies/" + ApiBuilder.MOVIE_ID + "/clips";
    private static final String URL_MOVIES_REVIEWS = "/movies/" + ApiBuilder.MOVIE_ID + "/reviews";
    private static final String URL_MOVIES_SIMILAR = "/movies/" + ApiBuilder.MOVIE_ID + "/similar";
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
    private static final ObjectMapper MAPPER = new ObjectMapper();
    /*
     * Defaults
     */
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_LIMIT = 0;
    private static final int DEFAULT_LIMIT = 0;
    private static final String DEFAULT_COUNTRY = "";
    private static final String DEFAULT_REVIEW = "";

    public RottenTomatoesApi(String apiKey) throws RottenTomatoesException {
        this(apiKey, new DefaultPoolingHttpClient());
    }

    public RottenTomatoesApi(String apiKey, CommonHttpClient httpClient) throws RottenTomatoesException {
        if (StringUtils.isBlank(apiKey)) {
            throw new RottenTomatoesException(RottenTomatoesExceptionType.NO_API_KEY, "No API Key provided!");
        }

        ApiBuilder.addApiKey(apiKey);
        this.httpClient = httpClient;
        this.throttler = new RequestThrottler(THROTTLE_RATE, THROTTLE_PERIOD);
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
     * Displays top box office earning movies, sorted by most recent weekend gross ticket sales.
     *
     * @param limit Limits the number of movies returned
     * @param country Provides localized data for the selected country
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getBoxOffice(String country, int limit) throws RottenTomatoesException {
        throttler.startRequest();
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_BOX_OFFICE);
        properties.put(ApiBuilder.PROPERTY_LIMIT, ApiBuilder.validateLimit(limit));
        properties.put(ApiBuilder.PROPERTY_COUNTRY, ApiBuilder.validateCountry(country));

        try {
            String webPage = getContent(ApiBuilder.create(properties));
            WrapperLists wl = MAPPER.readValue(webPage, WrapperLists.class);
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
     * Displays top box office earning movies, sorted by most recent weekend gross ticket sales.
     *
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getBoxOffice() throws RottenTomatoesException {
        throttler.startRequest();
        return getBoxOffice(DEFAULT_COUNTRY, DEFAULT_LIMIT);
    }

    /**
     * Displays top box office earning movies, sorted by most recent weekend gross ticket sales.
     *
     * @param country Provides localized data for the selected country
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getBoxOffice(String country) throws RottenTomatoesException {
        throttler.startRequest();
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
        throttler.startRequest();
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_IN_THEATERS);
        properties.put(ApiBuilder.PROPERTY_COUNTRY, ApiBuilder.validateCountry(country));
        properties.put(ApiBuilder.PROPERTY_PAGE, ApiBuilder.validatePage(page));
        properties.put(ApiBuilder.PROPERTY_PAGE_LIMIT, ApiBuilder.validatePageLimit(pageLimit));

        try {
            String webPage = getContent(ApiBuilder.create(properties));
            WrapperLists wl = MAPPER.readValue(webPage, WrapperLists.class);
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
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getInTheaters(String country) throws RottenTomatoesException {
        throttler.startRequest();
        return getInTheaters(country, DEFAULT_PAGE, DEFAULT_PAGE_LIMIT);
    }

    /**
     * Retrieves movies currently in theaters
     *
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getInTheaters() throws RottenTomatoesException {
        throttler.startRequest();
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
        throttler.startRequest();
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_OPENING_MOVIES);
        properties.put(ApiBuilder.PROPERTY_LIMIT, ApiBuilder.validateLimit(limit));
        properties.put(ApiBuilder.PROPERTY_COUNTRY, ApiBuilder.validateCountry(country));

        try {
            String webPage = getContent(ApiBuilder.create(properties));
            WrapperLists wl = MAPPER.readValue(webPage, WrapperLists.class);
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
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getOpeningMovies(String country) throws RottenTomatoesException {
        throttler.startRequest();
        return getOpeningMovies(country, DEFAULT_LIMIT);
    }

    /**
     * Retrieves current opening movies
     *
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getOpeningMovies() throws RottenTomatoesException {
        throttler.startRequest();
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
        throttler.startRequest();
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_UPCOMING_MOVIES);
        properties.put(ApiBuilder.PROPERTY_COUNTRY, ApiBuilder.validateCountry(country));
        properties.put(ApiBuilder.PROPERTY_PAGE, ApiBuilder.validatePage(page));
        properties.put(ApiBuilder.PROPERTY_PAGE_LIMIT, ApiBuilder.validatePageLimit(pageLimit));

        try {
            String webPage = getContent(ApiBuilder.create(properties));
            WrapperLists wl = MAPPER.readValue(webPage, WrapperLists.class);
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
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getUpcomingMovies(String country) throws RottenTomatoesException {
        throttler.startRequest();
        return getUpcomingMovies(country, DEFAULT_PAGE, DEFAULT_PAGE_LIMIT);
    }

    /**
     * Retrieves upcoming movies
     *
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getUpcomingMovies() throws RottenTomatoesException {
        throttler.startRequest();
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
        throttler.startRequest();
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_TOP_RENTALS);
        properties.put(ApiBuilder.PROPERTY_LIMIT, ApiBuilder.validateLimit(limit));
        properties.put(ApiBuilder.PROPERTY_COUNTRY, ApiBuilder.validateCountry(country));

        try {
            String webPage = getContent(ApiBuilder.create(properties));
            WrapperLists wl = MAPPER.readValue(webPage, WrapperLists.class);
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
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getTopRentals(String country) throws RottenTomatoesException {
        throttler.startRequest();
        return getTopRentals(DEFAULT_COUNTRY, DEFAULT_LIMIT);
    }

    /**
     * Retrieves the current top DVD rentals
     *
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getTopRentals() throws RottenTomatoesException {
        throttler.startRequest();
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
        throttler.startRequest();
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_CURRENT_RELEASE_DVDS);
        properties.put(ApiBuilder.PROPERTY_COUNTRY, ApiBuilder.validateCountry(country));
        properties.put(ApiBuilder.PROPERTY_PAGE, ApiBuilder.validatePage(page));
        properties.put(ApiBuilder.PROPERTY_PAGE_LIMIT, ApiBuilder.validatePageLimit(pageLimit));

        try {
            String webPage = getContent(ApiBuilder.create(properties));
            WrapperLists wl = MAPPER.readValue(webPage, WrapperLists.class);
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
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getCurrentReleaseDvds(String country) throws RottenTomatoesException {
        throttler.startRequest();
        return getCurrentReleaseDvds(DEFAULT_COUNTRY, DEFAULT_PAGE, DEFAULT_PAGE_LIMIT);
    }

    /**
     * Retrieves current release DVDs
     *
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getCurrentReleaseDvds() throws RottenTomatoesException {
        throttler.startRequest();
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
        throttler.startRequest();
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_NEW_RELEASE_DVDS);
        properties.put(ApiBuilder.PROPERTY_COUNTRY, ApiBuilder.validateCountry(country));
        properties.put(ApiBuilder.PROPERTY_PAGE, ApiBuilder.validatePage(page));
        properties.put(ApiBuilder.PROPERTY_PAGE_LIMIT, ApiBuilder.validatePageLimit(pageLimit));

        try {
            String webPage = getContent(ApiBuilder.create(properties));
            WrapperLists wl = MAPPER.readValue(webPage, WrapperLists.class);
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
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getNewReleaseDvds(String country) throws RottenTomatoesException {
        throttler.startRequest();
        return getNewReleaseDvds(DEFAULT_COUNTRY, DEFAULT_PAGE, DEFAULT_PAGE_LIMIT);
    }

    /**
     * Retrieves new release DVDs
     *
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getNewReleaseDvds() throws RottenTomatoesException {
        throttler.startRequest();
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
        throttler.startRequest();
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_UPCOMING_DVDS);
        properties.put(ApiBuilder.PROPERTY_COUNTRY, ApiBuilder.validateCountry(country));
        properties.put(ApiBuilder.PROPERTY_PAGE, ApiBuilder.validatePage(page));
        properties.put(ApiBuilder.PROPERTY_PAGE_LIMIT, ApiBuilder.validatePageLimit(pageLimit));

        try {
            String webPage = getContent(ApiBuilder.create(properties));
            WrapperLists wl = MAPPER.readValue(webPage, WrapperLists.class);
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
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getUpcomingDvds(String country) throws RottenTomatoesException {
        throttler.startRequest();
        return getUpcomingDvds(country, DEFAULT_PAGE, DEFAULT_PAGE_LIMIT);
    }

    /**
     * Retrieves current release DVDs
     *
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getUpcomingDvds() throws RottenTomatoesException {
        throttler.startRequest();
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
        throttler.startRequest();
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_ID, String.valueOf(movieId));
        properties.put(ApiBuilder.PROPERTY_URL, URL_MOVIES_INFO);

        try {
            String webPage = getContent(ApiBuilder.create(properties));
            RTMovie rtMovie = MAPPER.readValue(webPage, RTMovie.class);
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
    public List<RTCast> getCastInfo(int movieId) throws RottenTomatoesException {
        throttler.startRequest();
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_ID, String.valueOf(movieId));
        properties.put(ApiBuilder.PROPERTY_URL, URL_CAST_INFO);

        try {
            String webPage = getContent(ApiBuilder.create(properties));
            WrapperLists wl = MAPPER.readValue(webPage, WrapperLists.class);

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
    public List<RTClip> getMovieClips(int movieId) throws RottenTomatoesException {
        throttler.startRequest();
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_ID, String.valueOf(movieId));
        properties.put(ApiBuilder.PROPERTY_URL, URL_MOVIE_CLIPS);

        try {
            String webPage = getContent(ApiBuilder.create(properties));
            WrapperLists wl = MAPPER.readValue(webPage, WrapperLists.class);

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
        throttler.startRequest();
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_ID, String.valueOf(movieId));
        properties.put(ApiBuilder.PROPERTY_URL, URL_MOVIES_REVIEWS);
        properties.put(ApiBuilder.PROPERTY_REVIEW_TYPE, reviewType);
        properties.put(ApiBuilder.PROPERTY_PAGE_LIMIT, ApiBuilder.validatePageLimit(pageLimit));
        properties.put(ApiBuilder.PROPERTY_PAGE, ApiBuilder.validatePage(page));
        properties.put(ApiBuilder.PROPERTY_COUNTRY, ApiBuilder.validateCountry(country));

        try {
            String webPage = getContent(ApiBuilder.create(properties));
            WrapperLists wl = MAPPER.readValue(webPage, WrapperLists.class);

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
     * Retrieves the reviews for a movie
     *
     * @param movieId
     * @param reviewType
     * @param country
     * @return
     * @throws RottenTomatoesException
     */
    public List<Review> getMoviesReviews(int movieId, String reviewType, String country) throws RottenTomatoesException {
        throttler.startRequest();
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
        throttler.startRequest();
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
        throttler.startRequest();
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
        throttler.startRequest();
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_ID, String.valueOf(movieId));
        properties.put(ApiBuilder.PROPERTY_URL, URL_MOVIES_SIMILAR);
        properties.put(ApiBuilder.PROPERTY_LIMIT, ApiBuilder.validateLimit(limit));

        try {
            String webPage = getContent(ApiBuilder.create(properties));
            WrapperLists wl = MAPPER.readValue(webPage, WrapperLists.class);

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
     * Returns similar movies to a movie
     *
     * @param movieId RT Movie ID
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getMoviesSimilar(int movieId) throws RottenTomatoesException {
        throttler.startRequest();
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
        throttler.startRequest();
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_MOVIES_ALIAS);
        // remove the "tt" from the start of the ID if it's imdb
        if ("imdb".equalsIgnoreCase(type) && altMovieId.toLowerCase().startsWith("tt")) {
            properties.put(ApiBuilder.PROPERTY_ID, altMovieId.substring(2));
        } else {
            properties.put(ApiBuilder.PROPERTY_ID, altMovieId);
        }
        properties.put(ApiBuilder.PROPERTY_TYPE, type);

        try {
            String webPage = getContent(ApiBuilder.create(properties));
            RTMovie rtMovie = MAPPER.readValue(webPage, RTMovie.class);
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
     * The movies search endpoint for plain text queries. Let's you search for movies!
     *
     * @param query
     * @param pageLimit
     * @param page
     * @return
     * @throws RottenTomatoesException
     */
    public List<RTMovie> getMoviesSearch(String query, int pageLimit, int page) throws RottenTomatoesException {
        throttler.startRequest();
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_MOVIES_SEARCH);
        properties.put(ApiBuilder.PROPERTY_PAGE_LIMIT, ApiBuilder.validatePageLimit(pageLimit));
        properties.put(ApiBuilder.PROPERTY_PAGE, ApiBuilder.validatePage(page));

        try {
            properties.put(ApiBuilder.PROPERTY_QUERY, URLEncoder.encode(query, ENCODING_UTF8));
            String webPage = getContent(ApiBuilder.create(properties));
            WrapperLists wl = MAPPER.readValue(webPage, WrapperLists.class);

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

    public List<RTMovie> getMoviesSearch(String query) throws RottenTomatoesException {
        throttler.startRequest();
        return getMoviesSearch(query, DEFAULT_PAGE_LIMIT, DEFAULT_PAGE);
    }

    /**
     * Displays the top level lists available in the API
     *
     * @return
     * @throws RottenTomatoesException
     */
    public Map<String, String> getListsDirectory() throws RottenTomatoesException {
        throttler.startRequest();
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_LISTS_DIRECTORY);

        try {
            String webPage = getContent(ApiBuilder.create(properties));
            WrapperLists wl = MAPPER.readValue(webPage, WrapperLists.class);

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
        throttler.startRequest();
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_MOVIE_LISTS);
        try {
            String webPage = getContent(ApiBuilder.create(properties));
            WrapperLists wl = MAPPER.readValue(webPage, WrapperLists.class);

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
        throttler.startRequest();
        properties.clear();
        properties.put(ApiBuilder.PROPERTY_URL, URL_DVD_LISTS);

        try {
            String webPage = getContent(ApiBuilder.create(properties));
            WrapperLists wl = MAPPER.readValue(webPage, WrapperLists.class);

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
     * Get the content from a string, decoding it if it is in GZIP format
     *
     * @param urlString
     * @return
     * @throws RottenTomatoesException
     */
    private String getContent(String urlString) throws RottenTomatoesException {
        StringBuilder content = new StringBuilder();
        GZIPInputStream gzis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try {
            HttpEntity entity = httpClient.requestResource(urlString);

            if (entity.getContentEncoding() != null && entity.getContentEncoding().getValue().equalsIgnoreCase("gzip")) {
                gzis = new GZIPInputStream(entity.getContent());
                isr = new InputStreamReader(gzis, ENCODING_UTF8);
                br = new BufferedReader(isr);

                String readed = br.readLine();
                while (readed != null) {
                    content.append(readed);
                    readed = br.readLine();
                }
            } else {
                content.append(EntityUtils.toString(entity, Charset.forName(ENCODING_UTF8)));
            }
        } catch (IOException ex) {
            throw new RottenTomatoesException(RottenTomatoesExceptionType.MAPPING_FAILED, "Failed to read JSON data from " + urlString, ex);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    LOG.trace("Failed to close BufferedReader", ex);
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException ex) {
                    LOG.trace("Failed to close InputStreamReader", ex);
                }
            }
            if (gzis != null) {
                try {
                    gzis.close();
                } catch (IOException ex) {
                    LOG.trace("Failed to close GZIPInputStream", ex);
                }
            }
        }
        return content.toString();
    }
}
