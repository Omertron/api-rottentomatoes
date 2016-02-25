/*
 *      Copyright (c) 2004-2016 Stuart Boston
 *
 *      This file is part of the RottenTomatoes API.
 *
 *      The RottenTomatoes API is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      The RottenTomatoes API is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with the RottenTomatoes API.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.omertron.rottentomatoesapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class RTCast extends AbstractJsonMapping implements Serializable {

    /*
     * Serial Version
     */
    private static final long serialVersionUID = 1L;
    /*
     * Properties
     */
    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String castName;
    @JsonProperty("characters")
    private Set<String> characters = new HashSet<String>();

    //<editor-fold defaultstate="collapsed" desc="Getter methods">
    public String getCastName() {
        return castName;
    }

    public Set<String> getCharacters() {
        return characters;
    }

    public int getId() {
        return id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Setter methods">
    public void setCastName(String castName) {
        this.castName = castName;
    }

    public void setCharacters(Set<String> characters) {
        this.characters = characters;
    }

    public void setId(int id) {
        this.id = id;
    }
    //</editor-fold>

}
