package Percolation;

/**
 * A client for the PercolationStats class. This program takes the width of a grid to test,
 * the number of tests to run and a flag to dictate whether a visual representation of the model
 * is shown. This flag should be "-show", any other flag or the absence of one will result in no
 * visual display.
 *
 * Uses the PercolationStats class to print relevant information regarding the probability of percolation.
 */
public class StatsTest {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.printf("Invalid arguments:\nUsage:\n\tjava StatsTest [grid size] [number of tests] (-show)\n");
        }
        boolean display = false;
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        if (args.length == 3 && args[2].equals("-show")) {
            display = true;
        }
        System.out.println("Running " + T + " tests of size " + N);
        PercolationStats ps = new PercolationStats(N, T, display);
        System.out.println("Mean: " + ps.mean());
        System.out.println("Standard Deviation: " + ps.stddev());
        System.out.println("Confidence High: " + ps.confidenceHigh());
        System.out.println("Confidence Low: " + ps.confidenceLow());
        System.out.println("Failures: " + ps.failures());
        System.out.println("Average site vacancy at time of percolation: " + (((N * N) - ps.mean()) / (N * N) * 100.0) + "%");
    }
}
