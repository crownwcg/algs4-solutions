import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 *  The {@code PercolationStats} class provides a method To
 *  estimate the percolation threshold
 *
 *  @author Chenggang Wang
 */
public class PercolationStats {
    private final double[] attemps;
    private final double mean;
    private final double stddev;

    /**
     * Perform trials independent experiments on an n-by-n grid
     *
     * @param trial independent experiments times
     * @param n grid size
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("IllegalArgumentException");
        attemps = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            int steps = 0;
            while (!p.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                if (!p.isOpen(row, col)) {
                    p.open(row, col);
                    steps++;
                }
            }
            attemps[i] = (double) steps / (n * n);
        }

        mean = StdStats.mean(attemps);
        stddev = StdStats.stddev(attemps);;
    }

    /**
     * @return sample mean of percolation threshold
     */
    public double mean() {
        return mean;
    }

    /**
     * @return sample standard deviation of percolation threshold
     */
    public double stddev() {
        return stddev;
    }

    /**
     * @return low  endpoint of 95% confidence interval
     */
    public double confidenceLo() {
        return mean - 1.96 * stddev / Math.sqrt(attemps.length);
    }

    /**
     * @return high endpoint of 95% confidence interval
     */
    public double confidenceHi() {
        return mean + 1.96 * stddev / Math.sqrt(attemps.length);
    }

    // Unit test for class PercolationStats
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats p = new PercolationStats(n, t);
        System.out.println("mean = " + p.mean());
        System.out.println("stddev = " + p.stddev());
        System.out.println("95% confidence interval = " + p.confidenceLo() + ", " + p.confidenceHi());
    }
}
