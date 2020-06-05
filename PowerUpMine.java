import javax.swing.*;
import java.awt.event.*;

public class PowerUpMine {
    SoundEffect soundEffect;
    Timer deactivate;
    Timer mine;

    public PowerUpMine(Player player) {
        mine = new Timer(10, new ActionListener() {
            boolean once = true;

            @Override
            public void actionPerformed(ActionEvent e) {
                player.calculateDirection();

                // Mine dropped!
                if (player.reload && once) {
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
                if (!player.mines.isEmpty()) {
                    player.mines.get(0).visible = true;
                    deactivate(player);
                }
                mine.stop();
                ((Timer) e.getSource()).stop();
            }
        });
        deactivate.setInitialDelay(45000);
        deactivate.setRepeats(false);
        deactivate.start();
    }

    public void deactivate(Player player) {
        Timer displayMineRemoval = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.mines.remove(0);
                ((Timer) e.getSource()).stop();
            }
        });
        displayMineRemoval.setInitialDelay(2000);
        displayMineRemoval.setRepeats(false);
        displayMineRemoval.start();
    }

    public void makeMineInvisible(Player player) {
        Timer invisible = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!player.mines.isEmpty())
                    player.mines.get(player.mines.size() - 1).makeInvisible();
                ((Timer) e.getSource()).stop();
            }
        });
        invisible.setInitialDelay(1000);
        invisible.setRepeats(false);
        invisible.start();
    }

}