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

public class Link {
    private String linkType;
    private String linkUrl;
    
    public Link() {
        this.linkType = "";
        this.linkUrl = "";
    }

    public Link(String linkType, String linkUrl) {
        this.linkType = linkType;
        this.linkUrl = linkUrl;
    }

    public String getLinkType() {
        return linkType;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[Link=[linkType=");
        builder.append(linkType);
        builder.append("], [linkUrl=");
        builder.append(linkUrl);
        builder.append("]]");
        return builder.toString();
    }

}
