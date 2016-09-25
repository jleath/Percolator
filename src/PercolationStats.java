
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by jaleath on 9/25/16.
 */
public class PercolationStats {
    private int[] thresholds;
    private int T;
    private int N;
    private boolean display;
    private int failures;

    private HashMap<Integer, Pair> inputs;

    private class Pair {
        int x;
        int y;

        Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public String toString() {
            return x + " " + y;
        }
    }

    public PercolationStats(int N, int T, boolean display) {
        thresholds = new int[T];
        this.T = T;
        this.N = N;
        this.display = display;
        failures = 0;
        inputs = new HashMap<>();
        runTests();
    }

    private void runTests() {
        if (display) {
            StdDraw.show(0);
        }
        for (int i = 0; i < T; ++i) {
            int failedOps = 0;
            System.out.println("Running test " + i);
            Percolation test = new Percolation(N);
            System.out.println("Test constructed");
            int j = 0;
            while (!test.percolates()) {
                if (display) {
                    PercolationVisualizer.draw(test, N);
                    StdDraw.show(0);
                }
                int randomRow = StdRandom.uniform(N);
                int randomCol = StdRandom.uniform(N);
                inputs.put(j, new Pair(randomRow, randomCol));
                //System.out.println("Opening: " + randomRow + ", " + randomCol);
                if (test.isOpen(randomRow, randomCol)) {
                    failedOps += 1;
                    if (failedOps > 50) {
                        failures += 1;
                        dumpInputs();
                        System.exit(0);
                        break;
                    }
                } else {
                    failedOps = 0;
                }
                test.open(randomRow, randomCol);
                if (display) {
                    PercolationVisualizer.draw(test, N);
                    StdDraw.show(0);
                }
                j += 1;
            }
            if (display) {
                PercolationVisualizer.draw(test, N);
                StdDraw.show(2000);
            }
            thresholds[i] = test.numberOfOpenSites();
            System.out.println("Test " + i + ": " + test.numberOfOpenSites() + " opened sites");
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
        return mean() - ((1.96 * stddev()) / Math.sqrt(T));
    }

    /** High endpoint of 95% confidence interval. */
    public double confidenceHigh() {
        return mean() + ((1.96 * stddev()) / Math.sqrt(T));
    }

    public int failures() {
        return failures;
    }

    /** This method returns the inputs that were used in simulating percolation. Primarily used
     *  to test that the percolates() method is working correctly. I noticed that the percolates method
     *  was sometimes returning false even if all sites are open. I fixed the issue (the method that determines whether
     *  sites are full was not always being called at the correct time), but I am leaving this here because it was useful
     *  to be able to dump what has been attempted, copy it into a text file and then running it through the visualizer to
     *  analyze what is going on.
     */
    private void dumpInputs() {
        for (int i = 0; i < inputs.size(); i++) {
            System.out.println(inputs.get(i));
        }
    }
}
