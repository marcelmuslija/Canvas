package states;

import graphics.objects.CompositeShape;
import graphics.objects.GraphicalObject;
import graphics.objects.LineSegment;
import graphics.rendering.Renderer;
import graphics.util.Point;
import model.DocumentModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EraserState implements State {
    private final DocumentModel model;
    private final GraphicalObject eraser;
    private final Set<GraphicalObject> toErase;

    public EraserState(DocumentModel model) {
        this.model = model;
        this.eraser = new CompositeShape();
        this.toErase = new HashSet<>();
        model.addGraphicalObject(eraser);
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {

    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        List<GraphicalObject> trace = new ArrayList<>(eraser.getGraphicalObjects());
        trace.forEach(eraser::removeGraphicalObject);
        toErase.forEach(model::removeGraphicalObject);
        toErase.clear();
    }

    @Override
    public void mouseDragged(Point mousePoint) {
        List<GraphicalObject> trace = eraser.getGraphicalObjects();
        if (trace.isEmpty()) {
            eraser.addGraphicalObject(new LineSegment(mousePoint, mousePoint));
            return;
        }

        int lineEndHotPointIndex = 1;
        Point traceEnd = trace.get(trace.size()-1).getHotPoint(lineEndHotPointIndex);
        eraser.addGraphicalObject(new LineSegment(traceEnd, mousePoint));

        GraphicalObject go = model.findSelectedGraphicalObject(mousePoint);
        if (go != null && go != eraser)
            toErase.add(go);
    }

    @Override
    public void keyPressed(int keyCode) {

    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {

    }

    @Override
    public void afterDraw(Renderer r) {

    }

    @Override
    public void onLeaving() {
        model.removeGraphicalObject(eraser);
    }
}
