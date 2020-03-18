import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private Percolation percolation;
    private final double number_of_trials;
    private final double mean;
    private final double std_dev;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        //check input against bounds
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        // initialize array to keep track of results from monte carlo
        double[] number_of_open_sites_results = new double[trials];
        number_of_trials = trials;

        double frac_open_sites;
        int row, col;
        //while the system does not percolate, generate monte carlo samples
        for (int i = 0; i < trials; i++) {
            percolation = new Percolation(n);
            while (!percolation.percolates()) {
                row = StdRandom.uniform(1, n + 1);
                col = StdRandom.uniform(1, n + 1);
                if (!percolation.isOpen(row, col)) {
                    percolation.open(row, col);
                }
            }

            frac_open_sites = percolation.numberOfOpenSites() / ((double) n * n);
            number_of_open_sites_results[i] = frac_open_sites;
        }

        mean = StdStats.mean(number_of_open_sites_results);
        std_dev = StdStats.stddev(number_of_open_sites_results);

    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return std_dev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return (mean - (1.96 * std_dev / Math.sqrt(number_of_trials)));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return (mean + (1.96 * std_dev / Math.sqrt(number_of_trials)));
    }

    public static void main(String[] args)        // test client (described below)
    {
        PercolationStats ps = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        String confidence = ps.confidenceLo() + ", " + ps.confidenceHi();
        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println("95% confidence interval = " + confidence);
    }

}
