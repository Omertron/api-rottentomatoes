/*
 *      Copyright (c) 2004-2013 Stuart Boston
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

import com.fasterxml.jackson.annotation.JsonAnySetter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The base abstract class for the models to extend
 *
 * Contains the default "AnySetter" setter for unknown JSON properties
 *
 * @author stuart.boston
 */
public abstract class AbstractJsonMapping {

    private static Logger getLogger(Class<?> aClass) {
        return LoggerFactory.getLogger(aClass);
    }

    /**
     * Handle unknown properties and print a message
     *
     * @param key
     * @param value
     */
    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
        StringBuilder unknown = new StringBuilder();
        unknown.append("Unknown property: '").append(key);
        unknown.append("' value: '").append(value).append("'");

        getLogger(this.getClass()).trace(unknown.toString());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
