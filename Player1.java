import javax.swing.*;
import java.awt.event.*;

public class Player1 extends Player {
    private static final long serialVersionUID = 1L;

    public Player1(int id, int lives, int xpos, int ypos) {
        super(id, lives, xpos, ypos);
        loadSprite("Images/player1.png");

        /*
        Timer updateImg = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                switch (direction) {
                    case UP:
                        loadSprite("Images/player1Up.png");
                        break;
                    case LEFT:
                        loadSprite("Images/player1Left.png");
                        break;
                    case RIGHT:
                        loadSprite("Images/player1Right.png");
                        break;
                    case DOWN:
                        loadSprite("Images/player1Down.png");
                        break;
                }
            }
        });
        updateImg.start();*/
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