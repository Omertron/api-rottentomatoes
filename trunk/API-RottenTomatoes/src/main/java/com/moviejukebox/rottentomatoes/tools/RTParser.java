package com.moviejukebox.rottentomatoes.tools;

import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.moviejukebox.rottentomatoes.model.Movie;

public class RTParser {
    private static Logger logger = Logger.getLogger("rottentomatoes");

    public static Movie processMovie(JSONObject jMovie) {
        Movie movie = new Movie();
        
        movie.setTitle(readString(jMovie, "title"));
        movie.setYear(readInt(jMovie, "year"));
        movie.setRuntime(readInt(jMovie, "runtime"));
        movie.setSynopsis(readString(jMovie, "synopsis"));
        
        // Release Dates
        // Ratings
        // Posters
        // Cast
        // Links
        
        return movie;
    }
    
    private static String readString(JSONObject jObject, String item) {
        String response = "";
        
        try {
            response = jObject.getString(item);
        } catch (JSONException error) {
            logger.fine("RTParse: Error reading " + item + " from movie");
            error.printStackTrace();
        }
        
        return response;
    }
    
    private static int readInt(JSONObject jObject, String item) {
        int response = 0;
        String jResponse = "";
        
        try {
            jResponse = jObject.getString(item);
            if (jResponse == null || ("").equals(jResponse)) {
                response = 0;
            } else {
                response = Integer.parseInt(jResponse);
            }
        } catch (JSONException error) {
            logger.fine("RTParse: Error reading " + item + " from movie");
            error.printStackTrace();
            response = 0;
        } catch (NumberFormatException error) {
            logger.fine("RTParse: Error reading " + item + " from movie");
            error.printStackTrace();
            response = 0;
        }
        
        return response;
    }
}
