import javax.swing.*;
import java.awt.event.*;
import java.util.Map;

public class Player4 extends Player {
    private static final long serialVersionUID = 1L;

    public Player4(int id, int lives, int xpos, int ypos) {
        super(id, lives, xpos, ypos);

        loadSprite("Images/player4.png");
    }

    public Map<String, Integer> getKeys() {
        return Map.of("DOWN", KeyEvent.VK_J, "UP", KeyEvent.VK_U, "LEFT", KeyEvent.VK_H, "RIGHT", KeyEvent.VK_K);
    }

    public void btnPress(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_PERIOD) {
            shoot();
        } else if (key == KeyEvent.VK_MINUS) {
            pickUpOrDrop = true;
        }

    }

    public void btnRelease(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_MINUS) {
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
                loadSprite("Images/player4.png");
                ((Timer) e.getSource()).stop();
            }
        });
        backToNormalColor.start();
    }

}