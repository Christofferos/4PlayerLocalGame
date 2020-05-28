import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class LightningBlock implements Serializable {
    private static final long serialVersionUID = 1L;
    private int xpos;
    private int ypos;
    private int height;
    private int width;
    private transient Image img;

    public LightningBlock(int xpos, int ypos) {
        this.xpos = xpos;
        this.ypos = ypos;

        loadSprite();
    }

    public int getXpos() {
        return xpos;
    }

    public int getYpos() {
        return ypos;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Image getImage() {
        return img;
    }

    public Rectangle getBoundary() {
        return new Rectangle(xpos, ypos, width, height);
    }

    public void loadSprite() {
        ImageIcon img_fire = new ImageIcon("Images/lightning.png");
        img = img_fire.getImage();
        width = img.getWidth(null);
        height = img.getHeight(null);
    }
}