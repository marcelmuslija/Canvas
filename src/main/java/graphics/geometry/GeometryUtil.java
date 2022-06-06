package graphics.geometry;

public class GeometryUtil {

    public static double distanceFromPoint(Point point1, Point point2) {
        int x1 = point1.getX();
        int x2 = point2.getX();
        int y1 = point1.getY();
        int y2 = point2.getY();

        return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }

    /**
     *
     * <p>The line extending the line segment is parametrized as f(t) = s + t(e - s).
     * The projection of the target point onto the line falls where t = dot(p - s, e - s).</p>
     * Clamping t in range [0, 1] handles points outside the line segment.
     *
     * @param s start point of line segment
     * @param e end point of line segment
     * @param p target point
     * @return distance between the target point and the line segment
     */
    public static double distanceFromLineSegment(Point s, Point e, Point p) {
        double lineLength = distanceFromPoint(s, e);
        if (lineLength == 0.0d)
            return distanceFromPoint(s, p);

        double t = Math.max(0d, Math.min(1d, dot(p.difference(s), e.difference(s))/(lineLength*lineLength)));
        int projX = (int) (s.getX() + t*(e.getX() - s.getX()));
        int projY = (int) (s.getY() + t*(e.getY() - s.getY()));
        Point projection = new Point(projX, projY);
        return distanceFromPoint(p, projection);
    }

    private static int dot(Point a, Point b) {
        return a.getX()*b.getX() + a.getY()*b.getY();
    }
}
