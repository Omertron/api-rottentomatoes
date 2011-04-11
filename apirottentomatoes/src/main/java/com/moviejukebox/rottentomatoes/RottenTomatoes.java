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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    // Only get 10 movies per search
    private final static int    RESULTS_PER_PAGE     = 10;
    private final static String URL_PAGE_LIMIT       = "&page_limit=" + RESULTS_PER_PAGE;
   
    private final static String URL_LISTS            = ".json?apiKey=";
    private final static String URL_MOVIE_SEARCH     = "/movies.json?apikey=";
    private final static String URL_LIST_DIR         = "/lists.json?apikey=";
    private final static String URL_MOVIE_LIST       = "/lists/movies.json?apikey=";
    private final static String URL_DVD_LIST         = "/lists/dvds.json?apikey=";
    private final static String URL_OPENING_MOVIES   = "/lists/movies/opening.json?apikey=";
    private final static String URL_UPCOMING_MOVIES  = "/lists/movies/upcoming.json?apikey=";
    private final static String URL_NEW_RELEASE_DVDS = "/lists/dvds/new_releases.json?apikey=";
    private final static String URL_MOVIE_INFO       = "/movies/{movie-id}.json?apikey=";
    private final static String URL_MOVIE_CAST       = "/movies/{movie-id}/cast.json?apikey=";
    private final static String URL_MOVIE_REVIEWS    = "/movies/{movie-id}/reviews.json?apikey=";
    
    public RottenTomatoes(String apiKey) {
        setLogger(logger);
        setApiKey(apiKey);
    }

    private String buildUrl(String baseUrl) {
        return buildUrl(baseUrl, "");
    }
    
    private String buildUrl(String baseUrl, String additional) {
        StringBuffer url = new StringBuffer(API_SITE);
        url.append(baseUrl);
        url.append(apiKey);
        if (additional != null && !("").equals(additional)) {
            url.append("&q=");
            try {
                url.append(URLEncoder.encode(additional, "UTF-8"));
            } catch (UnsupportedEncodingException ignore) {
                // There's an issue with the encoding. so try the "raw" string
                url.append(additional);
            }
            url.append(URL_PAGE_LIMIT);
        }
        
        logger.fine("BuildUrl: " + url.toString());
        
        return url.toString();
    }
    
    /**
     * Get the lists from the base JSON API. These have no real use.
     * @return
     */
    public HashSet<Link> getLists() {
        String url = buildUrl(URL_LISTS);
        JSONObject objMaster;
        
        HashSet<Link> response = new HashSet<Link>();
        
        try {
            objMaster = new JSONObject(WebBrowser.request(url));

            objMaster = objMaster.getJSONObject("links");

            response.add(new Link("lists", objMaster.get("lists").toString()));
            response.add(new Link("movies", objMaster.get("movies").toString()));
        } catch (JSONException error) {
            error.printStackTrace();
        } catch (IOException error) {
            error.printStackTrace();
        }
        
        return response;
    }
    
    public HashSet<Movie> moviesSearch(String movieName) {
        String url = buildUrl(URL_MOVIE_SEARCH, movieName);
        String response = "";
        JSONObject movieObject = new JSONObject();
        
        try {
            response = WebBrowser.request(url);
            movieObject = new JSONObject(response);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        HashSet<Movie> movies = new HashSet<Movie>();

        if (movieObject != null) {
            try {
                logger.fine("Number of movies: " + movieObject.get("total"));
                JSONArray jsonMovies = movieObject.getJSONArray("movies");
                
                for (int loop = 0 ; loop < jsonMovies.length() ; ++loop) {
                    JSONObject singleMovie = jsonMovies.getJSONObject(loop);
                    Movie movie = RTParser.processMovie(singleMovie);
                    movies.add(movie);
                    logger.fine("Movie #" + loop + " - " + movie.getTitle());
                }
                
                
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            return movies;
        } else {
            return null;
        }
    }
    
    public String listsDirectory() {
        return null;
    }
    
    public String movieListsDirectory() {
        return null;
    }
    
    public String dvdListsDirectory() {
        return null;
    }

    public String openingMovies() {
        return null;
    }
    
    public String upcomingMovies() {
        return null;
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
}
