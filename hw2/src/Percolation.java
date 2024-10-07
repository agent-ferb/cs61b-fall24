import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private boolean[][] sites;
    private int size;
    private WeightedQuickUnionUF uf;
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

        if (row == 0) {
            uf.union(curr, virtualTop);
        }
        if (row == size - 1) {
            uf.union(curr, virtualBottom);
        }

        if (row > 0 && isOpen(row - 1, col)) {
            uf.union(curr, to1d(row - 1, col));
        }
        if (row < size - 1 && isOpen(row + 1, col)) {
            uf.union(curr, to1d(row + 1, col));
        }
        if (col > 0 && isOpen(row, col - 1)) {
            uf.union(curr, to1d(row, col - 1));
        }
        if (col < size - 1 && isOpen(row, col + 1)) {
            uf.union(curr, to1d(row, col + 1));
        }
    }

    public boolean isOpen(int row, int col) {
        validate(row, col);
        return sites[row][col];
    }

    public boolean isFull(int row, int col) {
        validate(row, col);
        return uf.connected(to1d(row, col), virtualTop);
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
}
