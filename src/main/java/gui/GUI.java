package gui;

import graphics.objects.GraphicalObject;
import graphics.objects.LineSegment;
import graphics.geometry.Point;
import graphics.objects.Oval;
import model.DocumentModel;
import states.AddShapeState;
import states.IdleState;
import states.State;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.util.List;

public class GUI extends JFrame {

    private final List<GraphicalObject> objects;
    private final DocumentModel model;
    private State currentState;

    public GUI(List<GraphicalObject> objects) {
        this.objects = objects;
        this.model = new DocumentModel();
        this.currentState = new IdleState();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(500, 500);
        initGUI();
    }

    private void initGUI() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        Canvas canvas = new Canvas(model);

        JToolBar toolBar = new JToolBar();
        for (GraphicalObject go : objects) {
            JButton goButton = new JButton(new AbstractAction(go.getShapeName()) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    currentState = new AddShapeState(go, model);
                }
            });

            toolBar.add(goButton);
        }

        cp.add(toolBar, BorderLayout.PAGE_START);
        cp.add(canvas, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        List<GraphicalObject> gos = List.of(
                new LineSegment(new Point(100, 100), new Point(100, 300)),
                new Oval(new Point(250, 250), new Point(300, 225))
        );
        SwingUtilities.invokeLater(() -> {
            new GUI(gos).setVisible(true);
        });
    }
}
