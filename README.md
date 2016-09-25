Modeling percolation with test suites for visualizing and analyzing the value of the percolation threshold using Monte
Carlo simulation.

Uses a disjoint set to model a grid of sites. A site is considered open if it can be connected to another site.
A site is considered full if there is a path of open sites to the top row of the grid. Once there is a path of full sites
from the topmost row to the bottommost row, the system has percolated.

A series of tests is run, with an optional visual display of the system percolating, in order to determine the average number
of open sites before a system percolates. This average is then used to calculate the typical ratio of vacant sites to total
sites that allows a system to percolate.

The Percolation package also includes a class that can process a predesigned sequence of site open operations. This class can be used
for debugging as well as experimentation. The StatsTest client tracks the random sites it opens and upon a failed test will
dump those operations to standard outputs in a format that can be accepted by the PercolationVisualizer class for inspection.
A test is considered failed once it has attempted to open 50 sites that have already been opened in a row.

To run the program compile and execute StatsTest as follows:

java StatsTest [<gridSize>]
               [<number of tests to run>]
               [-show <optional display flag>]

Dependencies:
    The algs4 library from Princeton. Uses the StdDraw class.
    
The QuickUnionPathCompressionUF class was created by Robert Sedgewick and Kevin Wayne at Princeton.
