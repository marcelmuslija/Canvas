package graphics.geometry;

import graphics.AbstractGraphicalObject;
import graphics.GraphicalObject;
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
        int y = right.getY() + diff.getY();
        int width = 2*diff.getX();
        int height = 2*diff.getY();
        return new Rectangle(x, y, width, height);
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        Rectangle boundingBox = getBoundingBox();
        int x = boundingBox.getX();
        int y = boundingBox.getY();
        int width = boundingBox.getWidth();
        int height = boundingBox.getHeight();

        Point topLeft = new Point(x, y);
        Point topRight = new Point(x + width, y);
        Point bottomLeft = new Point(x, y + height);
        Point bottomRight = new Point(x + width, y + height);

        List<LineSegment> borders = new ArrayList<>();
        borders.add(new LineSegment(topLeft, topRight));
        borders.add(new LineSegment(bottomLeft, bottomRight));
        borders.add(new LineSegment(bottomLeft, bottomRight));
        borders.add(new LineSegment(bottomLeft, bottomRight));

        double minSelectionDistance = Double.MAX_VALUE;
        for (LineSegment border : borders) {
            double selectionDistance = border.selectionDistance(mousePoint);
            if (selectionDistance < minSelectionDistance)
                minSelectionDistance = selectionDistance;
        }
        return minSelectionDistance;
    }

    @Override
    public void render(Renderer r) {

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
}
