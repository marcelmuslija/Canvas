package states;

import graphics.objects.CompositeShape;
import graphics.util.Point;
import graphics.util.Rectangle;
import graphics.objects.GraphicalObject;
import graphics.rendering.Renderer;
import model.DocumentModel;

import java.awt.event.KeyEvent;
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
        GraphicalObject selected = model.findSelectedGraphicalObject(mousePoint);
        if (selected == null)
            return;

        int selectedHotPointIndex = model.findSelectedHotPoint(selected, mousePoint);
        if (selectedHotPointIndex >= 0)
            selected.setHotPoint(selectedHotPointIndex, mousePoint);
    }

    @Override
    public void keyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP -> translate(new Point(0, -1));
            case KeyEvent.VK_DOWN -> translate(new Point(0, 1));
            case KeyEvent.VK_LEFT -> translate(new Point(-1, 0));
            case KeyEvent.VK_RIGHT -> translate(new Point(1, 0));
            case KeyEvent.VK_PLUS, KeyEvent.VK_ADD -> moveCloser();
            case KeyEvent.VK_MINUS, KeyEvent.VK_SUBTRACT -> moveFurther();
            case KeyEvent.VK_G -> group();
            case KeyEvent.VK_U -> ungroup();
        };
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
        List<GraphicalObject> unselect = new ArrayList<>(model.getSelectedObjects());
        unselect.forEach(go -> go.setSelected(false));
    }


    private void translate(Point delta) {
        model.getSelectedObjects().forEach(go -> go.translate(delta));
    }

    private void moveCloser() {
        List<GraphicalObject> selectedObjects = model.getSelectedObjects();
        if (selectedObjects.size() != 1)
            return;

        GraphicalObject go = selectedObjects.get(0);
        model.increaseZ(go);
    }

    private void moveFurther() {
        List<GraphicalObject> selectedObjects = model.getSelectedObjects();
        if (selectedObjects.size() != 1)
            return;

        GraphicalObject go = selectedObjects.get(0);
        model.decreaseZ(go);
    }

    private void group() {
        List<GraphicalObject> selectedObjects = model.getSelectedObjects();
        if (selectedObjects.isEmpty())
            return;

        GraphicalObject composite = new CompositeShape();
        selectedObjects.forEach(composite::addGraphicalObject);

        List<GraphicalObject> unselect = new ArrayList<>(selectedObjects);
        unselect.forEach(model::removeGraphicalObject);
        model.addGraphicalObject(composite);
    }

    private void ungroup() {
        List<GraphicalObject> selectedObjects = model.getSelectedObjects();
        if (selectedObjects.size() != 1)
            return;

        GraphicalObject composite = selectedObjects.get(0);
        if (!(composite instanceof CompositeShape))
            return;

        model.removeGraphicalObject(composite);
        composite.getGraphicalObjects().forEach(go -> {
            model.addGraphicalObject(go);
            go.setSelected(true);
        });
    }
}
