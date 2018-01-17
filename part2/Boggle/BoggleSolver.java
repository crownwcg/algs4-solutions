import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver
{

    private static final int R = 26;
    private static final int[] SCORES = {0, 0, 0, 1, 1, 2, 3, 5, 11};
    private Node root;

    private int r;
    private int c;
    private BoggleBoard b;
    private boolean[][] mark;

    private static class Node {
        private Object val;
        private Node[] next = new Node[R];
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) throw new IllegalArgumentException();
        for (int i = 0; i < dictionary.length; i++) {
            put(dictionary[i], score(dictionary[i]));
        }
    }

    private int get(String key) {
        if (key == null) throw new IllegalArgumentException();
        Node x = get(root, key, 0);
        if (x == null || x.val == null) return 0;
        return (Integer) x.val;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char ch = key.charAt(d);
        return get(x.next[ch - 'A'], key, d + 1);
    }

    private boolean contains(String key) {
        return get(key) != 0;
    }

    private void put(String key, int val) {
        if (key == null) throw new IllegalArgumentException();
        root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, int val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.val = val;
            return x;
        }
        char ch = key.charAt(d);
        x.next[ch - 'A'] = put(x.next[ch - 'A'], key, val, d + 1);
        return x;
    }

    private int score(String word) {
        return SCORES[Math.min(word.length(), SCORES.length - 1)];
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) throw new IllegalArgumentException();
        Set<String> res = new HashSet<String>();
        r = board.rows();
        c = board.cols();
        b = board;
        mark = new boolean[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                dfa(root, i, j, "", res);
            }
        }
        List<String> list = new ArrayList<String>(res);
        Collections.sort(list);
        return list;
    }

    private void dfa(Node x, int i, int j, String key, Set<String> res) {
        if (x == null) return;
        char ch = b.getLetter(i, j);
        if (ch == 'Q' && x.next['Q' - 'A'] != null)
            dfs(x.next['Q' - 'A'].next['U' - 'A'], key + "QU", i, j, res);
        else
            dfs(x.next[ch - 'A'], key + ch, i, j, res);
    }

    private void dfs(Node x, String key, int i, int j, Set<String> res) {
        int len = key.length();
        if (x == null) return;
        if (x.val != null && len >= 3) {
            res.add(key);
        }
        mark[i][j] = true;
        for (int m = -1; m <= 1; m++) {
            for (int n = -1; n <= 1; n++) {
                int row = i + m;
                int col = j + n;
                if (row >= 0 && row < r && col >= 0 && col < c)
                    if (!mark[row][col])
                        dfa(x, row, col, key, res);
            }
        }
        mark[i][j] = false;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null) throw new IllegalArgumentException();
        if (!contains(word)) return 0;
        return score(word);
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
