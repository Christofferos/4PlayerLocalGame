
// Shift + Alt + A  To do a multiline comment in VSC
import java.awt.EventQueue;
import javax.swing.JFrame;

public class App extends JFrame {
    private static final long serialVersionUID = 1L;
    public Battletronics battletronics;

    public App(int nrOfPlayers) {
     int height = 945;
     int width = 825;
     int windowHeight = 975; // 975
     int windowWidth = 825; // 825

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
