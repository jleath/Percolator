package Percolation;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * A class for analyzing the probability of percolation in a system. It's constructor takes in a grid size,
 * a number of tests to run, and whether or not to display a visual representation of the system being tested.
 * Tracks input sequences to be used for debugging if necessary. Uses the PercolationVisualizer class to display
 * a nice representation of the system if desired.
 */
public class PercolationStats {
    /** The maximum number of operations that a test can fail before it will give up and restart. */
    private static final int MAX_FAILED_OPERATIONS = 50;
    /** The amount of time in milliseconds to pause the display upon percolation. */
    private static final int DISPLAY_PAUSE_ON_SUCCESS = 2000;
    /** The default amount of time to delay the display between each operation. */
    private static final int DEFAULT_DELAY_PAUSE = 0;
    /** The scaling factor for calculating the confidence interval. */
    private static final double CONFIDENCE_SCALING_FACTOR = 1.96;

    /** An array to store the percolation threshold of each test. */
    private int[] thresholds;
    /** The number of tests to run. */
    private int T;
    /** The size of the grid to be used for tests. */
    private int N;
    /** True if the user used the -show flag upon execution. */
    private boolean display;
    /** An object to track the inputs that led to a test failure. */
    private FailureSequence failureTracker;
    /** A count of the number of failed tests. */
    private int failures;

    public PercolationStats(int N, int T, boolean display) {
        thresholds = new int[T];
        this.T = T;
        this.N = N;
        this.display = display;
        failureTracker = new FailureSequence(N);
        failures = 0;
        runTests();
    }

    private void runTests() {
        // run T tests
        for (int currentTestNumber = 0; currentTestNumber < T; ++currentTestNumber) {
            System.out.println("Running test " + currentTestNumber);
            // initialize required components for test
            Percolation test = new Percolation(N);
            failureTracker.resetInput();
            System.out.println("Test constructed");
            // open random sites until the system percolates
            while (!test.percolates()) {
                draw(test, DEFAULT_DELAY_PAUSE);
                int randomRow = StdRandom.uniform(N);
                int randomCol = StdRandom.uniform(N);
                // record the position of the site opened to facilitate
                // reproduction of test failure
                failureTracker.recordInput(randomRow, randomCol);
                if (test.isOpen(randomRow, randomCol)) {
                    // handle a failed open operation
                    if (failureTracker.recordFailedOp() > MAX_FAILED_OPERATIONS) {
                        failureTracker.recordFailedTest();
                        failures += 1;
                        break;
                    }
                } else {
                    // handle a successful open operation
                    failureTracker.resetFailedOps();
                }
                test.open(randomRow, randomCol);
                draw(test, DEFAULT_DELAY_PAUSE);
            }
            // system has percolated, update thresholds tracker
            draw(test, DISPLAY_PAUSE_ON_SUCCESS);
            thresholds[currentTestNumber] = test.numberOfOpenSites();
            System.out.println("Test " + currentTestNumber + ": " + test.numberOfOpenSites() + " opened sites");
        }
    }

    /** Mean of percolation threshold. */
    public double mean() {
        return StdStats.mean(thresholds);
    }

    /** Standard deviation of percolation threshold. */
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    /** Low endpoint of 95% confidence interval. */
    public double confidenceLow() {
        return mean() - ((CONFIDENCE_SCALING_FACTOR * stddev()) / Math.sqrt(T));
    }

    /** High endpoint of 95% confidence interval. */
    public double confidenceHigh() {
        return mean() + ((CONFIDENCE_SCALING_FACTOR * stddev()) / Math.sqrt(T));
    }

    /** Return the number of failed tests during this run. */
    public int failures() {
        return failures;
    }

    /**
     * A method to handle interaction with the PercolationVisualizer class to clean up the code a bit.
     */
    private void draw(Percolation p, int delay) {
        if (display) {
            PercolationVisualizer.draw(p, N);
            StdDraw.show(delay);
        }
    }
}
