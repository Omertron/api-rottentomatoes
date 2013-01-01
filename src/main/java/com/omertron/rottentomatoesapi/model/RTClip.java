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
package com.omertron.rottentomatoesapi.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class RTClip implements Serializable {

    /*
     * Logger
     */
    private static final Logger logger = Logger.getLogger(RTClip.class);
    /*
     * Serial Version
     */
    private static final long serialVersionUID = 1L;
    /*
     * Properties
     */
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
        logger.warn(unknownBuilder.toString());
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
