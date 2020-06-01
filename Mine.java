import javax.swing.*;
import java.awt.*;

public class Mine extends Sprite {
    private static final long serialVersionUID = 1L;

    public Mine(int xPos, int yPos) {
        super(xPos, yPos);
        loadSprite("Images/mineWithoutWhite.png");
    }

    public void loadSprite(String imgName) {
        ImageIcon icon = new ImageIcon(imgName);
        img = icon.getImage();
        width = img.getWidth(null);
        height = img.getHeight(null);
    }

    public void makeInvisible() {
        visible = false;
    }

    // Align activation radius with the sprite.
    @Override
    public Rectangle getBoundary() {
        return new Rectangle(xpos, ypos, width, height);
    }
}