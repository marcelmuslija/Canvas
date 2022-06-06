package gui;

import graphics.geometry.Point;
import graphics.rendering.G2DRendererImpl;
import graphics.rendering.Renderer;
import model.DocumentModel;
import states.State;

import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public class Canvas extends JComponent {
    private final DocumentModel model;
    private State state;

    public Canvas(DocumentModel model) {
        this.model = model;
        model.addDocumentModelListener(this::repaint);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Renderer r = new G2DRendererImpl(g2d);
        model.list().forEach(go -> go.render(r));
    }

    private final MouseInputAdapter mouseListener = new MouseInputAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            Point mousePoint = new Point(e.getX(), e.getY());
            state.mouseDown(mousePoint, e.isShiftDown(), e.isControlDown());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Point mousePoint = new Point(e.getX(), e.getY());
            state.mouseUp(mousePoint, e.isShiftDown(), e.isControlDown());
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            Point mousePoint = new Point(e.getX(), e.getY());
            state.mouseDragged(mousePoint);
        }
    };
}
