package graphics.objects;

import graphics.rendering.Renderer;
import graphics.util.GeometryUtil;
import graphics.util.Point;
import graphics.util.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CompositeShape extends AbstractGraphicalObject {

    private final List<GraphicalObject> objects = new ArrayList<>();

    @Override
    public int getNumberOfHotPoints() {
        return 0;
    }

    @Override
    public void translate(Point delta) {
        objects.forEach(go -> go.translate(delta));
    }

    @Override
    public Rectangle getBoundingBox() {
        if (objects.isEmpty())
            return new Rectangle(0,0, 0, 0);

        int leftMostX = Integer.MAX_VALUE;
        int topMostY = Integer.MAX_VALUE;
        int rightMostX = -1;
        int bottomMostY = -1;
        for (GraphicalObject go : objects) {
            Rectangle boundingBox = go.getBoundingBox();
            int boundX = boundingBox.getX();
            int boundY = boundingBox.getY();
            int boundWidth = boundingBox.getWidth();
            int boundHeight = boundingBox.getHeight();

            if (boundX < leftMostX)
                leftMostX = boundX;

            if (boundX + boundWidth > rightMostX)
                rightMostX = boundX + boundWidth;

            if (boundY < topMostY)
                topMostY = boundY;

            if (boundY + boundHeight > bottomMostY)
                bottomMostY = boundY + boundHeight;
        }

        int x = leftMostX;
        int y = topMostY;
        int width = rightMostX-leftMostX;
        int height = bottomMostY - topMostY;
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
        objects.forEach(go -> go.render(r));
    }

    @Override
    public void addGraphicalObject(GraphicalObject go) {
        objects.add(go);
        notifyListeners();
    }

    @Override
    public void removeGraphicalObject(GraphicalObject go) {
        objects.remove(go);
        notifyListeners();
    }

    @Override
    public List<GraphicalObject> getGraphicalObjects() {
        return objects;
    }

    @Override
    public String getShapeName() {
        return "Composite";
    }

    @Override
    public GraphicalObject duplicate() {
        GraphicalObject duplicate = new CompositeShape();
        objects.forEach(duplicate::addGraphicalObject);
        return duplicate;
    }

    @Override
    public String getShapeID() {
        return "@COMP";
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        GraphicalObject composite = new CompositeShape();
        int goCount = Integer.parseInt(data);
        while (goCount-- > 0) {
            composite.addGraphicalObject(stack.pop());
        }
        stack.push(composite);
    }

    @Override
    public void save(List<String> rows) {
        objects.forEach(o -> o.save(rows));
        rows.add(String.format("%s %d", getShapeID(), objects.size()));
    }
}
