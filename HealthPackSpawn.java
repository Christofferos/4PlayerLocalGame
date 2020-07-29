import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class HealthPackSpawn {
    int maxHPSpawnCap;
    Timer healthSpawnTimer;
    ArrayList<HealthPack> healthPacks;
    int width;
    int height;
    int xOffset;
    int yOffset;

    public HealthPackSpawn(ArrayList<HealthPack> healthPacks, int width, int height, int xOffset, int yOffset,
            CollisionDetection collisionDetection) {
        this.healthPacks = healthPacks;
        this.width = width;
        this.height = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;

        /* ## Timer: Healthspawn ## */
        maxHPSpawnCap = 2;
        healthSpawnTimer = new Timer(12500, new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spawnHealthPacks(collisionDetection);
            }
        });
        healthSpawnTimer.start();
    }

    /* ## HealthSpawn: Centered in Window ## */
    public void spawnHealthPacks(CollisionDetection collisionDetection) {
        Random rand = new Random();
        double doubleRand = 225 * Math.sqrt(rand.nextDouble()); // 55
        double theta = rand.nextDouble() * 2 * Math.PI;
        int xPlacement = (int) ((width - xOffset) / 2 + doubleRand * Math.cos(theta));
        int yPlacement = (int) ((height - yOffset) / 2 + doubleRand * Math.sin(theta));
        collisionDetection.healthSpawn(xPlacement, yPlacement);
    }

    /*
     * ## HealthCheck: Do not allow more than maxHPSpawnCap number of healthpacks
     * out on field ##
     */
    public void checkHealthPackCount() {
        if (healthPacks.size() == maxHPSpawnCap) {
            healthSpawnTimer.restart();
        }
    }
}