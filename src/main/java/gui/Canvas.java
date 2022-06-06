package gui;

import model.DocumentModel;

import javax.swing.JComponent;
import java.awt.Graphics;

public class Canvas extends JComponent {
    private final DocumentModel model;

    public Canvas(DocumentModel model) {
        this.model = model;
    }

    @Override
    protected void paintComponent(Graphics g) {

    }
}
