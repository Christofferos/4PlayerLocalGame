import javax.swing.*;
import java.awt.event.*;

public class PowerUpMine {
    SoundEffect soundEffect;
    Timer deactivate;
    Timer mine;

    public PowerUpMine(Player player, PlayerMovement playerMovement) {
        mine = new Timer(10, new ActionListener() {
            boolean once = true;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (player.id == 1)
                    player.dirKeyPress(playerMovement.movement1.xStep, playerMovement.movement1.yStep);
                if (player.id == 2)
                    player.dirKeyPress(playerMovement.movement2.xStep, playerMovement.movement2.yStep);
                if (player.id == 3)
                    player.dirKeyPress(playerMovement.movement3.xStep, playerMovement.movement3.yStep);
                if (player.id == 4)
                    player.dirKeyPress(playerMovement.movement4.xStep, playerMovement.movement4.yStep);

                // Mine dropped!
                if (player.reload && once && player.getMines().isEmpty()) {
                    try {
                        soundEffect = new SoundEffect("Sound/mine.wav");
                        soundEffect.play();
                    } catch (Exception ex) {
                        System.out.println("Soundtrack not found");
                        ex.printStackTrace();
                    }

                    makeMineInvisible(player);

                    player.getBullets().clear();
                    startDeactivateTimer(player);
                    once = false;

                    if (player.direction == Player.Direction.UP) {
                        player.mines.add(new Mine(player.xpos, player.ypos + player.height / 2));
                    } else if (player.direction == Player.Direction.DOWN) {
                        player.mines.add(new Mine(player.xpos, player.ypos - player.height / 2));
                    } else if (player.direction == Player.Direction.LEFT) {
                        player.mines.add(new Mine(player.xpos + player.width / 2, player.ypos));
                    } else if (player.direction == Player.Direction.RIGHT) {
                        player.mines.add(new Mine(player.xpos - player.width / 2, player.ypos));
                    }
                }
                if (once == false && player.getMines().size() == 0) {
                    ((Timer) e.getSource()).stop();
                    if (deactivate != null)
                        deactivate.stop();
                    soundEffect.stop();
                }
            }
        });
        mine.start();

    }

    public void startDeactivateTimer(Player player) {
        deactivate = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                soundEffect.stop();
                if (!player.mines.isEmpty())
                    player.mines.clear();
                mine.stop();
                ((Timer) e.getSource()).stop();
            }
        });
        deactivate.setInitialDelay(45000);
        deactivate.setRepeats(false);
        deactivate.start();
    }

    public void makeMineInvisible(Player player) {
        Timer invisible = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.mines.get(0).makeInvisible();
                ((Timer) e.getSource()).stop();
            }
        });
        invisible.setInitialDelay(1000);
        invisible.setRepeats(false);
        invisible.start();
    }
}