package graphics;

import graphics.geometry.Point;
import graphics.geometry.Rectangle;
import graphics.rendering.Renderer;

import java.util.List;
import java.util.Stack;

public interface GraphicalObject {

    //support for object editing
    boolean isSelected();
    void setSelected(boolean selected);
    int getNumberOfHotPoints();
    Point getHotPoint(int index);
    void setHotPoint(int index, Point point);
    boolean isHotPointSelected(int index);
    void setHotPointSelected(int index, boolean selected);
    double getHotPointDistance(int index, Point mousePoint);

    //geometric operations
    void translate(Point delta);
    Rectangle getBoundingBox();
    double selectionDistance(Point mousePoint);

    //drawing support
    void render(Renderer r);

    //for publishing changes to the model
    void addGraphicalObjectListener(GraphicalObjectListener l);
    void removeGraphicalObjectListener(GraphicalObjectListener l);

    //prototype pattern support
    String getShapeName();
    GraphicalObject duplicate();

    //support for loading and storing drawings
    String getShapeID();
    void load(Stack<GraphicalObject> stack, String data);
    void save(List<String> rows);
}
