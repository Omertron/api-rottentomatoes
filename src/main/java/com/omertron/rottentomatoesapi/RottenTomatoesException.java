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
package com.omertron.rottentomatoesapi;

import java.net.URL;
import org.yamj.api.common.exception.ApiException;
import org.yamj.api.common.exception.ApiExceptionType;

public class RottenTomatoesException extends ApiException {

    public RottenTomatoesException(ApiExceptionType exceptionType, String response) {
        super(exceptionType, response);
    }

    public RottenTomatoesException(ApiExceptionType exceptionType, String response, URL url) {
        super(exceptionType, response, url);
    }

    public RottenTomatoesException(ApiExceptionType exceptionType, String response, int responseCode, URL url) {
        super(exceptionType, response, responseCode, url);
    }

    public RottenTomatoesException(ApiExceptionType exceptionType, String response, String url) {
        super(exceptionType, response, url);
    }

    public RottenTomatoesException(ApiExceptionType exceptionType, String response, int responseCode, String url) {
        super(exceptionType, response, responseCode, url);
    }

    public RottenTomatoesException(ApiExceptionType exceptionType, String response, URL url, Throwable cause) {
        super(exceptionType, response, url, cause);
    }

    public RottenTomatoesException(ApiExceptionType exceptionType, String response, int responseCode, URL url, Throwable cause) {
        super(exceptionType, response, responseCode, url, cause);
    }

    public RottenTomatoesException(ApiExceptionType exceptionType, String response, String url, Throwable cause) {
        super(exceptionType, response, url, cause);
    }

    public RottenTomatoesException(ApiExceptionType exceptionType, String response, int responseCode, String url, Throwable cause) {
        super(exceptionType, response, responseCode, url, cause);
    }

}
