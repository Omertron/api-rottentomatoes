/*
 *      Copyright (c) 2004-2014 Stuart Boston
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
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yamj.api.common.http.CommonHttpClient;

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
    private static final int RETRY_LIMIT = 5;

    /*
     * Jackson JSON configuration
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String ENCODING_UTF8 = "UTF-8";
    /*
     * Retry settings
     */
    private long retryDelay = RETRY_DELAY_MS;
    private int retryLimit = RETRY_LIMIT;
    /*
     * HTTP Client for web requests
     */
    private final CommonHttpClient httpClient;

    public ResponseBuilder(CommonHttpClient httpClient) {
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
        try {
            String url = ApiBuilder.create(properties);
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
                throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, wrapper.getError());
            }
        } catch (IOException ex) {
            throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, ex);
        }
    }

    /**
     * Get the content from a string, decoding it if it is in GZIP format
     *
     * @param urlString
     * @return
     * @throws RottenTomatoesException
     */
    private String getContent(String urlString) throws RottenTomatoesException {
        StringBuilder content = new StringBuilder();
        GZIPInputStream gzis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try {
            HttpEntity entity = httpClient.requestResource(urlString);

            if (entity.getContentEncoding() != null && "gzip".equalsIgnoreCase(entity.getContentEncoding().getValue())) {
                gzis = new GZIPInputStream(entity.getContent());
                isr = new InputStreamReader(gzis, ENCODING_UTF8);
                br = new BufferedReader(isr);

                String readed = br.readLine();
                while (readed != null) {
                    content.append(readed);
                    readed = br.readLine();
                }
            } else {
                content.append(EntityUtils.toString(entity, Charset.forName(ENCODING_UTF8)));
            }
        } catch (IOException ex) {
            throw new RottenTomatoesException(RottenTomatoesException.RottenTomatoesExceptionType.MAPPING_FAILED, "Failed to read JSON data from " + urlString, ex);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    LOG.trace("Failed to close BufferedReader", ex);
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException ex) {
                    LOG.trace("Failed to close InputStreamReader", ex);
                }
            }
            if (gzis != null) {
                try {
                    gzis.close();
                } catch (IOException ex) {
                    LOG.trace("Failed to close GZIPInputStream", ex);
                }
            }
        }
        return content.toString();
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
