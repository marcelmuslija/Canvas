package graphics.objects;

import graphics.geometry.GeometryUtil;
import graphics.geometry.Point;
import graphics.geometry.Rectangle;
import graphics.rendering.Renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Oval extends AbstractGraphicalObject {
    private static final int FIRST = 0;
    private static final int SECOND = 1;

    public Oval() {
        this(new Point(0, 10), new Point(10, 0));
    }

    public Oval(Point first, Point second) {
        super(new Point[] {first, second});
    }

    @Override
    public Rectangle getBoundingBox() {
        Point first = getHotPoint(FIRST);
        Point second = getHotPoint(SECOND);
        Point diff = first.difference(second);

        int x = first.getX() - diff.getX();
        int y = second.getY() - diff.getY();
        int width = 2*diff.getX();
        int height = 2*diff.getY();
        return new Rectangle(x, y, width, height);
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        Point[] points = getPolygonPoints();
        double minSelectionDistance = Double.MAX_VALUE;
        for (Point point : points) {
            double selectionDistance = GeometryUtil.distanceFromPoint(mousePoint, point);
            if (selectionDistance < minSelectionDistance) {
                minSelectionDistance = selectionDistance;
            }
        }
        return minSelectionDistance;
    }

    @Override
    public void render(Renderer r) {
        r.fillPolygon(getPolygonPoints());
    }

    @Override
    public String getShapeName() {
        return "Oval";
    }

    @Override
    public GraphicalObject duplicate() {
        Oval duplicate = new Oval(getHotPoint(FIRST), getHotPoint(SECOND));
        duplicate.setSelected(isSelected());
        duplicate.setHotPointSelected(FIRST, isHotPointSelected(FIRST));
        duplicate.setHotPointSelected(SECOND, isHotPointSelected(SECOND));
        return duplicate;
    }

    @Override
    public String getShapeID() {
        return null;
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {

    }

    @Override
    public void save(List<String> rows) {

    }

    private Point[] getPolygonPoints() {
        Point first = getHotPoint(FIRST);
        Point second = getHotPoint(SECOND);
        Point diff = first.difference(second);

        Point center = first.translate(new Point(0, -diff.getY()));
        int radiusX = diff.getX();
        int radiusY = diff.getY();

        List<Point> points = new ArrayList<>();
        for (double t = 0d; t < 2*Math.PI; t += 0.01) {
            int x = (int) (radiusX * Math.cos(t));
            int y = (int) (radiusY * Math.sin(t));

            points.add(new Point(center.getX()+x, center.getY()-y));
        }

        return points.toArray(new Point[0]);
    }
}
