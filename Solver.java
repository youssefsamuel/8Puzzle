import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private SearchNode finalSearchNode;
    private SearchNode finalTwinSearchNode;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();
        SearchNode initialSearchNode = new SearchNode(initial, 0, null);
        SearchNode initialTwinSearchNode = new SearchNode(initial.twin(), 0, null);
        if (initialSearchNode.currentBoard.isGoal()) {
            this.finalSearchNode = new SearchNode(initial, 0, null);
        } else {
            MinPQ<SearchNode> pq = new MinPQ<>();
            MinPQ<SearchNode> pqTwin = new MinPQ<>();
            pq.insert(initialSearchNode);
            pqTwin.insert(initialTwinSearchNode);
            SearchNode current;
            SearchNode currentTwin;
            int t = 1;
            while (true) {
                current = pq.delMin();
                currentTwin = pqTwin.delMin();
                if (current.currentBoard.isGoal()) {
                    this.finalSearchNode = current;
                    break;
                } else if (currentTwin.currentBoard.isGoal()) {
                    this.finalTwinSearchNode = currentTwin;
                    break;
                } else {
                    for (Board board : current.currentBoard.neighbors()) {
                        if (t == 1) {
                            pq.insert(new SearchNode(board, 1, current));
                        } else if (current.previous != null && !(board.equals(current.previous.currentBoard))) {
                            int moves = current.numberOfMoves + 1;
                            pq.insert(new SearchNode(board, moves, current));
                        }
                    }
                    for (Board boardTwin : currentTwin.currentBoard.neighbors()) {
                        if (t == 1) {
                            pqTwin.insert(new SearchNode(boardTwin, 1, currentTwin));
                            t++;
                        } else if (currentTwin.previous != null
                                && !(boardTwin.equals(currentTwin.previous.currentBoard))) {
                            int movesTwin = currentTwin.numberOfMoves + 1;
                            pqTwin.insert(new SearchNode(boardTwin, movesTwin, currentTwin));
                        }
                    }
                }
            }
        }

    }

    private class SearchNode implements Comparable<SearchNode> {
        private final Board currentBoard;
        private final int numberOfMoves;
        private final int priority;
        private final SearchNode previous;

        private SearchNode(Board currentBoard, int numberOfMoves, SearchNode previous) {
            this.currentBoard = currentBoard;
            this.numberOfMoves = numberOfMoves;
            this.previous = previous;
            this.priority = currentBoard.manhattan() + numberOfMoves;
        }

        @Override
        public int compareTo(Solver.SearchNode k) {
            return (this.priority - k.priority);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        if (this.finalTwinSearchNode != null)
            return false;
        else if (this.finalSearchNode != null) {
            if (this.finalSearchNode.currentBoard.isGoal())
                return true;
        }
        return false;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable())
            return -1;
        return this.finalSearchNode.numberOfMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;
        Stack<Board> stack = new Stack<>();
        SearchNode current = this.finalSearchNode;
        do {
            stack.push(current.currentBoard);
            current = current.previous;
        } while (current != null);
        return stack;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
