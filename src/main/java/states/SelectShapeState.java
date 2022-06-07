package states;

import graphics.geometry.Point;
import graphics.geometry.Rectangle;
import graphics.objects.GraphicalObject;
import graphics.rendering.Renderer;
import model.DocumentModel;

import java.util.ArrayList;
import java.util.List;

public class SelectShapeState implements State {

    private final DocumentModel model;

    public SelectShapeState(DocumentModel model) {
        this.model = model;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        GraphicalObject selected = model.findSelectedGraphicalObject(mousePoint);
        if (selected == null || !ctrlDown) {
            List<GraphicalObject> unselect = new ArrayList<>(model.getSelectedObjects());
            unselect.forEach(go -> go.setSelected(false));
        }

        if (selected != null)
            selected.setSelected(true);
    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {

    }

    @Override
    public void mouseDragged(Point mousePoint) {
    }

    @Override
    public void keyPressed(int keyCode) {

    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {
        if (!go.isSelected())
            return;

        Rectangle boundingBox = go.getBoundingBox();
        int x = boundingBox.getX();
        int y = boundingBox.getY();
        int width = boundingBox.getWidth();
        int height = boundingBox.getHeight();

        Point topLeft = new Point(x, y);
        Point topRight = new Point(x+width, y);
        Point bottomLeft = new Point(x, y+height);
        Point bottomRight = new Point(x+width, y+height);

        r.drawLine(topLeft, topRight);
        r.drawLine(topLeft, bottomLeft);
        r.drawLine(bottomLeft, bottomRight);
        r.drawLine(topRight, bottomRight);
    }

    @Override
    public void afterDraw(Renderer r) {
        List<GraphicalObject> selectedObjects = model.getSelectedObjects();
        if (selectedObjects.size() != 1)
            return;

        GraphicalObject selected = selectedObjects.get(0);
        int numberOfHotPoints = selected.getNumberOfHotPoints();

        Point topLeftDelta = new Point(-2, -2);
        Point topRightDelta = new Point(2, -2);
        Point bottomLeftDelta = new Point(-2, 2);
        Point bottomRightDelta = new Point(2, 2);

        for (int i = 0; i < numberOfHotPoints; i++) {
            Point hotPoint = selected.getHotPoint(i);
            Point topLeft = hotPoint.translate(topLeftDelta);
            Point topRight = hotPoint.translate(topRightDelta);
            Point bottonLeft = hotPoint.translate(bottomLeftDelta);
            Point bottomRight = hotPoint.translate(bottomRightDelta);
            r.drawLine(topLeft, topRight);
            r.drawLine(topLeft, bottonLeft);
            r.drawLine(bottonLeft, bottomRight);
            r.drawLine(topRight, bottomRight);
        }
    }

    @Override
    public void onLeaving() {

    }
}
