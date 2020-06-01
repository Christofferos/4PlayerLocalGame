import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PowerUpSniper {
    Timer deactivate;
    int ammo = 5;
    boolean once = false;

    public PowerUpSniper(Player player) {
        if (player.bulletSpeed < 6) {
            player.bulletSpeed *= 2.0;
            player.bulletSound = Player.BulletSound.SNIPER;
        }

        deactivate = new Timer(100, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (player.reload && !once) {
                    once = true;
                    ammo--;
                    decrementAmmoOnce();
                }
                if (ammo == 0) {
                    player.bulletSpeed = 2;
                    player.bulletSound = Player.BulletSound.RIFLE;
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        deactivate.start();
    }

    public void decrementAmmoOnce() {
        Timer decrementTimer = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                once = false;
                ((Timer) e.getSource()).stop();
            }
        });
        decrementTimer.setInitialDelay(300);
        decrementTimer.setRepeats(false);
        decrementTimer.start();
    }

}