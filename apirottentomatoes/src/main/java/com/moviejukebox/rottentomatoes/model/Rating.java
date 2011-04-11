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

public class Rating {
    private String ratingType;
    private int    ratingScore;
    
    public Rating() {
        this.ratingType = null;
        this.ratingScore = 0;
    }

    public Rating(String ratingType, int ratingScore) {
        this.ratingType = ratingType;
        this.ratingScore = ratingScore;
    }

    public String getRatingType() {
        return ratingType;
    }
    
    public void setRatingType(String ratingType) {
        this.ratingType = ratingType;
    }
    
    public int getRatingScore() {
        return ratingScore;
    }
    
    public void setRatingScore(int ratingScore) {
        this.ratingScore = ratingScore;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[Rating=[ratingType=");
        builder.append(ratingType);
        builder.append("], [ratingScore=");
        builder.append(ratingScore);
        builder.append("]]");
        return builder.toString();
    }
    
}
