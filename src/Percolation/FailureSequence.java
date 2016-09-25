package Percolation;

import java.util.ArrayList;

/**
 * A class that serves as a record of inputs for debugging. Tracks the number of failed operations
 * during a test and prints a sequence of input commands to the standard output upon request.
 */
public class FailureSequence {
    /** A container for Pair objects to represent the input. */
    private ArrayList<Pair> inputs;
    /** The number of repeated failed operations of a test. */
    private int failedOps;
    /** The size of the grid for the tests this object tracks. */
    private int gridSize;

    public FailureSequence(int N) {
        inputs = new ArrayList<>();
        failedOps = 0;
        gridSize = N;
    }

    /**
     * An inner class for conveniently representing inputs. Very simple, only needs to store the
     * two components of an input operation (row and column) and provide a simple string representation.
     */
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

    /**
     * Increment the failedOps value, returns the failedOps value for a somewhat convenient fluent interface.
     *
     * @return the number of failed operations caused by the current test.
     */
    public int recordFailedOp() {
        failedOps += 1;
        return failedOps;
    }

    /**
     * Set the failedOps count to 0.
     */
    public void resetFailedOps() {
        failedOps = 0;
    }

    /**
     * Record an input operation.
     *
     * @param x the first argument of the command.
     * @param y the second argument of the command.
     */
    public void recordInput(int x, int y) {
        inputs.add(new Pair(x, y));
    }

    /**
     * Clear the input record.
     */
    public void resetInput() {
        inputs.clear();
        resetFailedOps();
    }

    /**
     * Outputs the sequence of inputs that led to a failed test in the same format as the files that
     * the PercolationVisualizer can take. Useful for debugging.
     */
    public void recordFailedTest() {
        System.out.println(gridSize);
        for (int i = 0; i < inputs.size(); ++i) {
            System.out.println(inputs.get(i).x + " " + inputs.get(i).y);
        }
    }
}
