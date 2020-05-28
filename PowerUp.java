import javax.swing.*;
import java.awt.event.*;

public class PowerUp extends Sprite {
    private static final long serialVersionUID = 1L;
    int type;

    public PowerUp(int xpos, int ypos, int type) {
        super(xpos, ypos);
        this.type = type;
        if (type == 1) {
            loadSprite("Images/strengthBoost.png");
        } else if (type == 2) {
            loadSprite("Images/fireBoost3.png");
        } else if (type == 3) {
            loadSprite("Images/movementBoost2.png");
        }
    }

    public int getType() {
        return type;
    }

    public void loadSprite(String imgName) {
        ImageIcon icon = new ImageIcon(imgName);
        img = icon.getImage();
        width = img.getWidth(null);
        height = img.getHeight(null);
    }
}