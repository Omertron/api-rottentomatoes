package com.moviejukebox.rottentomatoes.tools;

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

    public static Movie parseMovie(JSONObject jMovie) {
        Movie movie = new Movie();

        movie.setTitle(readString(jMovie, "title"));
        movie.setYear(readInt(jMovie, "year"));
        movie.setRuntime(readInt(jMovie, "runtime"));
        movie.setSynopsis(readString(jMovie, "synopsis"));
        movie.setReleaseDates(parseReleaseDates(jMovie));
        movie.setRatings(parseRatings(jMovie));
        movie.setLinks(parseGenericLinks(jMovie, "posters"));
        movie.setCast(parseCast(jMovie));
        movie.setLinks(parseGenericLinks(jMovie, "links"));
        movie.setDirectors(parseDirectors(jMovie));
        movie.setArtwork(parseGenericLinks(jMovie, "posters"));
        return movie;
    }
    
    public static HashSet<Cast> parseCast(JSONObject jMovie) {
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

    public static HashSet<String> parseDirectors(JSONObject jMovie) {
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
    public static HashSet<ReleaseDate> parseReleaseDates(JSONObject jMovie) {
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
    public static HashSet<Rating> parseRatings(JSONObject jMovie) {
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
    
    public static HashSet<Link> parseGenericLinks(JSONObject jMovie, String linkType) {
        HashSet<Link> response = new HashSet<Link>();

        JSONObject jObject;
        try {
            jObject = jMovie.getJSONObject(linkType);
        } catch (JSONException e) {
            logger.fine("No " + linkType + " for the movie");
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
    
}
