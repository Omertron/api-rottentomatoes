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

public class Cast {
    private String name;
    private HashSet<String> characters;
    
    public Cast() {
        this.name = "";
        this.characters = new HashSet<String>();
    }

    public Cast(String name, HashSet<String> characters) {
        this.name = name;
        this.characters = characters;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public HashSet<String> getCharacters() {
        return characters;
    }

    public void setCharacters(HashSet<String> characters) {
        this.characters = characters;
    }
    
    public void addCharacter(String character) {
        this.characters.add(character);
    }
}
