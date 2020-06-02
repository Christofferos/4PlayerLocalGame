import javax.swing.*;
import java.awt.event.*;

public class PowerUp extends Sprite {
    private static final long serialVersionUID = 1L;
    int type;

    public PowerUp(int xpos, int ypos, int type) {
        super(xpos, ypos);
        this.type = type;
        if (type == 1) {
            loadSprite("Images/strengthBoost.png"); // Increase maximum health cap.
        } else if (type == 2) {
            loadSprite("Images/fireBoost3.png"); // Decreases reload time.
        } else if (type == 3) {
            loadSprite("Images/movementBoost2.png"); // Increases inventory cap and size of obstacles.
        } else if (type == 4) {
            loadSprite("Images/minigun.png"); // 7 seconds auto minigun - decreases reload time. <<< Not yet implemented
        } else if (type == 5) {
            loadSprite("Images/sniper.png"); // 3 Ammo - Increases bullet speed. <<< Not yet implemented
        } else if (type == 6) {
            loadSprite("Images/rocket.png"); // Idle state while manuvering rocket - explodes at collision (even with bullets). <<< Not yet implemented
        } else if (type == 7) {
            loadSprite("Images/mine.png"); // Drops a mine that becomes invisible - explodes at collision with player. <<< Not yet implemented
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