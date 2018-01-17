import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;
import java.util.ArrayList;

public class BruteCollinearPoints {
    /* arraylist for line segments */
    private final ArrayList<LineSegment> segList = new ArrayList<LineSegment>(); 
    /* number of line segments */
    private int segNum = 0;

    /**
     * finds all line segments containing 4 points
     *
     * @param points[] array of the points
     */
    public BruteCollinearPoints(Point[] points) {
        /* check points array's validity */
        if (points == null)
            throw new IllegalArgumentException("Constructor argument Point[] is null");
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) 
                throw new IllegalArgumentException("there is null in Constructor argument");
        }
        Point[] pointsCopy = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            pointsCopy[i] = points[i];
        }
        Arrays.sort(pointsCopy);
        for (int i = 0; i < pointsCopy.length - 1; i++) { 
            if (pointsCopy[i].compareTo(pointsCopy[i+1]) == 0)
                    throw new IllegalArgumentException("points are illegal");
        }
        /* find line segments */
        findLineSegment(pointsCopy);
    }    

    /**
     * private method to find all line segments containing 4 points in
     * the order of growth of the running time of your program should 
     * be n4 in the worst case and it should use space proportional to 
     * n plus the number of line segments returned
     *
     *
     * @param points[] the points array
     */
    private void findLineSegment(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int m = k + 1; m < points.length; m++) {
                        // check the order of the points that is from low to hight
                        if (Double.compare(points[i].slopeTo(points[j]), points[i].slopeTo(points[k])) == 0 
                            && Double.compare(points[i].slopeTo(points[k]), points[i].slopeTo(points[m])) == 0) {
                            if (points[i].compareTo(points[j]) < 0 
                                && points[j].compareTo(points[k]) < 0 
                                && points[k].compareTo(points[m]) < 0) {
                                segList.add(new LineSegment(points[i], points[m]));
                                segNum++;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * the number of line segments
     *
     * @return the number of line segments
     */
    public int numberOfSegments() {
        return segNum;
    }       

    /**
     * the line segments
     *
     * @return array of the line segments
     */
    public LineSegment[] segments() {
        LineSegment[] segments = new LineSegment[segNum];
        int i = 0;
        for (LineSegment seg : segList) {
            segments[i++] = seg;
        }
        return segments;
    }               

    // test
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(0.001);
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdOut.println(collinear.segNum);
        StdDraw.show();
    }
}