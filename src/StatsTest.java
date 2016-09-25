
/**
 * Created by jaleath on 9/25/16.
 */
public class StatsTest {
    public static void main(String[] args) {
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
