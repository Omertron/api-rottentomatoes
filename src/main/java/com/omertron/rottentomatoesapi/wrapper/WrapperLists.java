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
package com.omertron.rottentomatoesapi.wrapper;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.omertron.rottentomatoesapi.model.RTCast;
import com.omertron.rottentomatoesapi.model.RTClip;
import com.omertron.rottentomatoesapi.model.RTMovie;
import com.omertron.rottentomatoesapi.model.Review;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class WrapperLists {
    // Logger

    private static final Logger logger = Logger.getLogger(WrapperLists.class);
    /*
     * Properties
     */
    @JsonProperty("total")
    private int total;
    @JsonProperty("movies")
    private List<RTMovie> movies = new ArrayList<RTMovie>();
    @JsonProperty("links")
    private Map<String, String> links = new HashMap<String, String>();
    @JsonProperty("cast")
    private List<RTCast> cast = new ArrayList<RTCast>();
    @JsonProperty("clips")
    private List<RTClip> clips = new ArrayList<RTClip>();
    @JsonProperty("reviews")
    private List<Review> reviews = new ArrayList<Review>();
    @JsonProperty("link_template")
    private String linkTemplate;
    @JsonProperty("error")
    private String error = "";

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<RTMovie> getMovies() {
        return movies;
    }

    public void setMovies(List<RTMovie> movies) {
        this.movies = movies;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getLinkTemplate() {
        return linkTemplate;
    }

    public void setLinkTemplate(String linkTemplate) {
        this.linkTemplate = linkTemplate;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }

    public List<RTCast> getCast() {
        return cast;
    }

    public void setCast(List<RTCast> cast) {
        this.cast = cast;
    }

    public List<RTClip> getClips() {
        return clips;
    }

    public void setClips(List<RTClip> clips) {
        this.clips = clips;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    //</editor-fold>

    /**
     * Handle unknown properties and print a message
     *
     * @param key
     * @param value
     */
    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
        StringBuilder unknownBuilder = new StringBuilder();
        unknownBuilder.append("Unknown property: '").append(key);
        unknownBuilder.append("' value: '").append(value).append("'");
        logger.warn(unknownBuilder.toString());
    }

    /**
     * Check to see if the returned values are valid
     *
     * @return
     */
    public boolean isValid() {
        // If the error string is empty, everything is OK
        if (StringUtils.isBlank(error)) {
            return true;
        }
        return false;
    }
}
