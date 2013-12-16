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
package com.omertron.rottentomatoesapi.tools;

import com.omertron.rottentomatoesapi.RottenTomatoesException;
import com.omertron.rottentomatoesapi.RottenTomatoesException.RottenTomatoesExceptionType;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(ApiBuilder.class);
    private static String apiKey;
    // Properties
    public static final String PROPERTY_URL = "url";
    public static final String PROPERTY_LIMIT = "limit";
    public static final String PROPERTY_PAGE_LIMIT = "page_limit";
    public static final String PROPERTY_PAGE = "page";
    public static final String PROPERTY_COUNTRY = "country";
    public static final String PROPERTY_REVIEW_TYPE = "review_type";
    public static final String PROPERTY_ID = "id";
    public static final String PROPERTY_TYPE = "type";
    public static final String PROPERTY_QUERY = "q";
    // API Base information
    private static final String API_SITE = "http://api.rottentomatoes.com/api/public/";
    private static final String API_VERSION = "v1.0";
    private static final String API_PREFIX = ".json?apikey=";
    // Movie replacement token
    private static final String MOVIE_ID = "{movie-id}";
    // Defaults and max
    private static final int LIMIT_MAX = 50;

    protected ApiBuilder() {
        throw new UnsupportedOperationException("Class can not be instantiated");
    }

    public static void addApiKey(String newApiKey) {
        apiKey = newApiKey;
    }

    /**
     * Create the URL
     *
     * @param properties
     * @return
     * @throws RottenTomatoesException
     */
    public static String create(Map<String, String> properties) throws RottenTomatoesException {
        if (StringUtils.isBlank(apiKey)) {
            throw new RottenTomatoesException(RottenTomatoesExceptionType.INVALID_URL, "Missing API Key");
        }

        StringBuilder urlBuilder = new StringBuilder(API_SITE);
        urlBuilder.append(API_VERSION);

        if (properties.containsKey(PROPERTY_URL) && StringUtils.isNotBlank(properties.get(PROPERTY_URL))) {
            urlBuilder.append(properties.get(PROPERTY_URL));
            // We don't need this property anymore
            properties.remove(PROPERTY_URL);
        } else {
            throw new RottenTomatoesException(RottenTomatoesExceptionType.INVALID_URL, "No URL specified");
        }

        urlBuilder.append(API_PREFIX).append(apiKey);

        for (Map.Entry<String, String> property : properties.entrySet()) {
            // Validate the key/value
            if (StringUtils.isNotBlank(property.getKey()) && StringUtils.isNotBlank(property.getValue())) {
                urlBuilder.append("&").append(property.getKey()).append("=").append(property.getValue());
            }
        }

        LOG.trace("URL: " + urlBuilder.toString());
        return urlBuilder.toString();
    }

    /**
     * Create the URL with the movie ID
     *
     * @param properties
     * @param movieId
     * @return
     * @throws RottenTomatoesException
     */
    public static String create(Map<String, String> properties, int movieId) throws RottenTomatoesException {
        String urlBuilder = create(properties).replace(MOVIE_ID, String.valueOf(movieId));
        return urlBuilder;
    }

    /**
     * Validate the key and value of a property
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean validateProperty(String key, String value) {
        return !StringUtils.isBlank(key) && !StringUtils.isBlank(value);
    }

    /**
     * Validate and convert the limit property
     *
     * @param limit
     * @return
     */
    public static String validateLimit(int limit) {
        if (limit < 1) {
            // 0 is a valid, null value
            return "";
        }

        if (limit > LIMIT_MAX) {
            return String.valueOf(LIMIT_MAX);
        }

        return String.valueOf(limit);
    }

    /**
     * Validate and convert the page limit property
     *
     * @param pageLimit
     * @return
     */
    public static String validatePageLimit(int pageLimit) {
        // Same validation as the limit
        return validateLimit(pageLimit);
    }

    /**
     * Validate the page property
     *
     * @param page
     * @return
     */
    public static String validatePage(int page) {
        if (page < 1) {
            return "";
        }

        return String.valueOf(page);
    }

    /**
     * Validate the country property
     *
     * @param country
     * @return
     */
    public static String validateCountry(String country) {
        if (country.length() > 2) {
            return country.substring(0, 2);
        }

        return country;
    }
}
