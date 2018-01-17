import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;

public class PointSET {
    private SET<Point2D> pointSet;
    public         PointSET() {
        pointSet = new SET<Point2D>();
    }                              
    // construct an empty set of points 

    public           boolean isEmpty() {
        return pointSet.isEmpty();
    }                     
    // is the set empty? 

    public               int size() {
        return pointSet.size();
    }                         
    // number of points in the set 

    public              void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("the point is null");
        pointSet.add(p);
    }              
    // add the point to the set (if it is not already in the set)

    public           boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("the point is null");
        return pointSet.contains(p);
    }            
    // does the set contain point p? 

    public              void draw() {
        if (isEmpty())
            return;
        for (Point2D p : pointSet) {
            p.draw();
        }
    }                        
    // draw all points to standard draw 

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("the rect is null");

        ArrayList<Point2D> collect = new ArrayList<Point2D>();
        for (Point2D p : pointSet)
            if (rect.contains(p))
                collect.add(p);

        return collect;
    }            
    // all points that are inside the rectangle (or on the boundary) 

    public           Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("the point is null");
        if (pointSet == null)
            return null;

        double nearestDistance = Double.POSITIVE_INFINITY;
        Point2D nearest = null;
        for (Point2D points : pointSet) {
            if (points.distanceTo(p) == 0)
                continue;
            if (points.distanceTo(p) < nearestDistance) {
                nearestDistance = points.distanceTo(p);
                nearest = points;
            }
        }

        return nearest;
    }             
    // a nearest neighbor in the set to point p; null if the set is empty 

    public static void main(String[] args) {
    }                  
    // unit testing of the methods (optional) 
}