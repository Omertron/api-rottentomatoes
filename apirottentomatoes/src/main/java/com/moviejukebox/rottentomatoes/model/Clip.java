package com.moviejukebox.rottentomatoes.model;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonProperty;

public class Clip {

    private static final Logger LOGGER = Logger.getLogger(Clip.class);
    @JsonProperty("title")
    private String title;
    @JsonProperty("duration")
    private int duration;
    @JsonProperty("thumbnail")
    private String thumbnail;
    @JsonProperty("links")
    private Map<String, String> links = new HashMap<String, String>();

    //<editor-fold defaultstate="collapsed" desc="Getter methods">
    public int getDuration() {
        return duration;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getTitle() {
        return title;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Setter methods">
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setTitle(String title) {
        this.title = title;
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
        builder.append("[Clip=");
        builder.append("[title=").append(title);
        builder.append("], [duration=").append(duration);
        builder.append("], [thumbnail=").append(thumbnail);
        builder.append("], [links=").append(links);
        builder.append("]]");
        return builder.toString();
    }
}
