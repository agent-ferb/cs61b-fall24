import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private boolean[][] sites;
    private int size;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufFullness;
    private int virtualTop;
    private int virtualBottom;
    private int openSites;


    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("Size must be positive.");
        }
        size = N;
        sites = new boolean[N][N];
        uf = new WeightedQuickUnionUF(N * N + 2);
        ufFullness = new WeightedQuickUnionUF(N * N + 1);
        virtualTop = N * N;
        virtualBottom = N * N + 1;
        openSites = 0;
    }

    public void open(int row, int col) {
        validate(row, col);
        if (isOpen(row, col)) {
            return;
        }
        sites[row][col] = true;
        openSites++;
        int curr = to1d(row, col);

        if (size == 1) {
            uf.union(curr, virtualTop);
            uf.union(curr, virtualBottom);
            ufFullness.union(curr, virtualTop);
        }

        if (row == 0) {
            uf.union(curr, virtualTop);
            ufFullness.union(curr, virtualTop);
        }
        if (row == size - 1) {
            uf.union(curr, virtualBottom);
        }

        connectIfOpen(row, col, row - 1, col);  // Top
        connectIfOpen(row, col, row + 1, col);  // Bottom
        connectIfOpen(row, col, row, col - 1);  // Left
        connectIfOpen(row, col, row, col + 1);  // Right
    }

    public boolean isOpen(int row, int col) {
        validate(row, col);
        return sites[row][col];
    }

    public boolean isFull(int row, int col) {
        validate(row, col);
        return ufFullness.connected(to1d(row, col), virtualTop);
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean percolates() {
        return uf.connected(virtualTop, virtualBottom);
    }

    private int to1d(int row, int col) {
        return row * size + col;
    }

    private void validate(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds.");
        }
    }

    private void connectIfOpen(int row, int col, int neighborRow, int neighborCol) {
        if (neighborRow >= 0 && neighborRow < size && neighborCol >= 0 && neighborCol < size && isOpen(neighborRow, neighborCol)) {
            int curr = to1d(row, col);
            int neighbor = to1d(neighborRow, neighborCol);
            uf.union(curr, neighbor);
            ufFullness.union(curr, neighbor);  // Fullness check
        }
    }
}
