/*
 *      Copyright (c) 2004-2015 Stuart Boston
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

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Review extends AbstractJsonMapping implements Serializable {

    /*
     * Serial Version
     */
    private static final long serialVersionUID = 1L;
    /*
     * Properties
     */
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

}
