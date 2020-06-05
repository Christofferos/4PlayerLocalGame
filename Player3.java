import javax.swing.*;
import java.awt.event.*;
import java.util.Map;

public class Player3 extends Player {
    private static final long serialVersionUID = 1L;

    public Player3(int id, int lives, int xpos, int ypos) {
        super(id, lives, xpos, ypos);
        loadSprite("Images/player3.png");
    }

    public Map<String, Integer> getKeys() {
        return Map.of("DOWN", KeyEvent.VK_NUMPAD5, "UP", KeyEvent.VK_NUMPAD8, "LEFT", KeyEvent.VK_NUMPAD4, "RIGHT",
                KeyEvent.VK_NUMPAD6);
    }

    public void btnPress(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_DIVIDE) {
            shoot();
        } else if (key == KeyEvent.VK_MULTIPLY) {
            pickUpOrDrop = true;
        }

    }

    public void btnRelease(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_MULTIPLY) {
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
                loadSprite("Images/player3.png");
                ((Timer) e.getSource()).stop();
            }
        });
        backToNormalColor.start();
    }

}