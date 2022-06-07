package states;

import graphics.util.Point;
import graphics.objects.GraphicalObject;
import graphics.rendering.Renderer;
import model.DocumentModel;

public class AddShapeState implements State {

    private final GraphicalObject prototype;
    private final DocumentModel model;

    public AddShapeState(GraphicalObject prototype, DocumentModel model) {
        this.prototype = prototype;
        this.model = model;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        GraphicalObject go = prototype.duplicate();
        go.translate(mousePoint);
        model.addGraphicalObject(go);
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

    }

    @Override
    public void afterDraw(Renderer r) {

    }

    @Override
    public void onLeaving() {

    }
}
