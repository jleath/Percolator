package Percolation;

import java.util.HashMap;
import java.util.Map;

/**
 * Simulates percolation. A percolation object represents a 2 dimensional collection of Site objects.
 * Each site is referred to as "open" when it is capable of connecting to other sites. A convenient metaphor
 * is the ground with a known width and depth arranged as a grid beneath a body of water.
 * We are able to "open" boxes (what I have named Sites) of this grid to allow the water above to flow through them.
 * The purpose of the Percolation class is to represent this grid of sites. The client of Percolation can then use it
 * to analyze statistical properties of the physical phenomena known as percolation.
 */
public class Percolation {
    /*
     * An inner class for representing a single site in order to track whether the site is
     * open and/or full. By default, each site is not open and therefore not full.
     */
    private class Site {
        int col;
        int row;
        boolean isOpen;
        boolean isFull;

        Site(int c, int r) {
            col = c;
            row = r;
            isOpen = false;
            isFull = false;
        }
    }

    /**
     * A Quick union find object for determining whether given sites are connected.
     */
    private QuickUnionPathCompressionUF connections;
    private Map<Integer, Site> siteMapping;
    private int N;
    private int numOpenSites;

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N less than or equal to 0");
        }
        this.N = N;
        connections = new QuickUnionPathCompressionUF(N * N);
        siteMapping = buildXyTo1DMapping(N);
        numOpenSites = 0;
    }

    private void checkBounds(int arg1, int arg2) {
        if (arg1 >= N || arg1 < 0) {
            throw new IndexOutOfBoundsException("Invalid index for grid of size " + N + ": " + arg1);
        }
        if (arg2 >= N || arg2 < 0) {
            throw new IndexOutOfBoundsException("Invalid index for grid of size " + N + ": " + arg2);
        }
    }

    /*
     * Build a mapping from integers to Site objects so that we can more easily locate
     * the site at a given X, Y position.
     */
    private Map buildXyTo1DMapping(int n) {
        HashMap<Integer, Site> result = new HashMap<>();
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                result.put(xyTo1D(i, j), new Site(i, j));
            }
        }
        return result;
    }

    /**
     * Determine whether a site has been opened or not.
     *
     * @param row The Y position of the site to check
     * @param col The X position of the site to check
     * @return true if the site found at position (X, Y) has been opened.
     */
    public boolean isOpen(int row, int col) {
        checkBounds(row, col);
        return siteMapping.get(xyTo1D(row, col)).isOpen;
    }

    /**
     * Determine whether a site has a connection to another site that is "full".
     * All open sites with an X position of 0 are considered full.
     *
     * @param row The Y position of the site to check
     * @param col The X position of the site to check
     * @return true if the site found at position (X, Y) is full.
     */
    public boolean isFull(int row, int col) {
        checkBounds(row, col);
        int siteId = xyTo1D(row, col);
        Site currSite = siteMapping.get(siteId);
        int parentId = connections.find(siteId);
        if (currSite.isFull) {
            siteMapping.get(parentId).isFull = true;
            return true;
        }
        currSite.isFull = siteMapping.get(parentId).isFull;
        return currSite.isFull;
    }

    /**
     * @return the number of sites that have been opened.
     */
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    /**
     * Opens the site at position ROW, COL. Checks for potential connections in the positions
     * to the top, bottom, left and right of the given site. If potential connections exits,
     * connects those sites. If any of the sites connections are full, the given site is set to full.
     * Do nothing if the site has already been opened.
     *
     * @param row The Y position of the site to process
     * @param col The X position of the site to process
     */
    public void open(int row, int col) {
        checkBounds(row, col);
        int siteId = xyTo1D(row, col);
        Site currSite = siteMapping.get(siteId);
        if (currSite.isOpen) {
            //System.out.println(row + " " + col + " already opened");
            return;
        }
        numOpenSites += 1;
        currSite.isOpen = true;
        if (isInTopRow(row)) {
            currSite.isFull = true;
        }
        connectAdjacentSites(row, col);
        isFull(row, col);
    }

    private boolean isInTopRow(int row) {
        return row == 0;
    }

    private void connectAdjacentSites(int row, int col) {
        int left = xyTo1D(row, col - 1);
        int right = xyTo1D(row, col + 1);
        int above = xyTo1D(row - 1, col);
        int below = xyTo1D(row + 1, col);
        int curr = xyTo1D(row, col);
        if (row > 0 && siteMapping.get(above).isOpen) {
            connections.union(curr, above);
        }
        if (row < N - 1 && siteMapping.get(below).isOpen) {
            connections.union(curr, below);
        }
        if (col > 0 && siteMapping.get(left).isOpen) {
            connections.union(curr, left);
        }
        if (col < N - 1 && siteMapping.get(right).isOpen) {
            connections.union(curr, right);
        }
    }

    /**
     * Determines whether there is a path from the topmost row of the grid to the
     * bottommost row of the grid.
     *
     * @return true if there is a path from the topmost row of the grid to the bottommost row of the grid, else false.
     */
    public boolean percolates() {
        int bottomRow = N - 1;
        int currColumn = 0;
        while (currColumn < N) {
            if (isFull(bottomRow, currColumn)) {
                return true;
            }
            currColumn += 1;
        }
        return false;
    }

    /*
     * Convert a 2-dimensional coordinate to a single integer. Ex. Given a N by N grid where N=5,
     * xyTo1D(3, 4) equals (3 * N) + 4.
     */
    private int xyTo1D(int row, int col) {
        return (row * N) + col;
    }
}
