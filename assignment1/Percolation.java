import edu.princeton.cs.algs4.WeightedQuickUnionUF;

// N nby N grid with two virtual sites
// its possible to connect open sites using the union command
public class Percolation {

    private WeightedQuickUnionUF perc_quf;
    private WeightedQuickUnionUF full_quf;
    private final int grid_length;
    private boolean[] open_cells; //keep track of whether site is open or not (0 - blocked, 1-open)
    private final int virtual_top_site_idx;
    private final int virtual_bottom_site_idx;
    private int number_open_sites;

    // creates n-by-n grid, with all sites initially blocked (none of the sites are connected)
    public Percolation(int N) {

        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        grid_length = N;
        //define a grid of N by N + virtual sites (0 to N*N+1)
        perc_quf = new WeightedQuickUnionUF((N * N) + 2);
        open_cells = new boolean[(N * N) + 2];
        virtual_top_site_idx = N * N;
        virtual_bottom_site_idx = N * N + 1;
        //build connections between virtual site and the respective rows
        //top virtual site - N*N idx, bottom - N*N+1, idx
        //virtual sites are open
        open_cells[virtual_top_site_idx] = true;
        open_cells[virtual_bottom_site_idx] = true;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > grid_length || col < 1 || col > grid_length) {
            throw new IllegalArgumentException();
        }

        int idx = (row - 1) * grid_length + (col - 1);
        if (!isOpen(row, col)) {
            open_cells[idx] = true;
            number_open_sites++;
        }

        if (row == 1) {
            perc_quf.union(col - 1, virtual_top_site_idx);
        }

        if (row == grid_length) {
            perc_quf.union((row - 1) * grid_length + (col - 1), virtual_bottom_site_idx);
        }

        if (row > 1 && isOpen(row - 1, col)) {
            perc_quf.union((row - 2) * grid_length + (col - 1), idx);
        }
        if (row < grid_length && isOpen(row + 1, col)) {
            perc_quf.union((row) * grid_length + (col - 1), idx);
        }
        if (col > 1 && isOpen(row, col - 1)) {
            perc_quf.union((row - 1) * grid_length + (col - 2), idx);
        }
        if (col < grid_length && isOpen(row, col + 1)) {
            perc_quf.union((row - 1) * grid_length + (col), idx);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > grid_length || col < 1 || col > grid_length) {
            return false;
        }

        return open_cells[(row - 1) * grid_length + (col - 1)];
    }

    // is the site (row, col) full? (can be connected to the top row via chaining)
    public boolean isFull(int row, int col) {

        if (row < 1 || row > grid_length || col < 1 || col > grid_length) {
            throw new IllegalArgumentException();
        }
        if (!isOpen(row, col)) {
            return false;
        }

        int true_idx = perc_quf.find((row - 1) * grid_length + (col - 1));
        //check if this object is in the same connected component as the any of the top sites
        return true_idx == perc_quf.find(virtual_top_site_idx);

    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return number_open_sites;
    }

    // does the system percolate?
    public boolean percolates() {
        //a ste on the bottom row is a full site
        return perc_quf.find(virtual_top_site_idx) == perc_quf.find(virtual_bottom_site_idx);
    }

    // test client (optional)
    public static void main(String[] args) {
        //for testing the client only
    }
}
