package org.moviejukebox.rottentomatoes;

import java.util.HashSet;

import junit.framework.TestCase;

import com.moviejukebox.rottentomatoes.RottenTomatoes;
import com.moviejukebox.rottentomatoes.model.Cast;
import com.moviejukebox.rottentomatoes.model.Link;
import com.moviejukebox.rottentomatoes.model.Movie;


public class Test extends TestCase {
    public RottenTomatoes rt = new RottenTomatoes("rnt8xak564a8sxkts5xkqj5z");

    public Test(String name) {
        super(name);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void NOtestGetLists() {
        System.out.println("Starting >>" + this.getName() + "<<");
        
        HashSet<Link> response = rt.getLists();
        assertEquals(2, response.size());
        
        System.out.println("Finished >>" + this.getName() + "<<");
    }
    
    public void NOtestMoviesSearch() {
        System.out.println("Starting >>" + this.getName() + "<<");

        HashSet<Movie> response = rt.moviesSearch("Blade Runner");
        assertEquals(3, response.size());
        
        System.out.println("Finished >>" + this.getName() + "<<");
    }

    public void NOtestListsDirectory() {
        System.out.println("Starting >>" + this.getName() + "<<");
        
        HashSet<Link> response = rt.listsDirectory();
        for (Link link : response) {
            System.out.println(link.getLinkType() + ": " + link.getLinkUrl());
        }
        assertEquals(2, response.size());
        
        
        System.out.println("Finished >>" + this.getName() + "<<");
    }
    
    public void NOtestMovieListsDirectory() {
        System.out.println("Starting >>" + this.getName() + "<<");
        
        HashSet<Link> response = rt.movieListsDirectory();
        for (Link link : response) {
            System.out.println(link.getLinkType() + ": " + link.getLinkUrl());
        }
        
        assertEquals(4, response.size());
        System.out.println("Finished >>" + this.getName() + "<<");
    }
    
    public void NOtestDvdListsDirectory() {
        System.out.println("Starting >>" + this.getName() + "<<");
        
        HashSet<Link> response = rt.dvdListsDirectory();
        for (Link link : response) {
            System.out.println(link.getLinkType() + ": " + link.getLinkUrl());
        }
        
        assertEquals(1, response.size());
        
        System.out.println("Finished >>" + this.getName() + "<<");
    }

    public void NOtestOpeningMovies() {
        System.out.println("Starting >>" + this.getName() + "<<");
        
        int maxMovies = 10;
        
        HashSet<Movie> response = rt.openingMovies(maxMovies);
        for (Movie movie : response) {
            System.out.println(movie.toString());
        }
        
        assertTrue(response.size() <= maxMovies);
        
        System.out.println("Finished >>" + this.getName() + "<<");
    }

    public void NOtestUpcomingMovies() {
        System.out.println("Starting >>" + this.getName() + "<<");
        
        int maxMovies = 10;
        
        HashSet<Movie> response = rt.upcomingMovies(maxMovies);
        for (Movie movie : response) {
            System.out.println(movie.toString());
        }
        
        assertTrue((response.size() > 0) && (response.size() <= maxMovies));
        
        System.out.println("Finished >>" + this.getName() + "<<");
    }

    public void NOtestNewReleaseDvds() {
        System.out.println("Starting >>" + this.getName() + "<<");
        
        int maxMovies = 10;
        
        HashSet<Movie> response = rt.newReleaseDvds(maxMovies);
        for (Movie movie : response) {
            System.out.println(movie.toString());
        }
        
        assertTrue((response.size() > 0) && (response.size() <= maxMovies));
        
        System.out.println("Finished >>" + this.getName() + "<<");
    }

    public void NOtestMovieInfo() {
        System.out.println("Starting >>" + this.getName() + "<<");
        
        Movie movie = rt.movieInfo(12886);
        System.out.println(movie.toString());
        
        assertEquals("Blade Runner", movie.getTitle());
        
        System.out.println("Finished >>" + this.getName() + "<<");
    }
    
    public void testMovieCast() {
        System.out.println("Starting >>" + this.getName() + "<<");
        
        HashSet<Cast> castList = rt.movieCast(12886);
        assertTrue(castList.size() > 0);
        
        System.out.println("Finished >>" + this.getName() + "<<");
    }

    public void testMovieReviews() {
        System.out.println("Starting >>" + this.getName() + "<<");
        
        Movie movie = rt.movieReviews(12886, "all");
        System.out.println(movie.toString());
        
        assertEquals("Blade Runner", movie.getTitle());
        
        System.out.println("Finished >>" + this.getName() + "<<");
    }

}
