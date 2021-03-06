import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PowerUpRocket {
    SoundEffect soundEffect;
    Timer deactivate;
    Timer rocket;

    public PowerUpRocket(Player player, ArrayList<Boolean> stopSoundEffects) {
        rocket = new Timer(10, new ActionListener() {
            boolean once = true;

            @Override
            public void actionPerformed(ActionEvent e) {
                player.calculateDirection();

                // Rocket fired!
                if (player.reload && once) {
                    // Sound effect
                    try {
                        soundEffect = new SoundEffect("Sound/rocketOfficial.wav", stopSoundEffects);
                        soundEffect.play();
                    } catch (Exception ex) {
                        System.out.println("Soundtrack not found");
                        ex.printStackTrace();
                    }

                    player.getBullets().clear();
                    startDeactivateTimer(player);
                    once = false;
                    player.disablePlayerShooting = true;
                    player.disablePlayerMovement = true;
                    if (player.direction == Player.Direction.UP) {
                        player.rocket.add(new Rocket(player.xpos, player.ypos - player.height / 2));
                    } else if (player.direction == Player.Direction.DOWN) {
                        player.rocket.add(new Rocket(player.xpos, player.ypos + player.height / 2));
                    } else if (player.direction == Player.Direction.LEFT) {
                        player.rocket.add(new Rocket(player.xpos - player.width / 2, player.ypos));
                    } else if (player.direction == Player.Direction.RIGHT) {
                        player.rocket.add(new Rocket(player.xpos + player.width / 2, player.ypos));
                    }
                }
                if (once == false && player.getRocket().size() == 0) {
                    ((Timer) e.getSource()).stop();
                    if (deactivate != null)
                        deactivate.stop();
                    player.disablePlayerMovement = false;
                    player.disablePlayerShooting = false;
                    soundEffect.stop();
                }
            }
        });
        rocket.start();

    }

    public void startDeactivateTimer(Player player) {
        deactivate = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.disablePlayerMovement = false;
                player.disablePlayerShooting = false;
                soundEffect.stop();
                if (!player.rocket.isEmpty())
                    player.rocket.clear();
                rocket.stop();
                ((Timer) e.getSource()).stop();
            }
        });
        deactivate.setInitialDelay(7500); // 10000
        deactivate.setRepeats(false);
        deactivate.start();
    }
}