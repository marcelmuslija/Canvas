package graphics.objects;

import graphics.geometry.GeometryUtil;
import graphics.geometry.Point;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGraphicalObject implements GraphicalObject {
    private final Point[] hotPoints;
    private final boolean[] hotPointSelected;
    private boolean selected;

    protected List<GraphicalObjectListener> listeners = new ArrayList<>();

    protected AbstractGraphicalObject(Point[] hotPoints) {
        this.hotPoints = hotPoints;
        hotPointSelected = new boolean[hotPoints.length];
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        notifySelectionListeners();
    }

    @Override
    public int getNumberOfHotPoints() {
        return hotPoints.length;
    }

    @Override
    public Point getHotPoint(int index) {
        return hotPoints[index];
    }

    @Override
    public void setHotPoint(int index, Point point) {
        hotPoints[index] = point;
    }

    @Override
    public boolean isHotPointSelected(int index) {
        return hotPointSelected[index];
    }

    @Override
    public void setHotPointSelected(int index, boolean selected) {
        hotPointSelected[index] = selected;
    }

    @Override
    public double getHotPointDistance(int index, Point mousePoint) {
        return GeometryUtil.distanceFromPoint(mousePoint, hotPoints[index]);
    }

    @Override
    public void translate(Point delta) {
        for (int i = 0; i < hotPoints.length; i++) {
            hotPoints[i] = hotPoints[i].translate(delta);
        }
    }

    @Override
    public void addGraphicalObjectListener(GraphicalObjectListener l) {
        listeners.add(l);
    }

    @Override
    public void removeGraphicalObjectListener(GraphicalObjectListener l) {
        listeners.remove(l);
    }

    protected void notifyListeners() {
        listeners.forEach(l -> l.graphicalObjectChanged(this));
    }

    protected void notifySelectionListeners() {
        listeners.forEach(l -> l.graphicalObjectSelectionChanged(this));
    }
}
