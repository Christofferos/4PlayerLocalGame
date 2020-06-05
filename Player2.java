import javax.swing.*;
import java.awt.event.*;
import java.util.Map;

public class Player2 extends Player {
    private static final long serialVersionUID = 1L;

    public Player2(int id, int lives, int xpos, int ypos) {
        super(id, lives, xpos, ypos);

        loadSprite("Images/player2.png");
    }

    public Map<String, Integer> getKeys() {
        return Map.of("DOWN", KeyEvent.VK_S, "UP", KeyEvent.VK_W, "LEFT", KeyEvent.VK_A, "RIGHT", KeyEvent.VK_D);
    }

    public void btnPress(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_1) {
            shoot();
        } else if (key == KeyEvent.VK_Q) {
            pickUpOrDrop = true;
        }

    }

    public void btnRelease(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_Q) {
            pickUpOrDrop = false;
        }
    }

    public void decrementLives() {
        if (!dead)
            playSoundEffect();
        lives--;
        loadSprite("Images/playerAttacked.png");
        Timer backToNormalColor = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                loadSprite("Images/player2.png");
                ((Timer) e.getSource()).stop();
            }
        });
        backToNormalColor.start();
    }

}