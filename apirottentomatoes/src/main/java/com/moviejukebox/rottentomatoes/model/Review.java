package com.moviejukebox.rottentomatoes.model;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonProperty;

public class Review {

    private static final Logger LOGGER = Logger.getLogger(Review.class);
    @JsonProperty("critic")
    private String critic;
    @JsonProperty("date")
    private String date;
    @JsonProperty("original_score")
    private String originalScore = "";
    @JsonProperty("freshness")
    private String freshness;
    @JsonProperty("publication")
    private String publication;
    @JsonProperty("quote")
    private String quote;
    @JsonProperty("links")
    private Map<String, String> links = new HashMap<String, String>();

    //<editor-fold defaultstate="collapsed" desc="Getter methods">
    public String getCritic() {
        return critic;
    }
    
    public String getDate() {
        return date;
    }

    public String getOriginalScore() {
        return originalScore;
    }
    
    public String getFreshness() {
        return freshness;
    }
    
    public Map<String, String> getLinks() {
        return links;
    }
    
    public String getPublication() {
        return publication;
    }
    
    public String getQuote() {
        return quote;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Setter methods">
    public void setCritic(String critic) {
        this.critic = critic;
    }
    
    public void setDate(String date) {
        this.date = date;
    }

    public void setOriginalScore(String originalScore) {
        this.originalScore = originalScore;
    }
    
    public void setFreshness(String freshness) {
        this.freshness = freshness;
    }
    
    public void setLinks(Map<String, String> links) {
        this.links = links;
    }
    
    public void setPublication(String publication) {
        this.publication = publication;
    }
    
    public void setQuote(String quote) {
        this.quote = quote;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[Review=");
        builder.append("[critic=").append(critic);
        builder.append("], [date=").append(date);
        builder.append("], [originalScore=").append(originalScore);
        builder.append("], [freshness=").append(freshness);
        builder.append("], [publication=").append(publication);
        builder.append("], [quote=").append(quote);
        builder.append("], [links=").append(links);
        builder.append("]]");
        return builder.toString();
    }
}
