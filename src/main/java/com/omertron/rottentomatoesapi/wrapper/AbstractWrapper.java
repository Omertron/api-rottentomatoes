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
package com.omertron.rottentomatoesapi.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.omertron.rottentomatoesapi.model.AbstractJsonMapping;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Stuart
 */
public abstract class AbstractWrapper extends AbstractJsonMapping implements IWrapperError {

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

}
