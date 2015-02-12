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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The Rotten Tomatoes movie details
 *
 * Note: Critics Consensus and Synopsis are no longer provided by the API
 *
 * @author Stuart.Boston
 */
@JsonIgnoreProperties({"critics_consensus", "synopsis"})
public class RTMovie extends AbstractJsonMapping implements Serializable {

    private static final long serialVersionUID = 1L;
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
    @JsonProperty("release_dates")
    private Map<String, String> releaseDates = new HashMap<String, String>();
    @JsonProperty("ratings")
    private Map<String, String> ratings = new HashMap<String, String>();
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
    @JsonProperty("abridged_directors")
    private Set<RTPerson> directors = new HashSet<RTPerson>();
    @JsonProperty("studio")
    private String studio;
    @JsonProperty("link_template")
    private String linkTemplate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMpaaRating() {
        return mpaaRating;
    }

    public void setMpaaRating(String mpaaRating) {
        this.mpaaRating = mpaaRating;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public Map<String, String> getReleaseDates() {
        return releaseDates;
    }

    public void setReleaseDates(Map<String, String> releaseDates) {
        this.releaseDates = releaseDates;
    }

    public Map<String, String> getRatings() {
        return ratings;
    }

    public void setRatings(Map<String, String> ratings) {
        this.ratings = ratings;
    }

    public Map<String, String> getArtwork() {
        return artwork;
    }

    public void setArtwork(Map<String, String> artwork) {
        this.artwork = artwork;
    }

    public Set<RTCast> getCast() {
        return cast;
    }

    public void setCast(Set<RTCast> cast) {
        this.cast = cast;
    }

    public Map<String, String> getAlternateIds() {
        return alternateIds;
    }

    public void setAlternateIds(Map<String, String> alternateIds) {
        this.alternateIds = alternateIds;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }

    public Set<String> getGenres() {
        return genres;
    }

    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }

    public Set<RTPerson> getDirectors() {
        return directors;
    }

    public void setDirectors(Set<RTPerson> directors) {
        this.directors = directors;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public String getLinkTemplate() {
        return linkTemplate;
    }

    public void setLinkTemplate(String linkTemplate) {
        this.linkTemplate = linkTemplate;
    }

}
