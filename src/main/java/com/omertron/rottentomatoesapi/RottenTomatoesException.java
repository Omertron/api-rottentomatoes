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
package com.omertron.rottentomatoesapi;

public class RottenTomatoesException extends Exception {

    private static final long serialVersionUID = -8952129102483143278L;

    public enum RottenTomatoesExceptionType {

        UNKNOWN_CAUSE, INVALID_URL, HTTP_404_ERROR, MAPPING_FAILED, CONNECTION_ERROR, NO_API_KEY;
    }
    private final RottenTomatoesExceptionType exceptionType;
    private final String response;

    public RottenTomatoesException(final RottenTomatoesExceptionType exceptionType, final String response) {
        super(response);
        this.exceptionType = exceptionType;
        this.response = response;
    }

    public RottenTomatoesException(final RottenTomatoesExceptionType exceptionType, final String response, final Throwable cause) {
        super(cause);
        this.exceptionType = exceptionType;
        this.response = response;
    }

    public RottenTomatoesException(final RottenTomatoesExceptionType exceptionType, final Throwable cause) {
        super(cause);
        this.exceptionType = exceptionType;
        this.response = cause.getMessage();
    }

    public RottenTomatoesExceptionType getExceptionType() {
        return exceptionType;
    }

    public String getResponse() {
        return response;
    }
}
