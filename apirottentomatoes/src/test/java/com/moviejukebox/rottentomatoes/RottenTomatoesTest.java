/*
 *      Copyright (c) 2004-2011 YAMJ Members
 *      http://code.google.com/p/moviejukebox/people/list 
 *  
 *      Web: http://code.google.com/p/moviejukebox/
 *  
 *      This software is licensed under a Creative Commons License
 *      See this page: http://code.google.com/p/moviejukebox/wiki/License
 *  
 *      For any reuse or distribution, you must make clear to others the 
 *      license terms of this work.  
 */
package com.moviejukebox.rottentomatoes;

import java.util.Set;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.moviejukebox.rottentomatoes.model.Cast;
import com.moviejukebox.rottentomatoes.model.Link;
import com.moviejukebox.rottentomatoes.model.Movie;
import com.moviejukebox.rottentomatoes.model.Review;

public class RottenTomatoesTest extends TestCase {
    public RottenTomatoes rt;

    @Before
    public void setUp() throws Exception {
        rt = new RottenTomatoes("");
    }

    @Test
    public void testGetLists() {
        Set<Link> response = rt.getLists();
        assertEquals(2, response.size());
    }
    
    @Test
    public void testMoviesSearch() {
        Set<Movie> response = rt.moviesSearch("Blade Runner");
        assertTrue(response.size() > 0);
    }

    @Test
    public void testListsDirectory() {
        Set<Link> response = rt.listsDirectory();
        assertEquals(2, response.size());
    }
    
    @Test
    public void testMovieListsDirectory() {
        Set<Link> response = rt.movieListsDirectory();
        assertEquals(4, response.size());
    }
    
    @Test
    public void testDvdListsDirectory() {
        Set<Link> response = rt.dvdListsDirectory();
        assertTrue(response.size() > 0);
    }

    @Test
    public void testOpeningMovies() {
        int maxMovies = 10;
        Set<Movie> response = rt.openingMovies(maxMovies);
        assertTrue(response.size() <= maxMovies);
    }

    @Test
    public void testUpcomingMovies() {
        int maxMovies = 10;
        Set<Movie> response = rt.upcomingMovies(maxMovies);
        assertTrue((response.size() > 0) && (response.size() <= maxMovies));
    }

    @Test
    public void testNewReleaseDvds() {
        int maxMovies = 10;
        Set<Movie> response = rt.newReleaseDvds(maxMovies);
        assertTrue((response.size() > 0) && (response.size() <= maxMovies));
    }

    @Test
    public void testMovieInfo() {
        Movie movie = rt.movieInfo(12886);
        assertEquals("Blade Runner", movie.getTitle());
    }
    
    @Test
    public void testMovieCast() {
        Set<Cast> castList = rt.movieCast(12886);
        assertTrue(castList.size() > 0);
    }

    @Test
    public void testMovieReviews() {
        Set<Review> reviewList = rt.movieReviews(12886, Review.ReviewType.all);
        assertTrue(reviewList.size() > 0);
    }
    
}
