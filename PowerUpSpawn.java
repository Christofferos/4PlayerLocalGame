import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class PowerUpSpawn {
    int maxPowerUpSpawnCap;
    Timer powerUpTimer;
    ArrayList<PowerUp> powerUps;
    int width;
    int height;
    int xOffset;
    int yOffset;

    public PowerUpSpawn(ArrayList<PowerUp> powerUps, int width, int height, int xOffset, int yOffset,
            CollisionDetection collisionDetection) {
        this.powerUps = powerUps;
        this.width = width;
        this.height = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        maxPowerUpSpawnCap = 3;

        /* ## Timer: PowerUps ## */
        // TESTING Powerups 5000
        powerUpTimer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spawnPowerUp(collisionDetection);
            }
        });
        powerUpTimer.start();

    }

    /* ## PowerUpSpawn: Along the edges [Called from GameLoop] ## */
    public void spawnPowerUp(CollisionDetection collisionDetection) {
        boolean spawned = false;
        while (!spawned) {
            double max = 0.98;
            double min = 0.56;
            Random rand = new Random();
            double doubleRand = 135 * Math.sqrt(rand.nextDouble() * (max - min) + min);
            double theta = rand.nextDouble() * 2 * Math.PI;
            int xPlacement = (int) ((width - xOffset) / 2 + doubleRand * Math.cos(theta));
            int yPlacement = (int) ((height - yOffset) / 2 + doubleRand * Math.sin(theta));
            spawned = collisionDetection.powerUpSpawn(xPlacement - 6, yPlacement - 4);
        }
    }

    public void checkPowerUpCount() {
        if (powerUps.size() == maxPowerUpSpawnCap) {
            powerUpTimer.restart();
        }
    }
}