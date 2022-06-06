package gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GUI extends JFrame {

    public GUI() {
        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GUI().setVisible(true);
        });
    }
}
