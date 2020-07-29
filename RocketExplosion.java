import javax.swing.*;
import java.awt.*;

public class RocketExplosion extends Sprite {
    private static final long serialVersionUID = 1L;

    public RocketExplosion(int xPos, int yPos) {
        super(xPos, yPos);
        loadSprite("Images/explosion.png");
    }

    public void loadSprite(String imgName) {
        ImageIcon icon = new ImageIcon(imgName);
        img = icon.getImage();
        width = img.getWidth(null);
        height = img.getHeight(null);
    }

    // In order to align hit radius of the bullet with the actual sprite.
    @Override
    public Rectangle getBoundary() {
        return new Rectangle(xpos - 40, ypos - 48, width, height); 
    }
}