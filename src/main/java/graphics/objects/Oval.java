package graphics.objects;

import graphics.util.GeometryUtil;
import graphics.util.Point;
import graphics.util.Rectangle;
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
    public void setHotPoint(int index, Point point) {
        super.setHotPoint(index, point);
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
        return new Oval(getHotPoint(BOTTOM), getHotPoint(RIGHT));
    }

    @Override
    public String getShapeID() {
        return "@OVAL";
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
