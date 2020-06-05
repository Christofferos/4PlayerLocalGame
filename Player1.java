import javax.swing.*;
import java.awt.event.*;
import java.util.Map;

public class Player1 extends Player {
    private static final long serialVersionUID = 1L;

    public Player1(int id, int lives, int xpos, int ypos) {
        super(id, lives, xpos, ypos);
        loadSprite("Images/player1.png");
    }

    public Map<String, Integer> getKeys() {
        return Map.of("DOWN", KeyEvent.VK_DOWN, "UP", KeyEvent.VK_UP, "LEFT", KeyEvent.VK_LEFT, "RIGHT",
                KeyEvent.VK_RIGHT);
    }

    public void btnPress(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_DELETE) {
            shoot();
        } else if (key == KeyEvent.VK_END) {
            pickUpOrDrop = true;
        }
    }

    public void btnRelease(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_END) {
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
                loadSprite("Images/player1.png");
                ((Timer) e.getSource()).stop();
            }
        });
        backToNormalColor.start();
    }
}