/* *****************************************************************************
 *  Name: Max Belushkin
 *  Date: 15.05.2020
 *  Description: Coursera Algorithms first week graded assignment from Princeton University
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.HashSet;
import java.util.Set;
public class Percolation {

    private int n;
    private int[][] neighbours = new int[][] {{-1,0}, {1, 0}, {0, -1}, {0, 1}};
    private int openCount = 0;
    private Set<Integer> open;
    private WeightedQuickUnionUF grid;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n is outside its prescribed range");
        }

        this.n = n;
        open = new HashSet<Integer>();

        // n*2 grid as a 1D representation of the matrix
        // Added two virtual top sites. 0 in the beginning of the array (top site)
        // (n*2) + 1 = added to the end of the array as a bottom virtual site
        grid = new WeightedQuickUnionUF((n * n) + 2);
    }

    // transpose row and col to the 1D representation
    private int transpose(int row, int col) {
        return ((row - 1) * n) + col;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row);
        validate(col);

        if (isOpen(row, col)) return;

        int set = transpose(row, col);
        open.add(set);

        // Connect neighbours to the newly open set
        for (int i = 0; i < neighbours.length; i++) {
            int newRow = row + neighbours[i][0];
            int newCol = col + neighbours[i][1];

            if (newRow > 0 && newRow <= n && newCol > 0 && newCol <= n) {
                if (isOpen(newRow, newCol)) {
                    grid.union(set, transpose(newRow, newCol));
                }
            }
        }
        openCount++;

        // Connect virtual top site to the open elements from the first row
        if (row == 1) {
            grid.union(0, set);
        }
        if (row == n) {
            grid.union((n*n)+1, set);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row);
        validate(col);
        return open.contains(transpose(row, col));
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row);
        validate(col);

        int set = transpose(row, col);

        return grid.find(0) == grid.find(set);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openCount;
        // return grid.count();
    }

    private void validate(int value) {
        if (value < 1 || value > n) {
            throw new IllegalArgumentException("value is outside its prescribed range");
        }
    }

    // does the system percolate?
    public boolean percolates() {
        return grid.find(0) == grid.find((n*n)+1);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation percolation = new Percolation(8);
        percolation.open(2,2);
        percolation.open(1,3);
        percolation.open(2,3);
        percolation.open(2,1);

        System.out.println(percolation.percolates());
    }
}
