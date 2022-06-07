package model;

import graphics.objects.GraphicalObject;
import graphics.objects.GraphicalObjectListener;
import graphics.util.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DocumentModel {
    public final static double SELECTION_PROXIMITY = 10d;

    private final List<GraphicalObject> objects = new ArrayList<>();
    private final List<GraphicalObject> roObjects = Collections.unmodifiableList(objects);
    private final List<DocumentModelListener> listeners = new ArrayList<>();
    private final List<GraphicalObject> selectedObjects = new ArrayList<>();
    private final List<GraphicalObject> roSelectedObjects = Collections.unmodifiableList(selectedObjects);

    private final GraphicalObjectListener goListener = new GraphicalObjectListener() {
        @Override
        public void graphicalObjectChanged(GraphicalObject go) {
            notifyListeners();
        }

        @Override
        public void graphicalObjectSelectionChanged(GraphicalObject go) {
            if (go.isSelected()) {
                selectedObjects.add(go);
            } else {
                selectedObjects.remove(go);
            }

            notifyListeners();
        }
    };

    public DocumentModel() {

    }

    public void clear() {
        for (GraphicalObject go : objects) {
            removeGraphicalObject(go);
        }
    }

    public void addGraphicalObject(GraphicalObject obj) {
        obj.addGraphicalObjectListener(goListener);
        objects.add(obj);
        notifyListeners();
    }

    public void removeGraphicalObject(GraphicalObject obj) {
        obj.removeGraphicalObjectListener(goListener);
        objects.remove(obj);
        notifyListeners();
    }

    public List<GraphicalObject> list() {
        return roObjects;
    }

    public void addDocumentModelListener(DocumentModelListener l) {
        listeners.add(l);
    }

    public void removeDocumentModelListener(DocumentModelListener l) {
        listeners.remove(l);
    }

    public void notifyListeners() {
        listeners.forEach(DocumentModelListener::documentChange);
    }

    public List<GraphicalObject> getSelectedObjects() {
        return roSelectedObjects;
    }

    public void increaseZ(GraphicalObject go) {
        int index = objects.indexOf(go);
        if (index < 0 || index == objects.size()-1)
            return;

        GraphicalObject next = objects.get(index+1);
        objects.set(index, next);
        objects.set(index+1, go);

        notifyListeners();
    }

    public void decreaseZ(GraphicalObject go) {
        int index = objects.indexOf(go);
        if (index <= 0)
            return;

        GraphicalObject previous = objects.get(index-1);
        objects.set(index, previous);
        objects.set(index-1, go);

        notifyListeners();
    }

    public GraphicalObject findSelectedGraphicalObject(Point mousePoint) {
        double minSelectionDistance = SELECTION_PROXIMITY;
        GraphicalObject selectedObject = null;

        for (GraphicalObject go : objects) {
            double selectionDistance = go.selectionDistance(mousePoint);
            if (selectionDistance < minSelectionDistance) {
                selectedObject = go;
                minSelectionDistance = selectionDistance;
            }
        }
        return selectedObject;
    }

    public int findSelectedHotPoint(GraphicalObject object, Point mousePoint) {
        int numberOfHotPoints = object.getNumberOfHotPoints();
        int selectedHotPointIndex = -1;
        double minDistanceFromHotPoint = SELECTION_PROXIMITY;
        for (int index = 0; index < numberOfHotPoints; index++) {
            double distanceFromHotPoint = object.getHotPointDistance(index, mousePoint);
            if (distanceFromHotPoint < minDistanceFromHotPoint) {
                minDistanceFromHotPoint = distanceFromHotPoint;
                selectedHotPointIndex = index;
            }
        }

        return selectedHotPointIndex;
    }
}
