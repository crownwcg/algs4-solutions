import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private SearchNode currentNode;
    private SearchNode twinNode;
    private Stack<Board> solution;

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int priority;
        private int moves;
        private SearchNode previous;

        public SearchNode(Board board, SearchNode previous) {
            this.board = board;
            if (previous == null) {
                this.moves = 0;
                this.previous = null;
            } else {
                this.moves = previous.moves + 1;
                this.previous = previous;
            }
            this.priority = moves + board.manhattan();
        }

        public int compareTo(SearchNode that) {
            if (this.priority < that.priority) return -1;
            if (this.priority > that.priority) return 1;
            return 0;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("the Board is null");
        currentNode = new SearchNode(initial, null);
        twinNode = new SearchNode(initial.twin(), null);
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        MinPQ<SearchNode> pqTwin = new MinPQ<SearchNode>();
        pq.insert(currentNode);
        pqTwin.insert(twinNode);

        while (true) {
            currentNode = pq.delMin();
            if (currentNode.board.isGoal()) break;
            putPQ(currentNode, pq);

            twinNode = pqTwin.delMin();
            if (twinNode.board.isGoal()) break;
            putPQ(twinNode, pqTwin);
        }
    }           

    private void putPQ(SearchNode node, MinPQ<SearchNode> pq) {
        for (Board neighbors: node.board.neighbors()) {
            if (node.previous == null || !neighbors.equals(node.previous.board))
                pq.insert(new SearchNode(neighbors, node));
        }
    }
    
    // is the initial board solvable?
    public boolean isSolvable() {
        return currentNode.board.isGoal();
    }            
    
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable())
            return -1;
        return currentNode.moves;
    }                    
    
    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (isSolvable()) {
            solution = new Stack<Board>();
            SearchNode node = currentNode;
            while (node != null) {
                solution.push(node.board);
                node = node.previous;
            }
            return solution;
        }
        return null;
    }      

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

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