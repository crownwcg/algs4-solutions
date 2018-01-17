import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    private static final int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] in  = BinaryStdIn.readString().toCharArray();
        char[] mtf = new char[R];
        for (char i = 0; i < R; i++)
            mtf[i] = i;
        for (int i = 0; i < in.length; i++) {
            char c = 0;
            for (char j = 0; j < 256; j++) {
                char tmp = mtf[j];
                mtf[j] = c;
                c = tmp;
                if (c == in[i]) {
                    mtf[0] = c;
                    BinaryStdOut.write(j);
                    break;
                }
            }
        }
        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] in  = BinaryStdIn.readString().toCharArray();
        char[] mtf = new char[R];
        for (char i = 0; i < R; i++)
            mtf[i] = i;
        for (int i = 0; i < in.length; i++) {
            char c = 0;
            for (int j = 0; j < R; j++) {
                char tmp = mtf[j];
                mtf[j] = c;
                c = tmp;
                if (j == in[i]) {
                    mtf[0] = c;
                    BinaryStdOut.write(c);
                    break;
                }
            }
        }
        BinaryStdOut.flush();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException();
    }
}
