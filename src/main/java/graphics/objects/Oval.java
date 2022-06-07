package graphics.objects;

import graphics.geometry.GeometryUtil;
import graphics.geometry.Point;
import graphics.geometry.Rectangle;
import graphics.rendering.Renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Oval extends AbstractGraphicalObject {
    private static final int BOTTOM = 0;
    private static final int RIGHT = 1;

    public Oval() {
        this(new Point(0, 10), new Point(10, 0));
    }

    public Oval(Point bottom, Point right) {
        super(new Point[] {bottom, right});
    }

    @Override
    public Rectangle getBoundingBox() {
        Point bottom = getHotPoint(BOTTOM);
        Point right = getHotPoint(RIGHT);
        Point diff = bottom.difference(right);

        int x = bottom.getX() - diff.getX();
        int y = right.getY() - diff.getY();
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
        Oval duplicate = new Oval(getHotPoint(BOTTOM), getHotPoint(RIGHT));
        duplicate.setSelected(isSelected());
        duplicate.setHotPointSelected(BOTTOM, isHotPointSelected(BOTTOM));
        duplicate.setHotPointSelected(RIGHT, isHotPointSelected(RIGHT));
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
        Point bottom = getHotPoint(BOTTOM);
        Point right = getHotPoint(RIGHT);
        Point diff = bottom.difference(right);

        Point center = bottom.translate(new Point(0, -diff.getY()));
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
