import javax.swing.*;
import java.awt.event.*;

public class Player2 extends Player {
    private static final long serialVersionUID = 1L;

    public Player2(int lives, int xpos, int ypos) {
        super(lives, xpos, ypos);

        loadSprite("Images/player2.png");
    }

    public void btnPress(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_F) {
            shoot();
        } else if (key == KeyEvent.VK_G) {
            pickUpOrDrop = true;
        }

    }

    public void btnRelease(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_G) {
            pickUpOrDrop = false;
        }
    }

    public void decrementLives() {
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