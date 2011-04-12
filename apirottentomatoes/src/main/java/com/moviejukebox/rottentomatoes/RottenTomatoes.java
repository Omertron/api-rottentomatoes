/*
 *      Copyright (c) 2004-2011 YAMJ Members
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import com.moviejukebox.rottentomatoes.model.Link;
import com.moviejukebox.rottentomatoes.model.Movie;
import com.moviejukebox.rottentomatoes.tools.LogFormatter;
import com.moviejukebox.rottentomatoes.tools.RTParser;
import com.moviejukebox.rottentomatoes.tools.WebBrowser;

/**
 * Connect to the Rotten Tomatoes web site and get the rating for a specific movie
 * @author Stuart.Boston
 *
 */
@SuppressWarnings("unused")
public class RottenTomatoes {
    private String apiKey;
    private static final String API_SITE = "http://api.rottentomatoes.com/api/public/v1.0";

    private static Logger logger = Logger.getLogger("rottentomatoes");
    private static LogFormatter loggerFormatter = new LogFormatter();
    private static ConsoleHandler loggerConsoleHandler = new ConsoleHandler();
   
    private final static String URL_LISTS            = ".json?apiKey=";
    private final static String URL_MOVIE_SEARCH     = "/movies.json?apikey=";
    private final static String URL_LIST_DIR         = "/lists.json?apikey=";
    private final static String URL_MOVIE_LISTS      = "/lists/movies.json?apikey=";
    private final static String URL_DVD_LIST         = "/lists/dvds.json?apikey=";
    private final static String URL_OPENING_MOVIES   = "/lists/movies/opening.json?apikey=";
    private final static String URL_UPCOMING_MOVIES  = "/lists/movies/upcoming.json?apikey=";
    private final static String URL_NEW_RELEASE_DVDS = "/lists/dvds/new_releases.json?apikey=";
    private final static String URL_MOVIE_INFO       = "/movies/{movie-id}.json?apikey=";
    private final static String URL_MOVIE_CAST       = "/movies/{movie-id}/cast.json?apikey=";
    private final static String URL_MOVIE_REVIEWS    = "/movies/{movie-id}/reviews.json?apikey=";

    private final static String PREFIX_MOVIE        = "&q=";
    private final static String PREFIX_LIMIT        = "&limit=";
    private final static String PREFIX_PAGE_LIMIT   = "&page_limit=";
    
    private final static int RESULTS_DEFAULT        = 10;
    private final static int RESULTS_MAX            = 20;

    public RottenTomatoes(String apiKey) {
        setLogger(logger);
        setApiKey(apiKey);
    }

    private String buildUrl(String baseUrl) {
        return buildUrl(baseUrl, "", "", false);
    }
    
    private String buildUrl(String baseUrl, String prefix, String additional, boolean appendPageLimit) {
        StringBuffer url = new StringBuffer(API_SITE);
        url.append(baseUrl);
        url.append(apiKey);
        if (isValidString(additional)) {
            url.append(prefix);
            try {
                url.append(URLEncoder.encode(additional, "UTF-8"));
            } catch (UnsupportedEncodingException ignore) {
                // There's an issue with the encoding. so try the "raw" string
                url.append(additional);
            }
        }
        
        if (appendPageLimit) {
            url.append(PREFIX_PAGE_LIMIT);
            url.append(RESULTS_DEFAULT);
        }
        
        System.out.println("BuildUrl: " + url.toString());
        
        return url.toString();
    }
    
    /**
     * Get the lists from the base JSON API. These have no real use.
     * @return
     */
    public HashSet<Link> getLists() {
        return RTParser.parseLists(buildUrl(URL_LISTS), "links");
    }
    
    /**
     * Search for a specific movie.
     * If the movie is found, just return that one, otherwise return all
     * @param movieName
     * @return
     */
    public HashSet<Movie> moviesSearch(String movieName) {
        return RTParser.getMovies(buildUrl(URL_MOVIE_SEARCH, PREFIX_MOVIE, movieName, true));
    }
    
    public HashSet<Link> listsDirectory() {
        return RTParser.parseLists(buildUrl(URL_LIST_DIR), "links");
    }
    
    public HashSet<Link> movieListsDirectory() {
        return RTParser.parseLists(buildUrl(URL_MOVIE_LISTS), "links");
    }
    
    public HashSet<Link> dvdListsDirectory() {
        return RTParser.parseLists(buildUrl(URL_DVD_LIST), "links");
    }

    public HashSet<Movie> openingMovies() {
        return openingMovies(RESULTS_DEFAULT);
    }
    
    public HashSet<Movie> openingMovies(int limit) {
        int returnLimit = limit;
        if (returnLimit < 0) {
            returnLimit = RESULTS_DEFAULT;
        } else if (returnLimit > RESULTS_MAX) {
            returnLimit = RESULTS_MAX;
        }
        
        String url = buildUrl(URL_OPENING_MOVIES, PREFIX_LIMIT, "" + returnLimit, false);
        return RTParser.getMovies(url);
    }
    
    public HashSet<Movie> upcomingMovies(int limit) {
        int returnLimit = limit;
        if (returnLimit < 0) {
            returnLimit = RESULTS_DEFAULT;
        } else if (returnLimit > RESULTS_MAX) {
            returnLimit = RESULTS_MAX;
        }
        
        String url = buildUrl(URL_UPCOMING_MOVIES, PREFIX_LIMIT, "" + returnLimit, false);
        return RTParser.getMovies(url);
    }
    
    public String newReleaseDvds() {
        return null;
    }
    
    public String movieInfo() {
        return null;
    }
    
    public String movieCast() {
        return null;
    }
    
    public String movieReviews() {
        return null;
    }
    
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
        loggerFormatter.addApiKey(apiKey);
    }

    public void setLogger(Logger logger) {
        if (logger == null) {
            return;
        }

        RottenTomatoes.logger = logger;
        loggerConsoleHandler.setFormatter(loggerFormatter);
        loggerConsoleHandler.setLevel(Level.FINE);
        logger.addHandler(loggerConsoleHandler);
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
    }

    /**
     * Set the web browser proxy information
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
     * @param webTimeoutConnect
     * @param webTimeoutRead
     */
    public void setTimeout(int webTimeoutConnect, int webTimeoutRead) {
        WebBrowser.setWebTimeoutConnect(webTimeoutConnect);
        WebBrowser.setWebTimeoutRead(webTimeoutRead);
    }

    /**
     * Check the string passed to see if it is invalid.
     * Invalid strings are null or blank
     * @param testString The string to test
     * @return True if the string is invalid, false otherwise
     */
    public static boolean isNotValidString(String testString) {
        return !isValidString(testString);
    }

    /**
     * Check the string passed to see if it contains a value.
     * @param testString The string to test
     * @return False if the string is empty or null, True otherwise
     */
    public static boolean isValidString(String testString) {
        // Checks if a String is whitespace, empty ("") or null.
        if (StringUtils.isBlank(testString)) {
            return false;
        }
        
        return true;
    }

}
