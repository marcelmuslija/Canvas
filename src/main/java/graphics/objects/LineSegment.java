package graphics.objects;

import graphics.geometry.GeometryUtil;
import graphics.geometry.Point;
import graphics.geometry.Rectangle;
import graphics.rendering.Renderer;

import java.util.List;
import java.util.Stack;

public class LineSegment extends AbstractGraphicalObject {
    private static final int START = 0;
    private static final int END = 1;

    public LineSegment() {
        this(new Point(0, 0), new Point(10, 0));
    }

    public LineSegment(Point s, Point e) {
        super(new Point[] {s, e});
    }

    @Override
    public Rectangle getBoundingBox() {
        Point s = getHotPoint(START);
        Point e = getHotPoint(END);
        Point diff = s.difference(e);

        int x = Math.min(s.getX(), e.getX());
        int y = Math.max(s.getY(), e.getY());
        int width = diff.getX();
        int height = diff.getY();

        return new Rectangle(x, y, width, height);
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        return GeometryUtil.distanceFromLineSegment(getHotPoint(START), getHotPoint(END), mousePoint);
    }

    @Override
    public void render(Renderer r) {
        r.drawLine(getHotPoint(START), getHotPoint(END));
    }

    @Override
    public String getShapeName() {
        return "Linija";
    }

    @Override
    public GraphicalObject duplicate() {
        return new LineSegment(getHotPoint(START), getHotPoint(END));
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
}
