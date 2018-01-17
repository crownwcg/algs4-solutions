/******************************************************************************
 * Compilation:  javac WordNet.java
 * Execution:    java WordNet filename.txt filename.txt
 * Dependencies: Digraph.java DirectedCycle.java In.java
 ******************************************************************************/

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * The {@code WordNet} class represents directed graph of words.
 * It supports to build the wordnet digraph: each vertex v is an integer that
 * represents a synset, and each directed edge v→w represents that w is a hypernym
 * of v. The wordnet digraph is a rooted DAG: it is acyclic and has one vertex—the
 * root—that is an ancestor of every other vertex. However, it is not necessarily
 * a tree because a synset can have more than one hypernym.
 *
 * WordNet is a semantic lexicon for the English language that is used extensively
 * by computational linguists and cognitive scientists; for example, it was a key
 * component in IBM's Watson. WordNet groups words into sets of synonyms called synsets
 * and describes semantic relationships between them. One such relationship is the
 * is-a relationship, which connects a hyponym (more specific synset) to a hypernym
 * (more general synset). For example, animal is a hypernym of both bird and fish;
 * bird is a hypernym of eagle, pigeon, and seagull.
 *
 * <p>
 * This implementation uses two hash map to record word noun and its id number.
 * All operations use space linear in the input size (size of synsets and hypernyms
 * files). The constructor should take time linearithmic (or better) in the input size.
 * The method isNoun() should run in time logarithmic (or better) in the number of nouns.
 * The methods distance() and sap() should run in time linear in the size of the WordNet
 * digraph. For the analysis, assume that the number of nouns per synset is bounded by
 * a constant.
 *
 * All methods and the constructor should throw a java.lang.IllegalArgumentException
 * if any argument is null. The constructor should throw a java.lang.IllegalArgumentException
 * if the input does not correspond to a rooted DAG. The distance() and sap() methods
 * should throw a java.lang.IllegalArgumentException unless both of the noun arguments
 * are WordNet nouns.
 * <p>
 *
 * @author Chenggang Wang
 */
public class WordNet {

    private final Map<Integer, String>            synsetsMap;       // HashMap store sysnsets
    private final Map<String, ArrayList<Integer>> nounsMap;         // HashMap store words nouns
    private final Digraph G;                                        // graph that represents the wordnet
    private final SAP     sap;                                      // class to find the shortest ancestral path

    /**
     * Constructor takes the name of the two input files to initialize the graph
     *
     * @param sysets    file name of synsets
     * @param hypernyms file name of hypernyms
     * @throws IllegalArgumentException if either file name is null
     */
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();;
        synsetsMap = new HashMap<>();
        nounsMap   = new HashMap<>();
        In synsetsIn   = new In(synsets);
        In hypernymsIn = new In(hypernyms);

        int vertices = 0;
        while (synsetsIn.hasNextLine()) {
            String line = synsetsIn.readLine();
            String[] tokens = line.split(",");
            int v = Integer.parseInt(tokens[0]);
            synsetsMap.put(v, tokens[1]);
            vertices++;
            String[] names = tokens[1].split(" ");
            for (String s : names) {
                if (!nounsMap.containsKey(s)) {
                    nounsMap.put(s, new ArrayList<Integer>());
                }
                nounsMap.get(s).add(v);
            }
        }
        synsetsIn.close();

        G = new Digraph(vertices);
        while (hypernymsIn.hasNextLine()) {
            String line = hypernymsIn.readLine();
            String[] tokens = line.split(",");
            int v = Integer.parseInt(tokens[0]);
            for (int i = 1; i < tokens.length; i++) {
                int w = Integer.parseInt(tokens[i]);
                G.addEdge(v, w);
            }
        }
        hypernymsIn.close();

        checkCircle();
        checkRoot();
        sap = new SAP(G);
    }

    /**
     * Check if the input graph has cycles
     *
     * @throws IllegalArgumentException if the graph has a cycle
     */
    private void checkCircle() {
        DirectedCycle cycle = new DirectedCycle(G);
        if (cycle.hasCycle()) throw new IllegalArgumentException();
    }

    /**
     * Check if the input graph is rooted
     *
     * @throws IllegalArgumentException if the graph is not rooted
     */
    private void checkRoot() {
        int count = 0;
        for (int v = 0; v < G.V(); v++) {
            if (G.outdegree(v) == 0)
                count++;
        }
        if (count != 1) throw new IllegalArgumentException();
    }

    /**
     * Iterate all WordNet nouns using HashMap key set
     *
     * @return all WordNet nouns
     */
    public Iterable<String> nouns() {
        return nounsMap.keySet();
    }

    /**
     * Check if the word is a WordNet noun
     *
     * @throws IllegalArgumentException if word is null
     * @return true if the word is a WordNet noun, otherwise false
     */
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return nounsMap.containsKey(word);
    }

    /**
     * Find distance between nounA and nounB
     *
     * @throws IllegalArgumentException if nounA or nounB is not a wordnet noun
     * @return the distance between nounA and nounB
     */
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        Iterable<Integer> nA = nounsMap.get(nounA);
        Iterable<Integer> nB = nounsMap.get(nounB);
        return sap.length(nA, nB);
    }

    /**
     * a synset that is the common ancestor of nounA and nounB
     * in a shortest ancestral path
     *
     * @throws IllegalArgumentException if nounA or nounB is not a wordnet noun
     * @return the common ancestor noun in the shortest ancestral path of nounA and nounB
     */
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new java.lang.IllegalArgumentException();
        Iterable<Integer> nA = nounsMap.get(nounA);
        Iterable<Integer> nB = nounsMap.get(nounB);
        int ancestor = sap.ancestor(nA, nB);
        return synsetsMap.get(ancestor);
    }

    /**
     * Unit test the data type.
     *
     * @param args the command-line argument
     */
    public static void main(String[] args) {
        WordNet wn = new WordNet(args[0], args[1]);
        System.out.println(wn.distance("a", "c"));
        System.out.println(wn.sap("a", "c"));
    }
}
