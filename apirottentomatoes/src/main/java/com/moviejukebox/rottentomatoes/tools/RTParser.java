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
package com.moviejukebox.rottentomatoes.tools;

import com.moviejukebox.rottentomatoes.model.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.ParseException;
import java.util.*;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RTParser {
    private static Logger logger = Logger.getLogger(RTParser.class);

    private static final String CAST_ABRIDGED = "abridged_cast";
    private static final String CAST_FULL = "cast";
    private static final String CHARACTERS = "characters";
    private static final String DIRECTORS_ABDRIDGED = "abridged_directors";
    private static final String GENRES = "genres";
    private static final String ID = "id";
    private static final String LINKS = "links";
    private static final String MOVIES = "movies";
    private static final String MPAA_RATING = "mpaa_rating";
    private static final String NAME = "name";
    private static final String POSTERS = "posters";
    private static final String RATINGS = "ratings";
    private static final String RELEASE_DATES = "release_dates";
    private static final String RUNTIME = "runtime";
    private static final String SYNOPSIS = "synopsis";
    private static final String TITLE = "title";
    private static final String YEAR = "year";
    private static final String CRITIC = "critic";
    private static final String DATE = "date";
    private static final String PUBLICATION = "publication";
    private static final String QUOTE = "quote";
    private static final String REVIEWS = "reviews";

    // Hide the constructor
    protected RTParser() {
        // prevents calls from subclass
        throw new UnsupportedOperationException();
    }

    /**
     * Get multiple movies and process them
     * @param searchUrl
     * @return
     */
    public static Set<Movie> getMovies(String searchUrl) throws ParseException {
        JSONObject movieObject;
        Set<Movie> movies = new HashSet<Movie>();

        try {
            String response = WebBrowser.request(searchUrl);
            movieObject = new JSONObject(response);
        } catch (IOException error) {
            logger.warn("RottenTomatoesAPI (getMovies): " + error.getMessage());
            final Writer eResult = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(eResult);
            error.printStackTrace(printWriter);
            logger.warn(eResult.toString());
            return movies;
        } catch (JSONException error) {
            logger.warn("RottenTomatoesAPI (getMovies): " + error.getMessage());
            final Writer eResult = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(eResult);
            error.printStackTrace(printWriter);
            logger.warn(eResult.toString());
            return movies;
        }

        try {
            if (movieObject.length() > 0) {
                JSONArray jsonMovies = movieObject.getJSONArray(MOVIES);

                for (int loop = 0 ; loop < jsonMovies.length() ; loop++) {
                    movies.add(parseMovie(jsonMovies.getJSONObject(loop)));
                }

                return movies;
            } else {
                return movies;
            }
        } catch (JSONException error) {
            throw new ParseException("RottenTomatoesAPI: Error getting list of movies - " + error.getMessage(), 0);
        }
    }

    /**
     * Process a single movie
     * @param searchUrl
     * @return
     */
    public static Movie getSingleMovie(String searchUrl) {
        JSONObject movieObject;
        Movie movie;

        try {
            String response = WebBrowser.request(searchUrl);
            movieObject = new JSONObject(response);

            movie = parseMovie(movieObject);

            return movie;
        } catch (IOException error) {
            throw new IllegalArgumentException("RottenTomatoesAPI: Invalid Movie ID - " + error.getMessage(), error);
        } catch (JSONException error) {
            throw new IllegalArgumentException("RottenTomatoesAPI: Invalid Movie ID - " + error.getMessage(), error);
        }
}

    public static Set<Cast> getCastList(String searchUrl) {
        JSONObject castObject;
        Set<Cast> castList = new HashSet<Cast>();

        try {
            String response = WebBrowser.request(searchUrl);
            castObject = new JSONObject(response);
            castList = parseCast(castObject, CAST_FULL);
            return castList;
        } catch (IOException error) {
            logger.warn("RottenTomatoesAPI (CastList): " + error.getMessage());
            final Writer eResult = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(eResult);
            error.printStackTrace(printWriter);
            logger.warn(eResult.toString());
        } catch (JSONException error) {
            logger.warn("RottenTomatoesAPI (CastList): " + error.getMessage());
            final Writer eResult = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(eResult);
            error.printStackTrace(printWriter);
            logger.warn(eResult.toString());
        } catch (ParseException error) {
            logger.warn("RottenTomatoesAPI (CastList): " + error.getMessage());
            final Writer eResult = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(eResult);
            error.printStackTrace(printWriter);
            logger.warn(eResult.toString());
        }

        return castList;
    }

    /**
     * Parse the movie JSON object for Cast information
     * @param jMovie
     * @return
     */
    private static Set<Cast> parseCast(JSONObject jMovie, String castType) throws ParseException {
        Set<Cast> response = new HashSet<Cast>();

        try {
            JSONArray jsonCast = jMovie.getJSONArray(castType);

            for (int loop = 0 ; loop < jsonCast.length() ; ++loop) {
                JSONObject jCast = jsonCast.getJSONObject(loop);
                Cast cast = new Cast();
                cast.setCastName(readString(jCast, NAME));

                // Sometimes the cast member doesn't have a character name.
                if (jCast.length() > 1) {
                    JSONArray charList = jCast.getJSONArray(CHARACTERS);
                    for (int loop2 = 0; loop2 < charList.length(); loop2++) {
                        cast.addCharacter(charList.getString(loop2));
                    }
                }

                response.add(cast);
            }
        } catch (JSONException error) {
            throw new ParseException("RottenTomatoesAPI: Error parsing cast - " + error.getMessage(), 0);
        }

        return response;
    }

    /**
     * Parse the movie JSON object for the director names
     * @param jMovie
     * @param directorsAbdridged
     * @return
     */
    private static Set<String> parseDirectors(JSONObject jMovie, String directorType) {
        Set<String> response = new HashSet<String>();

        JSONArray jsonCast;
        try {
            jsonCast = jMovie.getJSONArray(directorType);
            for (int loop = 0 ; loop < jsonCast.length() ; ++loop) {
                JSONObject jCast = jsonCast.getJSONObject(loop);
                response.add(readString(jCast, NAME));
            }
        } catch (JSONException e) {
            // The GENRES weren't found, we can safely ignore this error
            // throw new RuntimeException("RottenTomatoesAPI: Error parsing directors - " + e.getMessage());
        }

        return response;
    }

    /**
     * Parse the movie JSON object for a link object
     * @param jMovie
     * @param linkType
     * @return
     */
    public static Set<Link> parseGenericLinks(JSONObject jMovie, String linkType) {
        Set<Link> response = new HashSet<Link>();

        JSONObject jObject;
        try {
            jObject = jMovie.getJSONObject(linkType);
        } catch (JSONException e) {
            logger.debug("No " + linkType + " in the object");
            return null;
        }

        @SuppressWarnings("unchecked")
        Iterator<String> linkList = jObject.keys();

        while (linkList.hasNext()) {
            String type = (String)linkList.next();

            Link link = new Link();
            link.setLinkType(type);
            link.setLinkUrl(readString(jObject, type));

            response.add(link);
        }

        return response;
    }

    private static Set<String> parseGenres(JSONObject jMovie) {
        Set<String> response = new HashSet<String>();

        try {
            JSONArray jGenres = jMovie.getJSONArray(GENRES);

            if (jGenres.length() > 1) {
                for (int loop = 0; loop < jGenres.length(); loop++) {
                    response.add(jGenres.getString(loop));
                }
            }
        } catch (JSONException e) {
            // The GENRES weren't found, we can safely ignore this error
            // throw new RuntimeException("RottenTomatoesAPI: Error parsing genres - " + e.getMessage());
        }

        return response;
}

    /**
     * Retrieve the link list from the URL
     * @param linkSearchUrl
     * @param linkType
     * @return
     */
    public static Set<Link> parseLinks(String linkSearchUrl, String linkType) throws ParseException {
        JSONObject objMaster;
        Set<Link> response;

        try {
            objMaster = new JSONObject(WebBrowser.request(linkSearchUrl));
            response = RTParser.parseGenericLinks(objMaster, linkType);
        } catch (JSONException error) {
            throw new ParseException("RottenTomatoesAPI: Error parsing links - " + error.getMessage(), 0);
        } catch (IOException error) {
            throw new ParseException("RottenTomatoesAPI: Error parsing links - " + error.getMessage(), 0);
        }

        return response;
    }

    /**
     * Parse a JSON object for all the movie information
     * @param jMovie
     * @return
     */
    public static Movie parseMovie(JSONObject jMovie) {
        Movie movie = new Movie();

        movie.setId(readInt(jMovie, ID));
        movie.setTitle(readString(jMovie, TITLE));
        movie.setYear(readInt(jMovie, YEAR));
        movie.setGenres(parseGenres(jMovie));
        movie.setCertification(readString(jMovie, MPAA_RATING));
        movie.setRuntime(readInt(jMovie, RUNTIME));
        movie.setReleaseDates(parseReleaseDates(jMovie));
        movie.setRatings(parseRatings(jMovie));
        movie.setSynopsis(readString(jMovie, SYNOPSIS));
        movie.setArtwork(parseGenericLinks(jMovie, POSTERS));
        try {
            movie.setCast(parseCast(jMovie, CAST_ABRIDGED));
        } catch (ParseException ex) {
            movie.setCast(new HashSet<Cast>());
        }
        movie.setDirectors(parseDirectors(jMovie, DIRECTORS_ABDRIDGED));
        movie.setLinks(parseGenericLinks(jMovie, LINKS));
        return movie;
    }

    /**
     * Parse the movie JSON object for the ratings.
     * The whole object is passed in so we can trap the JSON Exceptions
     * @param jMovie
     * @return
     */
    private static Map<String, Integer> parseRatings(JSONObject jMovie) {
        HashMap<String, Integer> response = new HashMap<String, Integer>();

        JSONObject jObject;
        try {
            jObject = jMovie.getJSONObject(RATINGS);
        } catch (JSONException e) {
            logger.debug("No ratings for the movie");
            return null;
        }

        @SuppressWarnings("unchecked")
        Iterator<String> ratingList = jObject.keys();

        String type;
        int score;
        while (ratingList.hasNext()) {
            type = (String)ratingList.next();
            score =  readInt(jObject, type);
            response.put(type, score);
        }

        return response;
    }

    /**
     * Parse the movie JSON object for the release dates.
     * The whole object is passed in so we can trap the JSON Exceptions
     * @param jMovie
     * @return
     */
    private static Set<ReleaseDate> parseReleaseDates(JSONObject jMovie) {
        Set<ReleaseDate> response = new HashSet<ReleaseDate>();

        JSONObject jObject;
        try {
            jObject = jMovie.getJSONObject(RELEASE_DATES);
        } catch (JSONException e) {
            logger.debug("No release dates for the movie");
            return null;
        }

        @SuppressWarnings("unchecked")
        Iterator<String> releaseDateList = jObject.keys();

        while (releaseDateList.hasNext()) {
            String type = (String)releaseDateList.next();

            ReleaseDate rd = new ReleaseDate();
            rd.setReleaseType(type);
            rd.setReleaseDate(readString(jObject, type));

            response.add(rd);
        }

        return response;
    }

    /**
     * Get an integer from a JSON Object
     * @param jObject
     * @param item
     * @return
     */
    private static int readInt(JSONObject jObject, String item) {
        try {
            return jObject.getInt(item);
        } catch (JSONException ignore) {
            // We can't read this as an integer, so it's probably null or an empty string
            return 0;
        }
    }

    /**
     * Get a string from a JSON object
     * @param jObject
     * @param item
     * @return
     */
    private static String readString(JSONObject jObject, String item) {
        String response = "";

        if (jObject == null) {
            return response;
        }

        try {
            response = jObject.getString(item);
        } catch (JSONException error) {
            // The item wasn't found.
        }

        return response;
    }

    public static Set<Review> getReviews(String searchUrl) throws ParseException {
        JSONObject reviewListObject;
        Set<Review> reviewList = new HashSet<Review>();

        try {
            reviewListObject = new JSONObject(WebBrowser.request(searchUrl));

            JSONArray array = reviewListObject.getJSONArray(REVIEWS);

            for (int loop = 0; loop < array.length(); loop++) {
                reviewList.add(parseReview(array.getJSONObject(loop)));
            }

            return reviewList;
        } catch (IOException error) {
            throw new ParseException("RottenTomatoesAPI: Error getting reviews - " + error.getMessage(), 0);
        } catch (JSONException error) {
            throw new ParseException("RottenTomatoesAPI: Error getting reviews - " + error.getMessage(), 0);
        }
    }

    /**
     * Parse an individual review
     * @param jReview
     * @return
     */
    private static Review parseReview(JSONObject jReview) {
        Review review = new Review();

        review.setCritic(readString(jReview, CRITIC));
        review.setReviewDate(readString(jReview, DATE));
        review.setPublication(readString(jReview, PUBLICATION));
        review.setQuote(readString(jReview, QUOTE));
        review.setLinks(parseGenericLinks(jReview, LINKS));

        return review;
    }

}
