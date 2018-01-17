/******************************************************************************
 * Compilation:  javac SAP.java
 * Execution:    java SAP filename.txt
 * Dependencies: Digraph.java BreadFirstDirectedPaths.java In.java StdIn.java
 *               StdOut.java
 *
 * % more digraph1.txt             % java SAP digraph1.txt
 * 13                              3 11
 * 11                              length = 4, ancestor = 1
 *  7  3                            
 *  8  3                           9 12
 *  3  1                           length = 3, ancestor = 5
 *  4  1
 *  5  1                           7 2
 *  9  5                           length = 4, ancestor = 0
 * 10  5
 * 11 10                           1 6
 * 12 10                           length = -1, ancestor = -1
 *  1  0
 *  2  0
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

/**
 * The {@code SAP} class supports to find shortest ancestral path
 *  
 * An ancestral path between two vertices v and w in a digraph is a directed path 
 * from v to a common ancestor x, together with a directed path from w to the same 
 * ancestor x. A shortest ancestral path is an ancestral path of minimum total length.
 * 
 * <p>
 * All methods should throw a java.lang.IllegalArgumentException if any argument is null 
 * or if any argument vertex is invalid—not between 0 and G.V() - 1.
 *
 * All methods (and the constructor) should take time at most proportional to E + V 
 * in the worst case, where E and V are the number of edges and vertices in the digraph, 
 * respectively. Your data type should use space proportional to E + V.
 * <p>
 *
 * @author Chenggang Wang
 */
public class SAP {

    private final Digraph G;        // graph to find SAP

    /** 
     * Constructor takes a digraph (not necessarily a DAG)
     *
     * @param G input graph
     * @throws IllegalArgumentException if G is null
     */
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        this.G = new Digraph(G);
    }

    /** 
     * Find length of shortest ancestral path between v and w; -1 if no such path
     *
     * @param v index of vertice v
     * @param w index of vertice w
     * @throws IndexOutOfBoundsException if index is invalid
     * @return length of shortest ancestral path between v and w; -1 if no such path
     */
    public int length(int v, int w) {
        validate(v);
        validate(w);
        return findLength(new BreadthFirstDirectedPaths(G, v), new BreadthFirstDirectedPaths(G, w));
    }

    /** 
     * Find a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
     *
     * @param v index of vertice
     * @param w index of vertice
     * @throws IndexOutOfBoundsException if index is invalid
     * @return a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
     */
    public int ancestor(int v, int w) {
        validate(v);
        validate(w);
        return findAncestor(new BreadthFirstDirectedPaths(G, v), new BreadthFirstDirectedPaths(G, w));
    }

    /** 
     * Find length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
     *
     * @param v index of iterating vertices
     * @param w index of iterating vertices
     * @throws NullPointerException if input is null
     * @throws IndexOutOfBoundsException if index is invalid
     * @return length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
     */
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new NullPointerException();
        validate(v);
        validate(w);
        return findLength(new BreadthFirstDirectedPaths(G, v), new BreadthFirstDirectedPaths(G, w));
    }

    /** 
     * Find a common ancestor that participates in shortest ancestral path; -1 if no such path
     *
     * @param v index of iterating vertices
     * @param w index of iterating vertices
     * @throws NullPointerException if input is null
     * @throws IndexOutOfBoundsException if index is invalid
     * @return a common ancestor that participates in shortest ancestral path; -1 if no such path
     */
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new NullPointerException();
        validate(v);
        validate(w);
        return findAncestor(new BreadthFirstDirectedPaths(G, v), new BreadthFirstDirectedPaths(G, w));
    }

    /** 
     * Check if v is out of bound
     *
     * @param v index of vertice
     * @throws IndexOutOfBoundsException if index is invalid
     */
    private void validate(int v) {
        if (v < 0 || v > G.V() - 1) throw new IndexOutOfBoundsException();
    }

    /** 
     * Check if v is out of bound
     *
     * @param v index of vertices
     * @throws IndexOutOfBoundsException if index is invalid
     */
    private void validate(Iterable<Integer> v) {
        for (int i : v) 
            validate(i);
    }

    /** 
     * Find a distance between two bfs paths of v and w
     *
     * @param vp bfs path of a vertice
     * @param wp bfs path of a vertice
     * @throws NullPointerException if input is null
     * @return length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
     */
    private int findLength(BreadthFirstDirectedPaths vp, BreadthFirstDirectedPaths wp) {
        if (vp == null || wp == null) throw new NullPointerException();
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < G.V(); i++) {
            if (vp.hasPathTo(i) && wp.hasPathTo(i)) {
                min = Math.min(min, vp.distTo(i)+wp.distTo(i));
            }
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }

    /** 
     * Find a common ancestor between two bfs paths of v and w
     *
     * @param vp bfs path of a vertice
     * @param wp bfs path of a vertice
     * @throws NullPointerException if input is null
     * @return a common ancestor that participates in shortest ancestral path; -1 if no such path
     */
    private int findAncestor(BreadthFirstDirectedPaths vp, BreadthFirstDirectedPaths wp) {
        if (vp == null || wp == null) throw new NullPointerException();
        int ancestor = -1;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < G.V(); i++) {
            if (vp.hasPathTo(i) && wp.hasPathTo(i) && vp.distTo(i) + wp.distTo(i) < min) {
                    min = vp.distTo(i)+wp.distTo(i);
                    ancestor = i;
            }
        }
        return ancestor;
    }

    /** 
     * Unit test the data type.
     *
     * @param args the command-line argument
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}