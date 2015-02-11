/*
 *      Copyright (c) 2004-2015 Stuart Boston
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omertron.rottentomatoesapi.RottenTomatoesException;
import com.omertron.rottentomatoesapi.model.AbstractJsonMapping;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yamj.api.common.exception.ApiExceptionType;
import org.yamj.api.common.http.DigestedResponse;
import org.yamj.api.common.http.DigestedResponseReader;
import org.yamj.api.common.http.UserAgentSelector;

/**
 *
 * @author Stuart
 */
public class ResponseBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseBuilder.class);
    /*
     * Constants
     */
    private static final int RETRY_DELAY_MS = 500;
    private static final int RETRY_DEFAULT_LIMIT = 5;
    private static final int HTTP_STATUS_300 = 300;
    private static final int HTTP_STATUS_500 = 500;

    /*
     * Jackson JSON configuration
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String DEFAULT_CHARSET = "UTF-8";
    private final Charset charset = Charset.forName(DEFAULT_CHARSET);
    /*
     * Retry settings
     */
    private long retryDelay = RETRY_DELAY_MS;
    private int retryLimit = RETRY_DEFAULT_LIMIT;
    /*
     * HTTP Client for web requests
     */
    private final CloseableHttpClient httpClient;

    public ResponseBuilder(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Set the delay time between API retries when the account is over it's
     * limit
     *
     * @param retryDelay milliseconds to delay for, default is 500ms
     */
    public void setRetryDelay(long retryDelay) {
        if (retryDelay > RETRY_DELAY_MS) {
            this.retryDelay = retryDelay;
        }
    }

    /**
     * Number of times to retry the API call when the account limit is hit.
     *
     * Once this limit is hit an exception is thrown.
     *
     * @param retryLimit Number of retries, default is 5
     */
    public void setRetryLimit(int retryLimit) {
        if (retryLimit > 1) {
            this.retryLimit = retryLimit;
        }
    }

    /**
     * Get the wrapper for the passed properties
     *
     * Will retry up to retry limit
     *
     * @param <T>
     * @param clazz
     * @param properties
     * @return
     * @throws RottenTomatoesException
     */
    public <T extends AbstractJsonMapping> T getResponse(Class<T> clazz, Map<String, String> properties) throws RottenTomatoesException {
        String url = ApiBuilder.create(properties);
        try {
            T wrapper = clazz.cast(MAPPER.readValue(getContent(url), clazz));
            int retry = 1;

            while (!wrapper.isValid() && "Account Over Queries Per Second Limit".equalsIgnoreCase(wrapper.getError()) && retry <= retryLimit) {
                LOG.trace("Account over queries limit, waiting for {}ms.", retryDelay * retry);
                sleeper(retry++);
                wrapper = MAPPER.readValue(getContent(url), clazz);
            }

            if (wrapper.isValid()) {
                return wrapper;
            } else {
                throw new RottenTomatoesException(ApiExceptionType.MAPPING_FAILED, wrapper.getError());
            }
        } catch (IOException ex) {
            throw new RottenTomatoesException(ApiExceptionType.MAPPING_FAILED, "Failed to map response", url);
        }
    }

    /**
     * Get the content from a string, decoding it if it is in GZIP format
     *
     * @param urlString
     * @return
     * @throws RottenTomatoesException
     */
    private String getContent(String url) throws RottenTomatoesException {
        LOG.trace("Requesting: {}", url);
        try {
            final HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("accept", "application/json");
            httpGet.addHeader(HTTP.USER_AGENT, UserAgentSelector.randomUserAgent());

            final DigestedResponse response = DigestedResponseReader.requestContent(httpClient, httpGet, charset);

            if (response.getStatusCode() >= HTTP_STATUS_500) {
                throw new RottenTomatoesException(ApiExceptionType.HTTP_503_ERROR, response.getContent(), response.getStatusCode(), url);
            } else if (response.getStatusCode() >= HTTP_STATUS_300) {
                throw new RottenTomatoesException(ApiExceptionType.HTTP_404_ERROR, response.getContent(), response.getStatusCode(), url);
            }

            return response.getContent();
        } catch (IOException ex) {
            throw new RottenTomatoesException(ApiExceptionType.CONNECTION_ERROR, "Error retrieving URL", url, ex);
        }
    }

    /**
     * Close a BufferedReader
     *
     * @param br
     */
    private void close(BufferedReader br) {
        if (br != null) {
            try {
                br.close();
            } catch (IOException ex) {
                LOG.trace("Failed to close BufferedReader", ex);
            }
        }
    }

    /**
     * Close an InputStreamReader
     *
     * @param isr
     */
    private void close(InputStreamReader isr) {
        if (isr != null) {
            try {
                isr.close();
            } catch (IOException ex) {
                LOG.trace("Failed to close InputStreamReader", ex);
            }
        }
    }

    /**
     * Close a GZIPInputStream
     *
     * @param gzis
     */
    private void close(GZIPInputStream gzis) {
        if (gzis != null) {
            try {
                gzis.close();
            } catch (IOException ex) {
                LOG.trace("Failed to close GZIPInputStream", ex);
            }
        }
    }

    /**
     * Sleep for a short period
     *
     * @param count
     */
    private void sleeper(int count) {
        try {
            Thread.sleep(retryDelay * (long) count);
        } catch (InterruptedException ex) {
            LOG.trace("Sleep interrupted", ex);
        }
    }

}
