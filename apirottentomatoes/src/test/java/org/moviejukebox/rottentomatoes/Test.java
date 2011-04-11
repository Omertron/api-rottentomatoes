package org.moviejukebox.rottentomatoes;

import java.util.HashSet;

import junit.framework.TestCase;

import com.moviejukebox.rottentomatoes.RottenTomatoes;
import com.moviejukebox.rottentomatoes.model.Link;


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

    public void testMovieList() {

        System.out.println("Starting");
        HashSet<Link> response = rt.getLists();
        
        for (Link link : response) {
            System.out.println(link.getType() + ": " + link.getUrl());
        }
        
        System.out.println("Finished");
    }
    
    public void testMovieSearch() {
        rt.moviesSearch("Blade Runner");
    }
    
}
