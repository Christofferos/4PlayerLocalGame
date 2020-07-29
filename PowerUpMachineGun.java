import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PowerUpMachineGun {
    SoundEffect soundEffect;

    public PowerUpMachineGun(Player player, ArrayList<Boolean> stopSoundEffects) {
        player.holdingMachineGun = true;
        player.bulletSound = Player.BulletSound.MACHINEGUN;
        // Sound effect
        try {
            soundEffect = new SoundEffect("Sound/machineGun.wav", stopSoundEffects);
            soundEffect.play();
        } catch (Exception ex) {
            System.out.println("Soundtrack not found");
            ex.printStackTrace();
        }

        int fireFreq = player.reloadFreq - 50;
        if (player.reloadFreq < 150)
            fireFreq = 100;
        Timer machineGunFireRate = new Timer(fireFreq, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (soundEffect.status != "stopped") {
                    player.reload = false;
                    player.shoot();
                }
            }
        });
        machineGunFireRate.start();

        Timer deactivate = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.holdingMachineGun = false;
                player.bulletSound = Player.BulletSound.RIFLE;
                machineGunFireRate.stop();
                soundEffect.stop();
                ((Timer) e.getSource()).stop();
            }
        });
        deactivate.setInitialDelay(4900); // 5000
        deactivate.setRepeats(false);
        deactivate.start();
    }
}