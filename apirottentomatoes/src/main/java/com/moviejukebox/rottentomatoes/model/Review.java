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

public class Review {
    public static enum ReviewType {all, top_critic, dvd};
    
    private String          critic      = "";
    private String          reviewDate  = "";
    private String          publication = "";
    private String          quote       = "";
    private HashSet<Link>   links       = new HashSet<Link>();

    public String getCritic() {
        return critic;
    }
    
    public void setCritic(String critic) {
        this.critic = critic;
    }
    
    public String getReviewDate() {
        return reviewDate;
    }
    
    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }
    
    public String getPublication() {
        return publication;
    }
    
    public void setPublication(String publication) {
        this.publication = publication;
    }
    
    public String getQuote() {
        return quote;
    }
    
    public void setQuote(String quote) {
        this.quote = quote;
    }
    
    public HashSet<Link> getLinks() {
        return links;
    }
    
    public void setLinks(HashSet<Link> links) {
        this.links = links;
    }
    
    public void addLink(Link link) {
        this.links.add(link);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[Review=[critic=");
        builder.append(critic);
        builder.append("], [reviewDate=");
        builder.append(reviewDate);
        builder.append("], [publication=");
        builder.append(publication);
        builder.append("], [quote=");
        builder.append(quote);
        builder.append("], [links=");
        builder.append(links);
        builder.append("]]");
        return builder.toString();
    }
}
