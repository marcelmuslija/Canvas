package gui;

import graphics.util.Point;
import graphics.objects.GraphicalObject;
import graphics.rendering.G2DRendererImpl;
import graphics.rendering.Renderer;
import model.DocumentModel;
import states.IdleState;

import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;

public class Canvas extends JComponent {
    private final DocumentModel model;
    private final GUI window;

    public Canvas(DocumentModel model, GUI window) {
        this.model = model;
        this.window = window;

        model.addDocumentModelListener(this::repaint);
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
        addKeyListener(keyListener);
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Renderer r = new G2DRendererImpl(g2d);
        for (GraphicalObject go : model.list()) {
            go.render(r);
            window.getCurrentState().afterDraw(r, go);
        }
        window.getCurrentState().afterDraw(r);
    }

    private final MouseInputAdapter mouseListener = new MouseInputAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            requestFocusInWindow();
            Point mousePoint = new Point(e.getX(), e.getY());
            window.getCurrentState().mouseDown(mousePoint, e.isShiftDown(), e.isControlDown());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Point mousePoint = new Point(e.getX(), e.getY());
            window.getCurrentState().mouseUp(mousePoint, e.isShiftDown(), e.isControlDown());
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            Point mousePoint = new Point(e.getX(), e.getY());
            window.getCurrentState().mouseDragged(mousePoint);
        }
    };

    private final KeyListener keyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                window.setCurrentState(new IdleState());
                return;
            }
            window.getCurrentState().keyPressed(e.getKeyCode());
        }
    };
}
