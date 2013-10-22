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

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class RTMovie extends AbstractJsonMapping implements Serializable {

    /*
     * Serial Version
     */
    private static final long serialVersionUID = 1L;
    /*
     * Properties
     */
    @JsonProperty("id")
    private int id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("year")
    private int year;
    @JsonProperty("mpaa_rating")
    private String mpaaRating;
    @JsonProperty("runtime")
    private int runtime;
    @JsonProperty("critics_consensus")
    private String criticsConsensus;
    @JsonProperty("release_dates")
    private Map<String, String> releaseDates = new HashMap<String, String>();
    @JsonProperty("ratings")
    private Map<String, String> ratings = new HashMap<String, String>();
    @JsonProperty("synopsis")
    private String synopsis;
    @JsonProperty("posters")
    private Map<String, String> artwork = new HashMap<String, String>();
    @JsonProperty("abridged_cast")
    private Set<RTCast> cast = new HashSet<RTCast>();
    @JsonProperty("alternate_ids")
    private Map<String, String> alternateIds = new HashMap<String, String>();
    @JsonProperty("links")
    private Map<String, String> links = new HashMap<String, String>();
    @JsonProperty("genres")
    private Set<String> genres = new HashSet<String>();
    @JsonProperty("certification")
    private String certification;
    @JsonProperty("abridged_directors")
    private Set<RTPerson> directors = new HashSet<RTPerson>();
    @JsonProperty("studio")
    private String studio;
    // Error property
    @JsonProperty("error")
    private String error = "";
    @JsonProperty("link_template")
    private String linkTemplate;

    //<editor-fold defaultstate="collapsed" desc="Getter Methods">
    public Map<String, String> getAlternateIds() {
        return alternateIds;
    }

    public Map<String, String> getArtwork() {
        return artwork;
    }

    public Set<RTCast> getCast() {
        return cast;
    }

    public String getCertification() {
        return certification;
    }

    public String getCriticsConsensus() {
        return criticsConsensus;
    }

    public Set<RTPerson> getDirectors() {
        return directors;
    }

    public Set<String> getGenres() {
        return genres;
    }

    public int getId() {
        return id;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public String getMpaaRating() {
        return mpaaRating;
    }

    public Map<String, String> getRatings() {
        return ratings;
    }

    public Map<String, String> getReleaseDates() {
        return releaseDates;
    }

    public int getRuntime() {
        return runtime;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public String getError() {
        return error;
    }

    public String getLinkTemplate() {
        return linkTemplate;
    }

    public String getStudio() {
        return studio;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Setter Methods">
    public void setAlternateIds(Map<String, String> alternateIds) {
        this.alternateIds = alternateIds;
    }

    public void setArtwork(Map<String, String> artwork) {
        this.artwork = artwork;
    }

    public void setCast(Set<RTCast> cast) {
        this.cast = cast;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public void setCriticsConsensus(String criticsConsensus) {
        this.criticsConsensus = criticsConsensus;
    }

    public void setDirectors(Set<RTPerson> directors) {
        this.directors = directors;
    }

    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }

    public void setMpaaRating(String mpaaRating) {
        this.mpaaRating = mpaaRating;
    }

    public void setRatings(Map<String, String> ratings) {
        this.ratings = ratings;
    }

    public void setReleaseDates(Map<String, String> releaseDates) {
        this.releaseDates = releaseDates;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setLinkTemplate(String linkTemplate) {
        this.linkTemplate = linkTemplate;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }
    //</editor-fold>

    /**
     * Check to see if the returned values are valid
     *
     * @return
     */
    public boolean isValid() {
        return StringUtils.isBlank(error);
    }
}
