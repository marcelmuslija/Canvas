package gui;

import graphics.objects.GraphicalObject;
import graphics.objects.LineSegment;
import graphics.objects.Oval;
import model.DocumentModel;
import states.*;

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

        Canvas canvas = new Canvas(model, this);

        JToolBar toolBar = new JToolBar();
        for (GraphicalObject go : objects) {
            JButton goButton = new JButton(new AbstractAction(go.getShapeName()) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setCurrentState(new AddShapeState(go, model));
                }
            });

            toolBar.add(goButton);
        }

        JButton selectButton = new JButton(new AbstractAction("Selektiraj") {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentState = new SelectShapeState(model);
            }
        });
        toolBar.add(selectButton);

        JButton eraseButton = new JButton(new AbstractAction("Brisalo") {
            @Override
            public void actionPerformed(ActionEvent e) {
                setCurrentState(new EraserState());
            }
        });
        toolBar.add(eraseButton);

        cp.add(toolBar, BorderLayout.PAGE_START);
        cp.add(canvas, BorderLayout.CENTER);
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State state) {
        currentState.onLeaving();
        currentState = state;
    }

    public static void main(String[] args) {
        List<GraphicalObject> gos = List.of(
                new LineSegment(),
                new Oval()
        );
        SwingUtilities.invokeLater(() -> {
            new GUI(gos).setVisible(true);
        });
    }
}
