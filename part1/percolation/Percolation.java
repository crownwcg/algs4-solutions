import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 *  The {@code Percolation} class provides a method for modeling
 *  percolation experiment in coursera algorithms course
 *
 *  @author Chenggang Wang
 */
public class Percolation {
    private boolean[] openStates;               // an array to store whether the sites are open
    private final int side;                     // the number of side of the system
    private int numberOfOpenSites;              // the number of open sites
    private final WeightedQuickUnionUF uf;      // Union-Find algorithms
    private final WeightedQuickUnionUF backwash;

    /**
     * Create n-by-n grid, with all sites blocked
     *
     * @param n grid size
     */
    public Percolation(int n) {
        // when n is not positive, throw IllegalArgumentException
        if (n < 1)
            throw new IllegalArgumentException("Illegal argument");
        // initiate the algorithms, 0 for virtual top and n * n for virtual bottom
        uf = new WeightedQuickUnionUF(n * n + 2);
        backwash = new WeightedQuickUnionUF(n * n + 1);
        // initiate side number
        side = n;
        // initiate openStates
        openStates = new boolean[n * n + 2];
        openStates[0] = true;
        openStates[side * side + 1] = true;
    }

    /**
     * Check whether the inputing row and/or col are out of bounds
     *
     * @param row row
     * @param col col
     */
    private void validate(int row, int col) {
        if (row < 1 || row > side || col < 1 || col > side)
            throw new IllegalArgumentException();
    }

    /**
     * open site (row, col) if it is not open already
     *
     * @param row row
     * @param col col
     */
    public    void open(int row, int col) {
        validate(row, col);
        if (isOpen(row, col))
            return;
        int index = getIndex(row, col);
        // top row
        if (row == 1) {
            uf.union(index, 0);
            backwash.union(index, 0);
        }
        // bottom row
        if (row == side)
            uf.union(index, side * side + 1);
        // union upper site
        if (row > 1 && isOpen(row - 1, col)) {
            uf.union(index, getIndex(row - 1, col));
            backwash.union(index, getIndex(row - 1, col));
        }
        // union below site
        if (row < side && isOpen(row + 1, col)) {
            uf.union(index, getIndex(row + 1, col));
            backwash.union(index, getIndex(row + 1, col));
        }
        // union left site
        if (col > 1 && isOpen(row, col - 1)) {
            uf.union(index, getIndex(row, col - 1));
            backwash.union(index, getIndex(row, col - 1));
        }
        // union right site
        if (col < side && isOpen(row, col + 1)) {
            uf.union(index, getIndex(row, col + 1));
            backwash.union(index, getIndex(row, col + 1));
        }
        // change the state of the site
        openStates[getIndex(row, col)] = true;
        numberOfOpenSites++;
    }

    /**
     * Check whether site (row, col) open
     *
     * @param row row
     * @param col col
     * @return true if the site is open and false otherwise
     */
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return openStates[getIndex(row, col)];
    }

    private int getIndex(int row, int col) {
        validate(row, col);
        return side * (row - 1) + col;
    }

    /**
     * Check whether site (row, col) full
     *
     * @param row row
     * @param col col
     * @return true if the site is full and false otherwise
     */
    public boolean isFull(int row, int col) {
        validate(row, col);
        if (isOpen(row, col)) {
            if (backwash.connected(getIndex(row, col), 0))
                return true;
        }
        return false;
    }

    /**
     * @return the number of sites that are open
     */
    public     int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    /**
     * @return true if the system percolates and false otherwise
     */
    public boolean percolates() {
        return uf.connected(0, side * side + 1);
    }

    // Unit test for class Percolation
    public static void main(String[] args) {
        int n = 2;
        Percolation p = new Percolation(n);
        p.open(1, 1);
        p.open(2, 2);
        System.out.println(p.percolates());
        System.out.println(p.isOpen(1, 1));
        System.out.println(p.isFull(1, 1));
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                System.out.println(p.isOpen(i, j));
            }
        }
        System.out.println(p.uf.connected(1, 4));
    }
}
