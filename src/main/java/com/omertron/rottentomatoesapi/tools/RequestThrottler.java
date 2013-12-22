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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that throttles requests.
 *
 * Ensures that the StartRequest cannot be called more than a given amount of time in any given interval.
 *
 * StartRequest blocks until this requirement is satisfied.
 *
 * TryStartRequest returns 0 if the request was cleared, or a non-0 number of millisecond to sleep before the next attempt.
 *
 * Simple usage, 10 requests per second:
 *
 * Throttler t = new Throttler(10, 1000); ... ServiceRequest(Throttler t, ...) { t.StartRequest(); .. do work ..
 *
 * @author sergey@solyanik.com (Sergey Solyanik) http://1-800-magic.blogspot.com/2008/02/throttling-requests-java.html
 *
 */
public class RequestThrottler {

    private static final Logger LOG = LoggerFactory.getLogger(RequestThrottler.class);
    /**
     * The interval within we're ensuring max number of calls.
     */
    private final long _interval;

    /**
     * The maximum number of calls that can be made within the interval.
     */
    private final int _maxCalls;

    /**
     * Previous calls within the interval.
     */
    private final long[] _ticks;

    /**
     * Available element at the insertion point (back of the queue).
     */
    private int _tickNext;

    /**
     * Element at the removal point (front of the queue).
     */
    private int _tickLast;

    /**
     * the time when the last expired request occurred. might be used to auto disable some services all together
     */
    private long _lastExpiredMaxWait = 0;

    /**
     * Constructor.
     *
     * @param maxCalls Max number of calls that can be made within the interval.
     * @param interval The interval.
     */
    public RequestThrottler(final int maxCalls, final long interval) {
        if (maxCalls < 1) {
            throw new IllegalArgumentException("maxCalls must be >=1, was " + maxCalls);
        }
        if (interval < 1) {
            throw new IllegalArgumentException("interval must be >=1, was " + interval);
        }
        _interval = interval;
        _maxCalls = maxCalls + 1;
        _ticks = new long[_maxCalls];
        _tickLast = _tickNext = 0;
    }

    /**
     * Returns the next element in the queue.
     *
     * @param index The element for which to compute the next.
     * @return
     */
    private int next(int index) {
        index += 1;
        return index < _maxCalls ? index : 0;
    }

    /**
     * Attempts to clear the request.
     *
     * @return Returns 0 if successful, or a time hint (ms) in which we should attempt to clear it again.
     */
    public synchronized long tryStartRequest() {
        long result = 0;
        final long now = System.currentTimeMillis();
        while (_tickLast != _tickNext) {
            if (now - _ticks[_tickLast] < _interval) {
                break;
            }
            _tickLast = next(_tickLast);
        }

        final int next = next(_tickNext);
        if (next != _tickLast) {
            _ticks[_tickNext] = now;
            _tickNext = next;
        } else {
            result = _interval - (now - _ticks[_tickLast]);
        }
        return result;
    }

    /**
     * Clears the request. Blocks until the request can execute.
     */
    public void startRequest() {
        startRequest(Integer.MAX_VALUE);
    }

    /**
     * Clears the request. Blocks until the request can execute or until maxWait would be exceeded.
     *
     * @param maxWait
     * @return true if successful or false if request should not execute
     */
    public boolean startRequest(final int maxWait) {
        long sleep;
        long total = 0;
        while ((sleep = tryStartRequest()) > 0) {
            if (maxWait > 0 && (total += sleep) > maxWait) {
                _lastExpiredMaxWait = System.currentTimeMillis();
                return false;
            } else {
                try {
                    Thread.sleep(sleep);
                } catch (final InterruptedException ex) {
                    LOG.trace("RequestThrottler interrupted", ex);
                }
            }
        }
        return true;
    }

    public long getLastExpiredMaxWait() {
        return _lastExpiredMaxWait;
    }

}
