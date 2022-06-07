package gui;

import graphics.objects.GraphicalObject;
import graphics.objects.LineSegment;
import graphics.objects.Oval;
import graphics.rendering.SVGRendererImpl;
import model.DocumentModel;
import states.*;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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
                setCurrentState(new SelectShapeState(model));
            }
        });
        toolBar.add(selectButton);

        JButton eraseButton = new JButton(new AbstractAction("Brisalo") {
            @Override
            public void actionPerformed(ActionEvent e) {
                setCurrentState(new EraserState(model));
            }
        });
        toolBar.add(eraseButton);

        JButton svgExportButton = new JButton(new AbstractAction("SVG Export") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filename = askFilename();
                if (filename == null)
                    return;

                SVGRendererImpl r = new SVGRendererImpl(filename);
                model.list().forEach(go -> go.render(r));
                try {
                    r.close();
                    showSuccess();
                } catch (Exception ex) {
                    showError(filename);
                }
            }
        });
        toolBar.add(svgExportButton);

        JButton saveButton = new JButton(new AbstractAction("Pohrani") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filename = askFilename();
                if (filename == null)
                    return;
                List<String> rows = new ArrayList<>();
                model.list().forEach(go -> go.save(rows));
                try {
                    Files.writeString(Path.of(filename), String.join("\n", rows));
                    showSuccess();
                } catch (Exception ex) {
                    showError(filename);
                }
            }
        });
        toolBar.add(saveButton);

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

    private String askFilename() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("SVG Export");
        if (fileChooser.showSaveDialog(GUI.this) != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(
                    this,
                    "Neuspješno stvaranje SVG datoteke!",
                    "Upozorenje",
                    JOptionPane.WARNING_MESSAGE
            );
            return null;
        }
        return fileChooser.getSelectedFile().toPath().toAbsolutePath().toString();
    }

    private void showSuccess() {
        JOptionPane.showMessageDialog(
                GUI.this,
                "Datoteka spremljena!",
                "Uspjeh",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showError(String filename) {
        JOptionPane.showMessageDialog(
                GUI.this,
                "Pogreška prilikom spremanja datoteke " + filename + ".\n" +
                        "Upozorenje: datoteka je u nekonzistentnom stanju!",
                "Pogreška",
                JOptionPane.ERROR_MESSAGE
        );
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
