import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {

    private static final int CUTOFF = 5;
    private final char[] text;
    private final int[] index;
    private final int n;

    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        n = s.length();
        s = s + '\0';
        text = s.toCharArray();
        index = new int[n];
        for (int i = 0; i < n; i++)
            index[i] = i;
        sort(0, n-1, 0);
    }  // circular suffix array of s

    private char circularText(int i, int d) {
        int k = (i + d) % n;
        if (k < 0 || k >= n) throw new IllegalArgumentException();
        return text[k];
    }

    private void sort(int lo, int hi, int d) {
        if (d == n) return;
        if (hi <= lo + CUTOFF) {
            insertion(lo, hi, d);
            return;
        }
        int lt = lo, gt = hi;
        char v = circularText(index[lo], d);
        int i = lo + 1;
        while (i <= gt) {
            int t = circularText(index[i], d);
            if (t < v)      exch(lt++, i++);
            else if (t > v) exch(i, gt--);
            else i++;
        }
        sort(lo, lt - 1, d);
        sort(lt, gt, d + 1);
        sort(gt + 1, hi, d);
    }

    private void insertion(int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(index[j], index[j-1], d); j--)
                exch(j, j - 1);
    }

    private boolean less(int i, int j, int d) {
        if (i == j) return false;
        for (int k = d; k < n; k++) {
            if (circularText(i, k) < circularText(j, k)) return true;
            if (circularText(i, k) > circularText(j, k)) return false;
        }
        return i > j;
    }

    private void exch(int i, int j) {
        int swap = index[i];
        index[i] = index[j];
        index[j] = swap;
    }

    public int length() {
        return n;
    }                   // length of s
    public int index(int i) {
        if (i < 0 || i >= n) throw new IllegalArgumentException();
        return index[i];
    }               // returns index of ith sorted suffix
    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < csa.length(); i++)
            StdOut.println(csa.index(i));
    } // unit testing of the methods (optional)
}
