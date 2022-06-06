package gui;

import graphics.rendering.G2DRendererImpl;
import graphics.rendering.Renderer;
import model.DocumentModel;
import model.DocumentModelListener;

import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Canvas extends JComponent {
    private final DocumentModel model;
    private final DocumentModelListener modelListener = this::repaint;

    public Canvas(DocumentModel model) {
        this.model = model;
        model.addDocumentModelListener(modelListener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Renderer r = new G2DRendererImpl(g2d);
        model.list().forEach(go -> go.render(r));
    }
}
