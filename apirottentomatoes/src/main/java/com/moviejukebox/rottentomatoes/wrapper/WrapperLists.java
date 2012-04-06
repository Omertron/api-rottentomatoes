package com.moviejukebox.rottentomatoes.wrapper;

import com.moviejukebox.rottentomatoes.model.Cast;
import com.moviejukebox.rottentomatoes.model.Clip;
import com.moviejukebox.rottentomatoes.model.RTMovie;
import com.moviejukebox.rottentomatoes.model.Review;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonProperty;

public class WrapperLists {
    // Logger

    private static final Logger LOGGER = Logger.getLogger(WrapperLists.class);
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
    private List<Cast> cast = new ArrayList<Cast>();
    @JsonProperty("clips")
    private List<Clip> clips = new ArrayList<Clip>();
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
    
    public List<Cast> getCast() {
        return cast;
    }
    
    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }

    public List<Clip> getClips() {
        return clips;
    }

    public void setClips(List<Clip> clips) {
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
        LOGGER.warn(unknownBuilder.toString());
    }

    /**
     * Check to see if the returned values are valid
     *
     * @return
     */
    public boolean isValid() {
        if (StringUtils.isBlank(error)) {
            return true;
        } else {
            return false;
        }
    }
}
