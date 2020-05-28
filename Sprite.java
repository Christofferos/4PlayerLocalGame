import java.awt.*;
import java.io.Serializable;

// Player and Bullet shares a lot of functions.
public class Sprite implements Serializable {
    private static final long serialVersionUID = 1L;
    protected int xpos;
    protected int ypos;
    protected int width;
    protected int height;
    protected boolean visible;
    protected transient Image img;

    public Sprite(int x, int y) {
        this.xpos = x;
        this.ypos = y;
        visible = true;
    }

    public Image getImage() {
        return img;
    }

    public int getXpos() {
        return xpos;
    }

    public int getYpos() {
        return ypos;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Rectangle getBoundary() {
        return new Rectangle(xpos, ypos, width, height);
    }
}