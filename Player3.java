import javax.swing.*;
import java.awt.event.*;

public class Player3 extends Player {
    private static final long serialVersionUID = 1L;

    public Player3(int id, int lives, int xpos, int ypos) {
        super(id, lives, xpos, ypos);

        loadSprite("Images/player3.png");
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