import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.ArrayList;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> segList;
    private boolean[] mark;

    public FastCollinearPoints(Point[] points) {
        if (points == null)         throw new IllegalArgumentException("Constructor argument Point[] is null");
        for (int i = 0; i < points.length; i++)
            if (points[i] == null)  throw new IllegalArgumentException("there is null in Constructor argument");
        Point[] pointsCopy = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            pointsCopy[i] = points[i];
        }
        Arrays.sort(pointsCopy);
        for (int i = 0; i < pointsCopy.length - 1; i++) 
            if (pointsCopy[i].compareTo(pointsCopy[i+1]) == 0)  throw new IllegalArgumentException("points are illegal");
        segList = new ArrayList<LineSegment>();
        mark = new boolean[points.length];
        findLineSegments(pointsCopy);
    }     // finds all line segments containing 4 or more points

    private void findLineSegments(Point[] points) {
        int size = points.length;
        Point[] slopePoints = new Point[size];

        for (int i = 0; i < size; i++)  slopePoints[i] = points[i];
        for (int i = 0; i < size; i++) {
            Arrays.sort(slopePoints);
            Arrays.sort(slopePoints, points[i].slopeOrder());
            assert(slopePoints[0] == points[i]);

            int min = 1;
            int max = 1;

            while (min < size) {
                while (max < size && Double.compare(points[i].slopeTo(slopePoints[max]), points[i].slopeTo(slopePoints[min])) == 0) {
                    max++;
                }
                if (max - min > 2) {
                    Point pMin = points[i].compareTo(slopePoints[min]) < 0 ? points[i] : slopePoints[min];
                    Point pMax = slopePoints[max - 1];
                    if (points[i] == pMin)
                        segList.add(new LineSegment(pMin, pMax));
                }
                min = max;
            }
        }
    }

    public           int numberOfSegments() {
        return segList.size();
    }       // the number of line segments

    public LineSegment[] segments() {
        return segList.toArray(new LineSegment[segList.size()]);
    }               // the line segments

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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            segment.draw();
            StdOut.println(segment);
        }
        StdOut.println(collinear.numberOfSegments());
        StdDraw.show();
    }
}