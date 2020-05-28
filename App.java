
// Shift + Alt + A  To do a multiline comment
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class App extends JFrame {
    private static final long serialVersionUID = 1L;
    public Battletronics battletronics;

    public App(int nrOfPlayers) {
        int height = 315;
        int width = 275;
        int windowHeight = 335;
        int windowWidth = 275;

        battletronics = new Battletronics(width, height, nrOfPlayers);
        add(battletronics);

        setSize(windowWidth, windowHeight);
        setTitle("Battletronics");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            App battle = new App(2);
            battle.setVisible(true);
        });
    }

}
