import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * A class for analyzing the probability of percolation in a system. It's constructor takes in a grid size,
 * a number of tests to run, and whether or not to display a visual representation of the system being tested.
 * Tracks input sequences to be used for debugging if necessary. Uses the PercolationVisualizer class to display
 * a nice representation of the system if desired.
 */
public class PercolationStats {
    /** The scaling factor for calculating the confidence interval. */
    private static final double CONFIDENCE_SCALING_FACTOR = 1.96;

    /** An array to store the percolation threshold of each test. */
    private double[] thresholds;
    /** The number of tests to run. */
    private int T;
    /** The size of the grid to be used for tests. */
    private int N;
    
    public static void Main(String[] args) {
        if (args.length < 2) {
            System.out.printf("Invalid arguments:\nUsage:\n\tjava StatsTest [grid size] [number of tests] (-show)\n");
        }
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(N, T);
        System.out.printf("mean = %f\nstddev = %f\n95%% confidence interval = %f, %f\n", ps.mean(), ps.stddev(), ps.confidenceLo(), ps.confidenceHi());
    }

    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("Invalid args to PercolationStats Constructor");   
        }
        thresholds = new double[T];
        this.T = T;
        this.N = N;
        runTests();
    }

    private void runTests() {
        // run T tests
        for (int currentTestNumber = 0; currentTestNumber < T; ++currentTestNumber) {
            // initialize required components for test
            Percolation test = new Percolation(N);
            // open random sites until the system percolates
            while (!test.percolates()) {
                int randomRow = StdRandom.uniform(N);
                int randomCol = StdRandom.uniform(N);
                test.open(randomRow, randomCol);
            }
            // system has percolated, update thresholds tracker
            thresholds[currentTestNumber] = (double)test.numberOfOpenSites() / (N * N);
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
    public double confidenceLo() {
        return mean() - ((CONFIDENCE_SCALING_FACTOR * stddev()) / Math.sqrt(T));
    }

    /** High endpoint of 95% confidence interval. */
    public double confidenceHi() {
        return mean() + ((CONFIDENCE_SCALING_FACTOR * stddev()) / Math.sqrt(T));
    }
}
