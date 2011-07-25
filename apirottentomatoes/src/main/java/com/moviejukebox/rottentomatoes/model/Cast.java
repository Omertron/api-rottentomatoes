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
import java.util.Set;

public class Cast {
    private String castName;
    private Set<String> characters;
    
    public Cast() {
        this.castName = "";
        this.characters = new HashSet<String>();
    }

    public Cast(String castName, Set<String> characters) {
        this.castName = castName;
        this.characters = characters;
    }

    public String getCastName() {
        return castName;
    }
    
    public void setCastName(String castName) {
        this.castName = castName;
    }

    public Set<String> getCharacters() {
        return characters;
    }

    public void setCharacters(Set<String> characters) {
        this.characters = characters;
    }
    
    public void addCharacter(String character) {
        this.characters.add(character);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[Cast=[castName=");
        builder.append(castName);
        builder.append("], [characters=");
        builder.append(characters);
        builder.append("]]");
        return builder.toString();
    }
}
