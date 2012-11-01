/*
 *      Copyright (c) 2004-2012 Stuart Boston
 *
 *      This software is licensed under a Creative Commons License
 *      See the LICENCE.txt file included in this package
 *
 *      For any reuse or distribution, you must make clear to others the
 *      license terms of this work.
 */
package com.moviejukebox.rottentomatoes.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;

public class RTCast implements Serializable {

    /*
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(RTCast.class);
    /*
     * Serial Version
     */
    private static final long serialVersionUID = 1L;
    /*
     * Properties
     */
    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String castName;
    @JsonProperty("characters")
    private Set<String> characters = new HashSet<String>();

    //<editor-fold defaultstate="collapsed" desc="Getter methods">
    public String getCastName() {
        return castName;
    }

    public Set<String> getCharacters() {
        return characters;
    }

    public int getId() {
        return id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Setter methods">
    public void setCastName(String castName) {
        this.castName = castName;
    }

    public void setCharacters(Set<String> characters) {
        this.characters = characters;
    }

    public void setId(int id) {
        this.id = id;
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
        builder.append("[Cast=");
        builder.append("[id=").append(id);
        builder.append("], [castName=").append(castName);
        builder.append("], [characters=").append(characters);
        builder.append("]]");
        return builder.toString();
    }
}
