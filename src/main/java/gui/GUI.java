package gui;

import graphics.objects.CompositeShape;
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
import java.util.*;

public class GUI extends JFrame {
    private static final Map<String, GraphicalObject> prototypes = new HashMap<>();

    static {
        prototypes.put("@LINE", new LineSegment());
        prototypes.put("@OVAL", new Oval());
        prototypes.put("@COMP", new CompositeShape());
    }

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

        JButton loadButton = new JButton(new AbstractAction("Učitaj") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filename = askLoadFilename();
                if (filename == null)
                    return;

                Stack<GraphicalObject> stack = new Stack<>();
                try {
                    List<String> rows = Files.readAllLines(Path.of(filename));
                    for (String row : rows) {
                        String id = row.substring(0, row.indexOf(" "));
                        GraphicalObject prototype = prototypes.get(id);
                        prototype.load(stack, row.substring(row.indexOf(" ")+1));
                    }
                    stack.forEach(model::addGraphicalObject);
                } catch (IOException ex) {
                    showLoadError(filename);
                }
            }
        });
        toolBar.add(loadButton);

        JButton saveButton = new JButton(new AbstractAction("Pohrani") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filename = askSaveFilename();
                if (filename == null)
                    return;
                List<String> rows = new ArrayList<>();
                model.list().forEach(go -> go.save(rows));
                try {
                    Files.writeString(Path.of(filename), String.join("\n", rows));
                    showSaveSuccess();
                } catch (Exception ex) {
                    showSaveError(filename);
                }
            }
        });
        toolBar.add(saveButton);

        JButton svgExportButton = new JButton(new AbstractAction("SVG Export") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filename = askSaveFilename();
                if (filename == null)
                    return;

                SVGRendererImpl r = new SVGRendererImpl(filename);
                model.list().forEach(go -> go.render(r));
                try {
                    r.close();
                    showSaveSuccess();
                } catch (Exception ex) {
                    showSaveError(filename);
                }
            }
        });
        toolBar.add(svgExportButton);

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

    private String askSaveFilename() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Spremi");
        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(
                    this,
                    "Neuspješno spremanje datoteke!",
                    "Upozorenje",
                    JOptionPane.WARNING_MESSAGE
            );
            return null;
        }
        return fileChooser.getSelectedFile().toPath().toAbsolutePath().toString();
    }

    private String askLoadFilename() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Učitaj");
        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(
                    this,
                    "Neuspješno učitavanje datoteke!",
                    "Upozorenje",
                    JOptionPane.WARNING_MESSAGE
            );
            return null;
        }
        return fileChooser.getSelectedFile().toPath().toAbsolutePath().toString();
    }

    private void showSaveSuccess() {
        JOptionPane.showMessageDialog(
                GUI.this,
                "Datoteka spremljena!",
                "Uspjeh",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showSaveError(String filename) {
        JOptionPane.showMessageDialog(
                GUI.this,
                "Pogreška prilikom spremanja datoteke " + filename + ".\n" +
                        "Upozorenje: datoteka je u nekonzistentnom stanju!",
                "Pogreška",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void showLoadError(String filename) {
        JOptionPane.showMessageDialog(
                GUI.this,
                "Pogreška prilikom učitavanja datoteke " + filename + ".\n",
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
