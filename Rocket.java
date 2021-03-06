import javax.swing.*;
import java.awt.*;

public class Rocket extends Sprite {
    private static final long serialVersionUID = 1L;

    public Rocket(int xPos, int yPos) {
        super(xPos, yPos);
        loadSprite("Images/rocketImg.png");
    }

    public void loadSprite(String imgName) {
        ImageIcon icon = new ImageIcon(imgName);
        img = icon.getImage();
        width = img.getWidth(null);
        height = img.getHeight(null);
    }

    public void movement(Player.Direction dir, int bulletSpeed) {
        switch (dir) {
            case UP:
                ypos -= bulletSpeed;
                break;
            case DOWN:
                ypos += bulletSpeed;
                break;
            case LEFT:
                xpos -= bulletSpeed;
                break;
            case RIGHT:
                xpos += bulletSpeed;
                break;
        }

        if (xpos > 500 || xpos < 0 || ypos > 500 || ypos < 0) {
            visible = false;
        }
    }

    // In order to align hit radius of the bullet with the actual sprite.
    @Override
    public Rectangle getBoundary() {
        return new Rectangle(xpos + 1, ypos + 1, width, height);
    }
}