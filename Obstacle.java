import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Obstacle implements Serializable {
    private static final long serialVersionUID = 1L;
    private int xpos;
    private int ypos;
    private int height;
    private int width;
    private transient Image img;
    private boolean movable;
    private int size;

    public Obstacle(int xpos, int ypos, boolean movable, int size) {
        this.xpos = xpos;
        this.ypos = ypos;
        this.movable = movable;
        this.size = size;

        loadSprite();
    }

    public int getXpos() {
        return xpos;
    }

    public int getYpos() {
        return ypos;
    }

    public boolean movable() {
        return movable;
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
        ImageIcon img_obstacle;
        if (size == 24 && movable()) {
            img_obstacle = new ImageIcon("Images/movableObstacle.png");
        } else if (size == 30 && movable()) {
            img_obstacle = new ImageIcon("Images/movableObstacle10.png");
        } else if (size == 36 && movable()) {
            img_obstacle = new ImageIcon("Images/movableObstacle12.png");
        } else if (!movable()) {
            img_obstacle = new ImageIcon("Images/obstacle.png");
        } else {
            img_obstacle = new ImageIcon("Images/movableObstacle14.png");
        }
        img = img_obstacle.getImage();
        width = img.getWidth(null);
        height = img.getHeight(null);
    }
}