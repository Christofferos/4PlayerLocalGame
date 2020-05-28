import javax.swing.*;
import java.awt.event.*;

public class Player1 extends Player {
    private static final long serialVersionUID = 1L;

    public Player1(int lives, int xpos, int ypos) {
        super(lives, xpos, ypos);
        loadSprite("Images/player1.png");
    }

    public void btnPress(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_BACK_SPACE) {
            shoot();
        } else if (key == KeyEvent.VK_DELETE) {
            pickUpOrDrop = true;
        }
    }

    public void btnRelease(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_DELETE) {
            pickUpOrDrop = false;
        }
    }

    public void decrementLives() {
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