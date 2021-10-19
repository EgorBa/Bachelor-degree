package model;

public class Point {
    public double x;
    public double y;

    Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point point = (Point) obj;
            return point.x == x && point.y == y;
        }
        return false;
    }

    @Override
    public String toString() {
        return "model.Point (x : " + x + ", y : " + y + ")";
    }

    public double dist(Point p) {
        return Math.sqrt((x - p.x) * (x - p.x) + (y - p.y) * (y - p.y));
    }

}
