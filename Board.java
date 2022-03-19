
import edu.princeton.cs.algs4.Stack;

public class Board {
    private final int[][] tiles;
    private final int n;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.n = tiles.length;
        this.tiles = copyArray(tiles);
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("");
        s.append(n);
        s.append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(" ");
                s.append(tiles[i][j]);
                s.append(" ");
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if ((tiles[i][j] != i * n + j + 1) && (tiles[i][j] != 0))
                    count++;
            }
        }
        return count;
    }

    private int getGoalRow(int x) {
        int rightI = 0;
        int y = n;
        while (y < x) {
            rightI++;
            y += n;
        }
        return rightI;
    }

    private int getGoalColumn(int x, int rightI) {
        int rightJ = x - 1 - (rightI * n);
        return rightJ;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if ((tiles[i][j] != i * n + j + 1) && (tiles[i][j] != 0)) {
                    int x = tiles[i][j];
                    int rightI = getGoalRow(x);
                    int rightJ = getGoalColumn(x, rightI);
                    count += Math.abs(rightI - i) + Math.abs(rightJ - j);
                }
            }
        }
        return count;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if ((tiles[i][j] != i * n + j + 1) && (i != n - 1 || j != n - 1))
                    return false;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null)
            return false;
        if (!(y.getClass() == this.getClass()))
            return false;
        Board that = (Board) y;
        if (this.dimension() != that.dimension())
            return false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] != that.tiles[i][j])
                    return false;
            }
        }
        return true;
    }

    private static int[][] copyArray(int[][] x) {
        int n = x.length;
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                copy[i][j] = x[i][j];
            }
        }
        return copy;
    }

    private static void exch(int[][] x, int i1, int j1, int i2, int j2) {
        int temp = x[i1][j1];
        x[i1][j1] = x[i2][j2];
        x[i2][j2] = temp;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> stack = new Stack<>();
        int[][] copy = copyArray(this.tiles);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] == 0) {
                    if (j - 1 >= 0) {
                        exch(copy, i, j, i, j - 1);
                        stack.push(new Board(copy));
                        exch(copy, i, j - 1, i, j);
                    }
                    if (j + 1 < n) {
                        exch(copy, i, j, i, j + 1);
                        stack.push(new Board(copy));
                        exch(copy, i, j + 1, i, j);
                    }
                    if (i - 1 >= 0) {
                        exch(copy, i, j, i - 1, j);
                        stack.push(new Board(copy));
                        exch(copy, i, j, i - 1, j);
                    }
                    if (i + 1 < n) {
                        exch(copy, i, j, i + 1, j);
                        stack.push(new Board(copy));
                        exch(copy, i + 1, j, i, j);
                    }
                    return stack;
                }
            }
        }
        return stack;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] x = copyArray(this.tiles);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n - 1; j++)
                if (tiles[i][j] != 0 && tiles[i][j + 1] != 0)
                {
                    exch(x, i, j, i, j + 1);
                    return new Board(x);
                }           
        throw new RuntimeException();
    }

    // unit testing (not graded)

    /*
     * public static void main(String[] args) {
     * 
     * int x[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 8, 7, 0 } }; // int y[][] = { { 1,
     * 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } }; Board b = new Board(x); Board b2 =
     * b.twin(); System.out.println(b); System.out.println(b2); Stack<Board> s =
     * (Stack<Board>) b.neighbors(); for (Board bb : s) { System.out.println(bb); }
     * Stack<Board> s2 = (Stack<Board>) b2.neighbors(); for (Board bb : s2) {
     * System.out.println(bb); }
     * 
     * //Board f = new Board(y); /*System.out.println("Board 1 : ");
     * System.out.println(b); System.out.println("Dimension of the board: " +
     * b.dimension()); System.out.println("Hamming: " + b.hamming());
     * System.out.println("Manhattan: " + b.manhattan());
     * System.out.println("Is goal? " + b.isGoal());
     * 
     * System.out.println("Board 2 : "); System.out.println(f);
     * System.out.println("Dimension of the board: " + f.dimension());
     * System.out.println("Hamming: " + f.hamming());
     * System.out.println("Manhattan: " + f.manhattan());
     * System.out.println("Is goal? " + f.isGoal());
     * 
     * System.out.println("Are they equal? " + b.equals(f));
     * System.out.println("Neighbours of board 2: "); Stack<Board> s =
     * (Stack<Board>) f.neighbors(); for (Board bb : s) { System.out.println(bb); }
     * System.out.println("Twin of board 1: "); System.out.println(b.twin());
     * 
     * }
     */
}
