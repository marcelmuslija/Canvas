package graphics.objects;

import graphics.util.GeometryUtil;
import graphics.util.Point;
import graphics.util.Rectangle;
import graphics.rendering.Renderer;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        int y = Math.min(s.getY(), e.getY());
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
        return "@LINE";
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        List<Integer> coordinates = Arrays.stream(data.split(" "))
                .map(Integer::valueOf)
                .toList();
        Point s = new Point(coordinates.get(0), coordinates.get(1));
        Point e = new Point(coordinates.get(2), coordinates.get(3));
        stack.push(new LineSegment(s, e));
    }

    @Override
    public void save(List<String> rows) {
        Point s = getHotPoint(START);
        Point e = getHotPoint(END);
        rows.add(String.format("%s %d %d %d %d", getShapeID(), s.getX(), s.getY(), e.getX(), e.getY()));
    }
}
