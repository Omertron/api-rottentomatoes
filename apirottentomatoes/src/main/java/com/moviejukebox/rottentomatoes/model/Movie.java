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

import java.util.HashSet;

public class Movie {
    private String                  title           = "";
    private int                     year            = 0;
    private int                     runtime         = 0;
    private HashSet<ReleaseDate>    releaseDates    = new HashSet<ReleaseDate>();
    private HashSet<Rating>         ratings         = new HashSet<Rating>();
    private String                  synopsis        = "";
    private HashSet<Artwork>        artwork         = new HashSet<Artwork>();
    private HashSet<Cast>           cast            = new HashSet<Cast>();
    private HashSet<Link>           links           = new HashSet<Link>();
    private HashSet<String>         genres          = new HashSet<String>();
    private String                  certification   = "";
    private HashSet<String>         directors       = new HashSet<String>();
    
    public void addArtwork(Artwork artwork) {
        this.artwork.add(artwork);
    }
    
    public void addCast(Cast cast) {
        this.cast.add(cast);
    }
    
    public void addReleaseDate(ReleaseDate releaseDate) {
        this.releaseDates.add(releaseDate);
    }
    
    public void addRating(Rating rating) {
        this.ratings.add(rating);
    }
    
    public void addLink(Link link) {
        this.links.add(link);
    }
    
    public void addGenre(String genre) {
        this.genres.add(genre);
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
    
    public int getRuntime() {
        return runtime;
    }
    
    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }
    
    public HashSet<ReleaseDate> getReleaseDates() {
        return releaseDates;
    }
    
    public void setReleaseDates(HashSet<ReleaseDate> releaseDates) {
        this.releaseDates = releaseDates;
    }
    
    public HashSet<Rating> getRatings() {
        return ratings;
    }
    
    public void setRatings(HashSet<Rating> ratings) {
        this.ratings = ratings;
    }
    
    public String getSynopsis() {
        return synopsis;
    }
    
    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
    
    public HashSet<Artwork> getArtwork() {
        return artwork;
    }
    
    public void setArtwork(HashSet<Artwork> artwork) {
        this.artwork = artwork;
    }
    
    public HashSet<Cast> getCast() {
        return cast;
    }
    
    public void setCast(HashSet<Cast> cast) {
        this.cast = cast;
    }
    
    public HashSet<Link> getLinks() {
        return links;
    }
    
    public void setLinks(HashSet<Link> links) {
        this.links = links;
    }
    
    public HashSet<String> getGenres() {
        return genres;
    }
    
    public void setGenres(HashSet<String> genres) {
        this.genres = genres;
    }
    
    public String getCertification() {
        return certification;
    }
    
    public void setCertification(String certification) {
        this.certification = certification;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[Movie=[title=");
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

    public HashSet<String> getDirectors() {
        return directors;
    }

    public void setDirectors(HashSet<String> directors) {
        this.directors = directors;
    }
    
}
