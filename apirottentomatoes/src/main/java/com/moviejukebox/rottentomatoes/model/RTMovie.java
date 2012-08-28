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
package com.moviejukebox.rottentomatoes.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonProperty;

public class RTMovie implements Serializable {

    /*
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(RTMovie.class);
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
    private Set<Cast> cast = new HashSet<Cast>();
    @JsonProperty("alternate_ids")
    private Map<String, String> alternateIds = new HashMap<String, String>();
    @JsonProperty("links")
    private Map<String, String> links = new HashMap<String, String>();
    @JsonProperty("genres")
    private Set<String> genres = new HashSet<String>();
    @JsonProperty("certification")
    private String certification;
    @JsonProperty("abridged_directors")
    private Set<String> directors = new HashSet<String>();
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

    public Set<Cast> getCast() {
        return cast;
    }

    public String getCertification() {
        return certification;
    }

    public String getCriticsConsensus() {
        return criticsConsensus;
    }

    public Set<String> getDirectors() {
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

    public void setCast(Set<Cast> cast) {
        this.cast = cast;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public void setCriticsConsensus(String criticsConsensus) {
        this.criticsConsensus = criticsConsensus;
    }

    public void setDirectors(Set<String> directors) {
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
        StringBuilder sb = new StringBuilder("[RTMovie=[");
        sb.append("id=").append(id);
        sb.append("], [title=").append(title);
        sb.append("], [year=").append(year);
        sb.append("], [mpaaRating=").append(mpaaRating);
        sb.append("], [runtime=").append(runtime);
        sb.append("], [criticsConsensus=").append(criticsConsensus);
        sb.append("], [releaseDates=").append(releaseDates);
        sb.append("], [ratings=").append(ratings);
        sb.append("], [synopsis=").append(synopsis);
        sb.append("], [artwork=").append(artwork);
        sb.append("], [cast=").append(cast);
        sb.append("], [alternateIds=").append(alternateIds);
        sb.append("], [links=").append(links);
        sb.append("], [genres=").append(genres);
        sb.append("], [certification=").append(certification);
        sb.append("], [directors=").append(directors);
        sb.append("], [studio=").append(studio);
        sb.append("]]");
        return sb.toString();
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