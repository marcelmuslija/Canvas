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

    public Oval(Point bottom, Point right) {
        super(new Point[] {bottom, right});
    }

    @Override
    public Rectangle getBoundingBox() {
        return null;
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        Rectangle boundingBox = getBoundingBox();
        int mouseX = mousePoint.getX();
        int mouseY = mousePoint.getY();
        int x = boundingBox.getX();
        int y = boundingBox.getY();
        int width = boundingBox.getWidth();
        int height = boundingBox.getHeight();

        if (mouseX >= x && mouseX <= x+width && mouseY >= y && mouseY <= y+height)
            return 0.0d;

        Point topLeft = new Point(x, y);
        Point topRight = new Point(x+width, y);
        Point bottomLeft = new Point(x, y+height);
        Point bottomRight = new Point(x+width, y+height);

        double d1 = GeometryUtil.distanceFromLineSegment(topLeft, topRight, mousePoint);
        double d2 = GeometryUtil.distanceFromLineSegment(topLeft, bottomLeft, mousePoint);
        double d3 = GeometryUtil.distanceFromLineSegment(bottomLeft, bottomRight, mousePoint);
        double d4 = GeometryUtil.distanceFromLineSegment(topRight, bottomRight, mousePoint);

        return Math.min(d1, Math.min(d2, Math.min(d3, d4)));
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
        return new Oval(getHotPoint(FIRST), getHotPoint(SECOND));
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
