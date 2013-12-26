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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.omertron.rottentomatoesapi.wrapper.IWrapperError;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;

/**
 * Abstract base class for the RT objects
 *
 * Handles any unknown properties by outputting a log message<br/>
 * Handles error messages
 *
 * @author stuart.boston
 */
public abstract class AbstractJsonMapping implements Serializable, IWrapperError {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractJsonMapping.class);
    @JsonProperty("error")
    private String error = "";

    @Override
    public String getError() {
        return error;
    }

    @Override
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Check to see if the returned values are valid
     *
     * @return
     */
    @Override
    public boolean isValid() {
        return StringUtils.isBlank(error);
    }

    /**
     * Handle unknown properties and print a message
     *
     * @param key
     * @param value
     */
    @JsonAnySetter
    protected void handleUnknown(String key, Object value) {
        StringBuilder unknown = new StringBuilder(this.getClass().getSimpleName());
        unknown.append(": Unknown property='").append(key);
        unknown.append("' value='").append(value).append("'");

        LOG.trace(unknown.toString());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
