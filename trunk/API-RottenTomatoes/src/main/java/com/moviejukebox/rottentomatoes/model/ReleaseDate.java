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

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReleaseDate {
    private String type;
    private Date releaseDate;
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public ReleaseDate() {
        this.type = "";
        this.releaseDate = null;
    }

    public ReleaseDate(String type, Date releaseDate) {
        this.type = type;
        this.releaseDate = releaseDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public static SimpleDateFormat getDateFormat() {
        return dateFormat;
    }
    
    
}
