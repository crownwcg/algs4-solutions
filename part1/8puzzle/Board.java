import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;

/**
 *  The {@code Board} class provides methods for calculating hamming distance,
 *  manhattan distance and other related methods of 8 puzzle board
 *
 *  @author Chenggang Wang
 */
public class Board {
    private final short n;          // size of square array side length
    private final int[][] tiles;    // tiles[i][j] = current number of the board

    /**
     * Initializes a Board data structure using pass-in array
     *
     * @param  blocks 
     * @throws IllegalArgumentException if {@code n < 0}
     */
    public Board(int[][] blocks) {
        n = (short) blocks.length;
        tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = blocks[i][j];
            }
        }
    }          
    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)

    public int dimension() {
        return n;
    }                 
    // board dimension n

    private int tileNumber(int i, int j) {
        int tileNumber = i * n + j + 1;
        if (tileNumber == n * n)
            return 0;
        return tileNumber;
    }

    public int hamming() {
        int hammingCount = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != tileNumber(i, j)) 
                    hammingCount++;
            }
        }

        if (tiles[n-1][n-1] != 0)
            hammingCount--;

        return hammingCount;
    }                   
    // number of blocks out of place

    public int manhattan() {
        int[][] distances = new int[n][n];
        int manhattanCount = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                distances[i][j] = findDistance(tiles, i, j);
            }
        } 

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                manhattanCount += distances[i][j];
            }
        }

        return manhattanCount;
    }                
    // sum of Manhattan distances between blocks and goal

    private int findDistance(int[][] blocks, int i, int j) {
        if (blocks[i][j] == 0) 
            return 0;
        if (blocks[i][j] == tileNumber(i, j)) 
            return 0;

        int numberBe = blocks[i][j];
        int row, col;
        Index index;

        index = search(numberBe);
        row = index.getRow();
        col = index.getCol();

        return Math.abs(row - i) + Math.abs(col - j);
    }

    private Index search(int tileNumber) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tileNumber(i, j) == tileNumber) {
                    Index index = new Index(i, j);
                    return index;
                }
            }
        }

        return null;
    }

    private class Index {
        private final int row;
        private final int col; 

        public Index(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }
    }

    public boolean isGoal() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != tileNumber(i, j)) 
                    return false;
            }
        }

        return true;
    }                
    // is this board the goal board?

    public Board twin() {
        Board twin = new Board(tiles);
        int i1, j1, i2, j2;

        i1 = 0;
        j1 = 0;
        i2 = 1;
        j2 = 0;
        while (twin.tiles[i1][j1] == 0 && j1 < n) {
            j1++;
        }
        while (twin.tiles[i2][j2] == 0 && j2 < n) {
            j2++;
        }
        swap(twin.tiles, i1, j1, i2, j2);

        return twin;
    }                   
    // a board that is obtained by exchanging any pair of blocks

    private void swap(int[][] board, int i1, int j1, int i2, int j2) {
        int temp = board[i1][j1];
        board[i1][j1] = board[i2][j2];
        board[i2][j2] = temp;
    }

    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        Board compare = (Board) y;
        if (this.dimension() != compare.dimension()) return false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] != compare.tiles[i][j])
                    return false;
            }
        }

        return true;
    }        
    // does this board equal y?

    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<Board>();
        Index zeroIndex;
        zeroIndex = findZeroIndex(this);
        if (zeroIndex.getRow() > 0)       neighbors.add(moveZeroBoard(zeroIndex, -1, 0));
        if (zeroIndex.getRow() < n - 1)   neighbors.add(moveZeroBoard(zeroIndex, 1, 0));
        if (zeroIndex.getCol() > 0)       neighbors.add(moveZeroBoard(zeroIndex, 0, -1));
        if (zeroIndex.getCol() < n - 1)   neighbors.add(moveZeroBoard(zeroIndex, 0, 1));

        return neighbors;
    }     
    // all neighboring boards

    private Board moveZeroBoard(Index zeroIndex, int i, int j) {
        int row = zeroIndex.getRow() + i;
        int col = zeroIndex.getCol() + j;
        Board b = new Board(this.tiles);
        swap(b.tiles, zeroIndex.getRow(), zeroIndex.getCol(), row, col);

        return b;
    }

    private Index findZeroIndex(Board b) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (b.tiles[i][j] == 0) {
                    Index in = new Index(i, j);
                    return in;
                }
            }
        }

        return null;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(" " + n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                builder.append(tiles[i][j] + "\t");
            }
            builder.append("\n");
        }

        return builder.toString();
    }               
    // string representation of this board (in the output format specified below)

    public static void main(String[] args) {
        In in = new In("./8puzzle/puzzle06.txt");  
        int n = in.readInt();  
        int[][] blocks = new int[n][n];  
        for (int i = 0; i < n; i++)  
            for (int j = 0; j < n; j++)  
                blocks[i][j] = in.readInt();  
        Board initial = new Board(blocks);  
        StdOut.println(initial);  
        StdOut.println(initial.hamming()); 
        StdOut.println(initial.manhattan());
        StdOut.println(initial.neighbors());
        StdOut.println(initial.twin());
    } 
}