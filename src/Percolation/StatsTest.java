package Percolation;

/**
 * A client for the PercolationStats class. This program takes the width of a grid to test,
 * the number of tests to run and a flag to dictate whether a visual representation of the model
 * is shown. This flag should be "-show", any other flag or the absence of one will result in no
 * visual display.
 * <p>
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
        System.out.printf("Mean: %.3f\nStandard Deviation: %.3f\nConfidence Low: %.3f\nConfidence High: %.3f\n",
                           ps.mean(), ps.stddev(), ps.confidenceLow(), ps.confidenceHigh());
        System.out.println("Failures: " + ps.failures());
        System.out.printf("Average site vacancy at time of percolation: %.3f%%\n", siteVacancy(ps, N));
    }

    /**
     * @param ps The PercolationStats system representing the system we are modeling.
     * @param gridSize The size of the system we are modeling.
     * @return the percentage of sites that were open at the time of percolation in the PercolationStats system
     */
    private static double siteVacancy(PercolationStats ps, int gridSize) {
        return (Math.pow(gridSize, 2) - ps.mean()) / (Math.pow(gridSize, 2) * 100.0);
    }
}
