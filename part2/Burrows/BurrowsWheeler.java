import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import java.util.Arrays;

public class BurrowsWheeler {
    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        char[] out = new char[s.length()];
        int id = 0;
        for (int i = 0; i < s.length(); i++) {
            int k = csa.index(i);
            if (k == 0) {
                id = i;
                out[i] = s.charAt(s.length() - 1);
            } else {
                out[i] = s.charAt(k-1);
            }
        }
        BinaryStdOut.write(id);
        for (int i = 0; i < s.length(); i++) {
            BinaryStdOut.write(out[i]);
        }
        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {
        int id = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        int n = s.length();
        char[] coll = s.toCharArray();
        int[] next = new int[n];
        int[] counts = new int[256];
        Arrays.fill(counts, 0);
        for (int i = 0; i < n; i++)
            counts[s.charAt(i)]++;
        for (int i = 1; i < 256; i++)
            counts[i] += counts[i-1];
        for (int i = n-1; i >= 0; i--) {
            char c = s.charAt(i);
            int j = --counts[c];
            next[j] = i;
            coll[j] = c;
        }
        for (int i = 0; i < n; id = next[id], i++)
            BinaryStdOut.write(coll[id]);
        BinaryStdOut.flush();
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
        if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException();
    }
}
