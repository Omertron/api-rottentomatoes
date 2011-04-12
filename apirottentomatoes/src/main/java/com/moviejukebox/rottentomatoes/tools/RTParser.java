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
package com.moviejukebox.rottentomatoes.tools;

import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Logger;

import org.xerial.json.JSONArray;
import org.xerial.json.JSONErrorCode;
import org.xerial.json.JSONException;
import org.xerial.json.JSONObject;
import org.xerial.json.JSONValue;

import com.moviejukebox.rottentomatoes.model.Cast;
import com.moviejukebox.rottentomatoes.model.Link;
import com.moviejukebox.rottentomatoes.model.Movie;
import com.moviejukebox.rottentomatoes.model.Rating;
import com.moviejukebox.rottentomatoes.model.ReleaseDate;

public class RTParser {
    private static Logger logger = Logger.getLogger("rottentomatoes");

    /**
     * Parse a JSON object for all the movie information
     * @param jMovie
     * @return
     */
    public static Movie parseMovie(JSONObject jMovie) {
        Movie movie = new Movie();

        movie.setId(readInt(jMovie, "id"));
        movie.setTitle(readString(jMovie, "title"));
        movie.setYear(readInt(jMovie, "year"));
        movie.setRuntime(readInt(jMovie, "runtime"));
        movie.setSynopsis(readString(jMovie, "synopsis"));
        movie.setReleaseDates(parseReleaseDates(jMovie));
        movie.setRatings(parseRatings(jMovie));
        movie.setLinks(parseGenericLinks(jMovie, "posters"));
        movie.setCast(parseCast(jMovie));
        movie.setLinks(parseGenericLinks(jMovie, "links"));
        // Try and get the movie id from the link data
        if (movie.getId() <= 0) {
            movie.setId(getIdFromLinks(movie.getLinks()));
        }
        movie.setDirectors(parseDirectors(jMovie));
        movie.setArtwork(parseGenericLinks(jMovie, "posters"));
        return movie;
    }
    
    /**
     * Get the movie-id from the "self" link if it exists
     * @param links
     * @return
     */
    private static int getIdFromLinks(HashSet<Link> links) {
        for (Link link : links) {
            if ("self".equalsIgnoreCase(link.getLinkType())) {
                String linkText = link.getLinkUrl();
                int start = linkText.indexOf("/movies/");
                if (start > 0) {
                    start += "/movies/".length();
                    int end = linkText.indexOf(".json", start);
                    if (start < end) {
                        try {
                            return Integer.parseInt(new String(linkText.substring(start, end)));
                        } catch (Exception error) {
                            return 0;
                        }
                    }
                }
            }
        }
        
        return 0;
    }

    /**
     * Parse the movie JSON object for Cast information
     * @param jMovie
     * @return
     */
    private static HashSet<Cast> parseCast(JSONObject jMovie) {
        HashSet<Cast> response = new HashSet<Cast>();
        
        JSONArray jsonCast = jMovie.getJSONArray("abridged_cast");
        
        for (int loop = 0 ; loop < jsonCast.size() ; ++loop) {
            JSONObject jCast = jsonCast.getJSONObject(loop);
            Cast cast = new Cast();
            cast.setCastName(readString(jCast, "name"));

            // Sometimes cast doesn't have a character.
            if (jCast.elementSize() > 1) {
                for (JSONValue character : jCast.getJSONArray("characters")) {
                    cast.addCharacter(character.toString());
                }
            }
            
            response.add(cast);
        }
        
        return response;
    }

    /**
     * Parse the movie JSON object for the director names
     * @param jMovie
     * @return
     */
    private static HashSet<String> parseDirectors(JSONObject jMovie) {
        HashSet<String> response = new HashSet<String>();
        
        JSONArray jsonCast = jMovie.getJSONArray("abridged_cast");
        
        for (int loop = 0 ; loop < jsonCast.size() ; ++loop) {
            JSONObject jCast = jsonCast.getJSONObject(loop);
            response.add(readString(jCast, "name"));
        }
        
        return response;
    }
    
    /**
     * Parse the movie JSON object for the release dates.
     * The whole object is passed in so we can trap the JSON Exceptions
     * @param jMovie
     * @return
     */
    private static HashSet<ReleaseDate> parseReleaseDates(JSONObject jMovie) {
        HashSet<ReleaseDate> response = new HashSet<ReleaseDate>();

        JSONObject jObject;
        try {
            jObject = jMovie.getJSONObject("release_dates");
        } catch (JSONException e) {
            logger.fine("No release dates for the movie");
            return null;
        }
        
        for (String type : jObject.getKeyValueMap().keySet()) {
            ReleaseDate rd = new ReleaseDate();
            rd.setReleaseType(type);
            rd.setReleaseDate(readString(jObject, type));
            response.add(rd);
        }
        return response;
    }
    
    /**
     * Parse the movie JSON object for the ratings.
     * The whole object is passed in so we can trap the JSON Exceptions
     * @param jMovie
     * @return
     */
    private static HashSet<Rating> parseRatings(JSONObject jMovie) {
        HashSet<Rating> response = new HashSet<Rating>();

        JSONObject jObject;
        try {
            jObject = jMovie.getJSONObject("ratings");
        } catch (JSONException e) {
            logger.fine("No ratings for the movie");
            return null;
        }
        
        for (String type : jObject.getKeyValueMap().keySet()) {
            Rating rating = new Rating();
            rating.setRatingType(type);
            rating.setRatingScore(readInt(jObject, type));
            response.add(rating);
        }
        
        return response;
    }
    
    /**
     * Parse the movie JSON object for a link object
     * @param jMovie
     * @param linkType
     * @return
     */
    public static HashSet<Link> parseGenericLinks(JSONObject jMovie, String linkType) {
        HashSet<Link> response = new HashSet<Link>();

        JSONObject jObject;
        try {
            jObject = jMovie.getJSONObject(linkType);
        } catch (JSONException e) {
            logger.fine("No " + linkType + " in the object");
            return null;
        }
        
        for (String type : jObject.getKeyValueMap().keySet()) {
            Link link = new Link();
            link.setLinkType(type);
            link.setLinkUrl(readString(jObject, type));
            response.add(link);
        }
        
        return response;
    }
    
    /**
     * Get a string from a JSON object
     * @param jObject
     * @param item
     * @return
     */
    private static String readString(JSONObject jObject, String item) {
        String response = "";
        
        try {
            response = jObject.getString(item);
        } catch (JSONException error) {
            if (error.getErrorCode() == JSONErrorCode.KeyIsNotFound) {
                // This is fine, return back the empty string
                return "";
            } else {
                logger.fine("RTParse: Error reading " + item + " from movie");
                error.printStackTrace();
            }
        }
        
        return response;
    }
    
    /**
     * Get an int from a JSON Object
     * @param jObject
     * @param item
     * @return
     */
    private static int readInt(JSONObject jObject, String item) {
        int response = 0;
        
        try {
            response = jObject.getInt(item);
            return response;
        } catch (JSONException ignore) {
            // We can't read this as an integer, so it's probably null or an empty string
            return 0;
        }
    }
    
    /**
     * Retrieve the link list from the url
     * @param listUrl
     * @param listType
     * @return
     */
    public static HashSet<Link> parseLists(String listUrl, String listType) {
        JSONObject objMaster;
        HashSet<Link> response = new HashSet<Link>();
        
        try {
            objMaster = new JSONObject(WebBrowser.request(listUrl));
            response = RTParser.parseGenericLinks(objMaster, listType);
        } catch (JSONException error) {
            error.printStackTrace();
        } catch (IOException error) {
            error.printStackTrace();
        }
        
        return response;
    }

    public static HashSet<Movie> getMovies(String searchUrl) {
        JSONObject movieObject = new JSONObject();
        HashSet<Movie> movies = new HashSet<Movie>();
        
        try {
            String response = WebBrowser.request(searchUrl);
            System.out.println("Response size: " + response.length());
            movieObject = new JSONObject(response);
        } catch (IOException e) {
            e.printStackTrace();
            return movies;
        } catch (JSONException e) {
            if (e.getErrorCode() == JSONErrorCode.InvalidJSONData) {
                System.out.println("Error reading the data");
            }
            e.printStackTrace();
            return movies;
        }

        if (movieObject.elementSize() > 0) {
            JSONArray jsonMovies = movieObject.getJSONArray("movies");
            logger.fine("Number of movies: " + jsonMovies.size());
            
            for (int loop = 0 ; loop < jsonMovies.size() ; loop++) {
                Movie movie = RTParser.parseMovie(jsonMovies.getJSONObject(loop));
                
                System.out.println("Movie #" + (loop) + " - " + movie.getTitle() + " (" + movie.getId() + ")");
                movies.add(movie);
            }
            
            return movies;
        } else {
            return movies;
        }
    }
    
}
