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

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Stuart
 */
public class RateLimitTest {

    private static final Logger LOG = LoggerFactory.getLogger(RateLimitTest.class);
    private static final String API_KEY = "rnt8xak564a8sxkts5xkqj5z";
    private static RottenTomatoesApi rt;

    @BeforeClass
    public static void setUpClass() throws RottenTomatoesException {
        TestLogger.configure("INFO");
        rt = new RottenTomatoesApi(API_KEY);
    }

    @Test
    public void rateTest() throws RottenTomatoesException {
        LOG.info("rateTest");
        for (int i = 1; i <= 100; i++) {
            LOG.info("Test #{}", i);
            rt.getBoxOffice("us", 5);
        }
    }
}
