/******************************************************************************
 * Compilation:  javac Outcast.java
 * Execution:    java Outcast filename.txt
 * Dependencies: In.java StdOut.java
 *
 * % more outcast5.txt
 * horse zebra cat bear table
 *
 * % more outcast8.txt
 * water soda bed orange_juice milk apple_juice tea coffee
 *
 * % more outcast11.txt
 * apple pear peach banana lime lemon blueberry strawberry mango watermelon potato
 *
 *
 * % java Outcast synsets.txt hypernyms.txt outcast5.txt outcast8.txt outcast11.txt
 * outcast5.txt: table
 * outcast8.txt: bed
 * outcast11.txt: potato
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * The {@code Outcase} class supports to measure the semantic relatedness of two nouns.
 *  
 * Semantic relatedness refers to the degree to which two concepts are related. 
 * Measuring semantic relatedness is a challenging problem. For example, most of us agree 
 * that George Bush and John Kennedy (two U.S. presidents) are more related than are 
 * George Bush and chimpanzee (two primates). However, not most of us agree that George 
 * Bush and Eric Arthur Blair are related concepts. But if one is aware that George Bush 
 * and Eric Arthur Blair (aka George Orwell) are both communicators, then it becomes clear 
 * that the two concepts might be related.
 * 
 * <p>
 * We define the semantic relatedness of two wordnet nouns A and B as follows:
 *
 * distance(A, B) = distance is the minimum length of any ancestral path between 
 * any synset v of A and any synset w of B.
 * 
 * This is the notion of distance that you will use to implement the distance() and sap() 
 * methods in the WordNet data type.
 *
 * Given a list of wordnet nouns A1, A2, ..., An, which noun is the least related to the others? 
 * To identify an outcast, compute the sum of the distances between each noun and every other one:
 *
 * di   =   dist(Ai, A1)   +   dist(Ai, A2)   +   ...   +   dist(Ai, An)
 * 
 * and return a noun At for which dt is maximum.
 * <p>
 *
 * @author Chenggang Wang
 */
public class Outcast {

    private final WordNet wn;       // word net instance

    /** 
     * Constructor takes a wordnet instance
     *
     * @param wordnet input wordnet
     * @throws @throws IllegalArgumentException if wordnet is null
     */
    public Outcast(WordNet wordnet) {
        if (wordnet == null) throw new IllegalArgumentException();
        wn = wordnet;
    }         

    /** 
     * Calculate the outcast of the nouns
     *
     * @param nouns String array of word nouns
     * @throws IllegalArgumentException if input is invalid
     * @return an outcast
     */
    public String outcast(String[] nouns) {
        if (nouns == null) throw new IllegalArgumentException();
        String leastRelated = nouns[0];
        int max = Integer.MIN_VALUE;
        for (String s : nouns) {
            if (!wn.isNoun(s)) throw new IllegalArgumentException();
        }
        for (String s : nouns) {
            int related = 0;
            for (String t : nouns) {
                related += wn.distance(s, t);
            }
            if (max < related) {
                max = related;
                leastRelated = s;
            }
        }
        return leastRelated;
    }   

    /** 
     * Unit test the data type.
     *
     * @param args the command-line argument
     */
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}