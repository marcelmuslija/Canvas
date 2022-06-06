package graphics.geometry;

public class GeometryUtil {

    public static double distanceFromPoint(Point point1, Point point2) {
        int x1 = point1.getX();
        int x2 = point2.getX();
        int y1 = point1.getY();
        int y2 = point2.getY();

        return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }

    public static double distanceFromLineSegment(Point s, Point e, Point p) {
        return 0.0d;
    }
}
