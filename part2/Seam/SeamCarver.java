import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {

    private static final double BORDER_ENERGY = 1000;

    private Picture pic;
    private int ht;
    private int wd;
    private double[][] energies;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new java.lang.IllegalArgumentException();

        this.pic = new Picture(picture);
        ht  = pic.height();
        wd  = pic.width();

        calEnergies();
    }     

    private void calEnergies() {
        energies = new double[wd][ht];
        for (int x = 0; x < wd; x++) {
            for (int y = 0; y < ht; y++) {
                energies[x][y] = calEnergy(x, y);
            }
        }
    } 

    private void validate(int x, int y) {
        if (x < 0 || y < 0 || x >= wd || y >= ht) 
            throw new java.lang.IllegalArgumentException();
    }  

    // current picture
    public Picture picture() {
        Picture picture = new Picture(pic);
        return picture;
    }                      

    // width of current picture  
    public     int width() {
        return wd;
    }                          

    // height of current picture
    public     int height() {
        return ht;
    }                          

    // energy of pixel at column x and row y
    public  double energy(int x, int y) {
        validate(x, y);
        return energies[x][y];
    }      

    private double calEnergy(int x, int y) {
        validate(x, y);
        if (x == 0 || x == wd - 1 || y == ht - 1 || y == 0) return BORDER_ENERGY;

        Color  left  = pic.get(x - 1, y);
        Color  right = pic.get(x + 1, y);
        Color  up    = pic.get(x, y - 1);
        Color  down  = pic.get(x, y + 1);
        double delRx = right.getRed()   - left.getRed();
        double delGx = right.getGreen() - left.getGreen();
        double delBx = right.getBlue()  - left.getBlue();
        double delRy = down.getRed()    - up.getRed();
        double delGy = down.getGreen()  - up.getGreen();
        double delBy = down.getBlue()   - up.getBlue();
        return Math.sqrt(delRx * delRx + delGx * delGx + delBx * delBx
             + delRy * delRy + delGy * delGy + delBy * delBy);
    }        

    // sequence of indices for horizontal seam
    public   int[] findHorizontalSeam() {
        int[]  horSeam = new int[wd];
        int[][] edgeTo = new int[wd][ht];
        double[][]  dp = new double[wd][ht];

        /** initiate the array of dp and edgeTo */
        for (int x = 0; x < wd; x++) {
            for (int y = 0; y < ht; y++) {
                dp[x][y] = Double.POSITIVE_INFINITY;
                if (x == 0) dp[x][y] = 0;
                edgeTo[x][y] = -1;
            }
        }

        for (int x = 0; x < wd - 1; x++) {
            for (int y = 0; y < ht; y++) {
                if (y > 0)       relaxH(x, y, x + 1, y - 1, dp, edgeTo);
                                 relaxH(x, y, x + 1, y,     dp, edgeTo);
                if (y <= ht - 2) relaxH(x, y, x + 1, y + 1, dp, edgeTo);
            }
        }

        int yIndex = -1;
        double dpMin = Double.POSITIVE_INFINITY;
        for (int y = 0; y < ht; y++) {
            if (dp[wd - 1][y] < dpMin) {
                dpMin  = dp[wd - 1][y];
                yIndex = y;
            }
        }

        int index = yIndex;
        for (int x = wd - 1; x >= 0; x--) {
            horSeam[x] = index;
            index = edgeTo[x][index];
        }
        return horSeam;
    }             

    private void relaxH(int fromX, int fromY,  int toX, int toY, double[][] dp, int[][] edgeTo) {
        if (Double.compare(dp[fromX][fromY] + energies[toX][toY], dp[toX][toY]) < 0) {
            dp[toX][toY] = dp[fromX][fromY] + energies[toX][toY];
            edgeTo[toX][toY] = fromY;
        }
    }

    // sequence of indices for vertical seam
    public   int[] findVerticalSeam() {
        int[]  verSeam = new int[ht];
        int[][] edgeTo = new int[wd][ht];
        double[][]  dp = new double[wd][ht];

        /** initiate the array of dp and edgeTo */
        for (int x = 0; x < wd; x++) {
            for (int y = 0; y < ht; y++) {
                dp[x][y] = Double.POSITIVE_INFINITY;
                if (y == 0) dp[x][y] = 0;
                edgeTo[x][y] = -1;
            }
        }

        for (int y = 0; y < ht - 1; y++) {
            for (int x = 0; x < wd; x++) {
                if (x > 0)       relaxV(x, y, x - 1, y + 1, dp, edgeTo);
                                 relaxV(x, y, x,     y + 1, dp, edgeTo);
                if (x <= wd - 2) relaxV(x, y, x + 1, y + 1, dp, edgeTo);
            }
        }

        int xIndex = -1;
        double dpMin = Double.POSITIVE_INFINITY;
        for (int x = 0; x < wd; x++) {
            if (dp[x][ht-1] < dpMin) {
                dpMin  = dp[x][ht-1];
                xIndex = x;
            }
        }

        int index = xIndex;
        for (int y = ht - 1; y >= 0; y--) {
            verSeam[y] = index;
            index = edgeTo[index][y];
        }
        return verSeam;
    }    

    private void relaxV(int fromX, int fromY,  int toX, int toY, double[][] dp, int[][] edgeTo) {
        if (Double.compare(dp[fromX][fromY] + energies[toX][toY], dp[toX][toY]) < 0) {
            dp[toX][toY] = dp[fromX][fromY] + energies[toX][toY];
            edgeTo[toX][toY] = fromX;
        }
    }    

    // remove horizontal seam from current picture
    public    void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new java.lang.IllegalArgumentException();

        if (seam.length < 1 || seam.length != wd) 
            throw new java.lang.IllegalArgumentException();
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= ht)
                throw new java.lang.IllegalArgumentException();
        }
        for (int i = 1; i < seam.length; i++) {
            if (Math.abs(seam[i] - seam[i-1]) > 1)
                throw new java.lang.IllegalArgumentException();
        }
        Picture tmp = new Picture(wd, ht - 1);
        for (int x = 0; x < wd; x++) {
            for (int y = 0; y < seam[x]; y++) {
                tmp.set(x, y, pic.get(x, y));
            }
            for (int y = seam[x]; y < ht - 1; y++) {
                tmp.set(x, y, pic.get(x, y + 1));
            }
        }
        pic = tmp;
        wd  = tmp.width();
        ht  = tmp.height();
        calEnergies();
    }  

    // remove vertical seam from current picture
    public    void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new java.lang.IllegalArgumentException();

        if (seam.length < 1 || seam.length != ht) 
            throw new java.lang.IllegalArgumentException();
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= wd)
                throw new java.lang.IllegalArgumentException();
        }
        for (int i = 1; i < seam.length; i++) {
            if (Math.abs(seam[i] - seam[i-1]) > 1)
                throw new java.lang.IllegalArgumentException();
        }
        Picture tmp = new Picture(wd - 1, ht);
        for (int y = 0; y < ht; y++) {
            for (int x = 0; x < seam[y]; x++) {
                tmp.set(x, y, pic.get(x, y));
            }
            for (int x = seam[y]; x < wd - 1; x++) {
                tmp.set(x, y, pic.get(x + 1, y));
            }
        }
        pic = tmp;
        wd  = tmp.width();
        ht  = tmp.height();
        calEnergies();
    }    

    public static void main(String[] args) {

    }
}
