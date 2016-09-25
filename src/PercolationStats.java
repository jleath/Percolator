
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

import java.awt.*;

/**
 * Created by jaleath on 9/25/16.
 */
public class PercolationStats {
    private int[] thresholds;
    private int T;
    private int N;
    private double mean;
    private double stddev;
    private double confidenceLow;
    private double confidenceHigh;
    private boolean display;


    public PercolationStats(int N, int T, boolean display) {
        thresholds = new int[T];
        this.T = T;
        this.N = N;
        this.display = display;
        runTests();
        mean = mean();
        stddev = stddev();
        confidenceHigh = confidenceHigh();
        confidenceLow = confidenceLow();
    }

    private void runTests() {
        if (display) {
            StdDraw.show(0);
        }
        for (int i = 0; i < T; ++i) {
            System.out.println("Running test " + i);
            Percolation test = new Percolation(N);
            System.out.println("Test constructed");
            while (!test.percolates()) {
                if (display) {
                    draw(test, N);
                    StdDraw.show(0);
                }
                int randomRow = StdRandom.uniform(N);
                int randomCol = StdRandom.uniform(N);
                //System.out.println("Opening: " + randomRow + ", " + randomCol);
                test.open(randomRow, randomCol);
                if (display) {
                    draw(test, N);
                    StdDraw.show(0);
                }
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
        return mean - ((1.96 * stddev) / Math.sqrt(T));
    }

    /** High endpoint of 95% confidence interval. */
    public double confidenceHigh() {
        return mean + ((1.96 * stddev) / Math.sqrt(T));
    }

    public static void draw(Percolation perc, int N) {
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setXscale(-.05 * N, 1.05 * N);
        StdDraw.setYscale(-.05 * N, 1.05 * N);   // leave a border to write text
        StdDraw.filledSquare(N / 2.0, N / 2.0, N / 2.0);

        // draw N-by-N grid
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                if (perc.isFull(row, col)) {
                    StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
                } else if (perc.isOpen(row, col)) {
                    StdDraw.setPenColor(StdDraw.WHITE);
                } else {
                    StdDraw.setPenColor(StdDraw.BLACK);
                }
                StdDraw.filledSquare(col + 0.5, N - row - 0.5, 0.45);
            }
        }

        // write status text
        StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 12));
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(.25 * N, -N * .025, perc.numberOfOpenSites() + " open sites");
        if (perc.percolates()) {
            StdDraw.text(.75 * N, -N * .025, "percolates");
        } else {
            StdDraw.text(.75 * N, -N * .025, "does not percolate");
        }

    }
}
