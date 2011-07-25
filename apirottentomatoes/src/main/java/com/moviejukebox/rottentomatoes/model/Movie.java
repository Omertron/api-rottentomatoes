/*
 *      Copyright (c) 2004-2011 YAMJ Members
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Movie {
    private int                     id              = 0;
    private String                  title           = "";
    private int                     year            = 0;
    private int                     runtime         = 0;
    private Set<ReleaseDate>        releaseDates    = new HashSet<ReleaseDate>();
    private Map<String, Integer>    ratings         = new HashMap<String, Integer>();
    private String                  synopsis        = "";
    private Set<Link>               artwork         = new HashSet<Link>();
    private Set<Cast>               cast            = new HashSet<Cast>();
    private Set<Link>               links           = new HashSet<Link>();
    private Set<String>             genres          = new HashSet<String>();
    private String                  certification   = "";
    private Set<String>             directors       = new HashSet<String>();
    
    public void addArtwork(Link artwork) {
        this.artwork.add(artwork);
    }
    
    public void addCast(Cast cast) {
        this.cast.add(cast);
    }
    
    public void addGenre(String genre) {
        this.genres.add(genre);
    }
    
    public void addLink(Link link) {
        this.links.add(link);
    }
    
    public void addRating(String ratingType, int ratingScore) {
        this.ratings.put(ratingType, ratingScore);
    }
    
    public void addReleaseDate(ReleaseDate releaseDate) {
        this.releaseDates.add(releaseDate);
    }
    
    public Set<Link> getArtwork() {
        return artwork;
    }
    
    public Set<Cast> getCast() {
        return cast;
    }
    
    public String getCertification() {
        return certification;
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
    
    public Set<Link> getLinks() {
        return links;
    }
    
    public int getRating(String ratingType) {
        return this.ratings.get(ratingType);
    }
    
    public Map<String, Integer> getRatings() {
        return ratings;
    }
    
    public Set<ReleaseDate> getReleaseDates() {
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
    
    public void setArtwork(Set<Link> artwork) {
        this.artwork = artwork;
    }
    
    public void setCast(Set<Cast> cast) {
        this.cast = cast;
    }
    
    public void setCertification(String certification) {
        this.certification = certification;
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
    
    public void setLinks(Set<Link> links) {
        this.links = links;
    }
    
    public void setRatings(Map<String, Integer> ratings) {
        this.ratings = ratings;
    }
    
    public void setReleaseDates(Set<ReleaseDate> releaseDates) {
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[Movie=[id=");
        builder.append(id);
        builder.append("][title=");
        builder.append(title);
        builder.append("][year=");
        builder.append(year);
        builder.append("][runtime=");
        builder.append(runtime);
        builder.append("][releaseDates=");
        builder.append(releaseDates);
        builder.append("][ratings=");
        builder.append(ratings);
        builder.append("][synopsis=");
        builder.append(synopsis);
        builder.append("][artwork=");
        builder.append(artwork);
        builder.append("][cast=");
        builder.append(cast);
        builder.append("][links=");
        builder.append(links);
        builder.append("][genres=");
        builder.append(genres);
        builder.append("][certification=");
        builder.append(certification);
        builder.append("][directors=");
        builder.append(directors);
        builder.append("]]");
        return builder.toString();
    }
    
}
