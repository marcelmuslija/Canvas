package gui;

import graphics.GraphicalObject;
import graphics.geometry.LineSegment;
import graphics.geometry.Oval;
import model.DocumentModel;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Container;
import java.util.List;

public class GUI extends JFrame {

    private final List<GraphicalObject> objects;

    public GUI(List<GraphicalObject> objects) {
        this.objects = objects;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(500, 500);
        initGUI();
    }

    private void initGUI() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        JToolBar toolBar = new JToolBar();
        objects.forEach(go -> toolBar.add(new JButton(go.getShapeName())));

        Canvas canvas = new Canvas(new DocumentModel());

        cp.add(toolBar, BorderLayout.PAGE_START);
        cp.add(canvas, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        List<GraphicalObject> gos = List.of(new Oval(), new LineSegment());
        SwingUtilities.invokeLater(() -> {
            new GUI(gos).setVisible(true);
        });
    }
}
