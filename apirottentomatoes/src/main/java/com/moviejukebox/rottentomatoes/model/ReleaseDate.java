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

public class ReleaseDate {
    private String releaseType;
    private String releaseDate;
    
    public ReleaseDate() {
        this.releaseType = null;
        this.releaseDate = "";
    }

    public ReleaseDate(String releaseType, String releaseDate) {
        this.releaseType = releaseType;
        this.releaseDate = releaseDate;
    }

    public String getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(String releaseType) {
        this.releaseType = releaseType;
    }
    
    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[ReleaseDate=[releaseType=");
        builder.append(releaseType);
        builder.append("], [releaseDate=");
        builder.append(releaseDate);
        builder.append("]]");
        return builder.toString();
    }
    
}
