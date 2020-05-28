import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.Action;
import javax.swing.Timer;

public class Environment {
    boolean water;
    boolean fire;
    boolean lightning;
    private Timer waterCheck1;
    private Timer waterCheck2;
    private Timer waterCheck3;
    private Timer waterCheck4;

    public Environment(Player1 player1, Player2 player2, Player3 player3, Player4 player4, WaterFlood waterFlood,
            FireRing fireRing, LightningStorm lightningStorm, CollisionDetection collisionDetection,
            int environmentLastMatch) {

        if (environmentLastMatch == -1) {
            Random randSpawnType = new Random();
            double randSpawnTypeD = randSpawnType.nextDouble();
            // TESTING ENVIRONMENT
            initiateEnvironment(randSpawnTypeD, waterFlood, fireRing, lightningStorm, collisionDetection, player1,
                    player2, player3, player4);
        } else {
            newEnvironment(environmentLastMatch, waterFlood, fireRing, lightningStorm, collisionDetection, player1,
                    player2, player3, player4);
        }
    }

    public boolean isWaterEnvironment() {
        if (water)
            return water;
        return water;
    }

    public boolean isFireEnvironment() {
        if (fire)
            return fire;
        return fire;
    }

    public void restartWaterClock1() {
        waterCheck1.restart();
    }

    public void restartWaterClock2() {
        waterCheck2.restart();
    }

    public void restartWaterClock3() {
        waterCheck3.restart();
    }

    public void restartWaterClock4() {
        waterCheck4.restart();
    }

    public void newEnvironment(int environmentLastMatch, WaterFlood waterFlood, FireRing fireRing,
            LightningStorm lightningStorm, CollisionDetection collisionDetection, Player1 player1, Player2 player2,
            Player3 player3, Player4 player4) {
        double newEnvironment = -1;
        Random rand = new Random();
        double randSpawn = rand.nextDouble();
        if (environmentLastMatch == 1) { // environmentLastMatch = 1 : Fire
            if (randSpawn < 0.50)
                newEnvironment = 0;
            if (randSpawn >= 0.50)
                newEnvironment = 0.5;
        } else if (environmentLastMatch == 2) { // environmentLastMatch = 2 : Water
            if (randSpawn < 0.50)
                newEnvironment = 1;
            if (randSpawn >= 0.50)
                newEnvironment = 0;
        } else if (environmentLastMatch == 3) { // environmentLastMatch = 3 : Lightning
            if (randSpawn < 0.50)
                newEnvironment = 0.5;
            if (randSpawn >= 0.50)
                newEnvironment = 1;
        }
        initiateEnvironment(newEnvironment, waterFlood, fireRing, lightningStorm, collisionDetection, player1, player2,
                player3, player4);
    }

    public void initiateEnvironment(double randSpawnTypeD, WaterFlood waterFlood, FireRing fireRing,
            LightningStorm lightningStorm, CollisionDetection collisionDetection, Player1 player1, Player2 player2,
            Player3 player3, Player4 player4) {
        if (randSpawnTypeD <= 0.33) {
            /* ## Timer: LightningSpawn ## */
            lightning = true;
            int lightningFrequency = 1500;
            Timer lightningTimer = new Timer(lightningFrequency, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    lightningStorm.startStorm();
                }
            });
            lightningTimer.start();

            /* ## Timer: Lightning Damage To Players ## */
            int lightningCheckFreq = 350; // 500
            Timer lightningCheck = new Timer(lightningCheckFreq, new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    if (!player1.dead)
                        collisionDetection.standingInLightning(1);
                    if (!player2.dead)
                        collisionDetection.standingInLightning(2);
                    if (player3 != null)
                        if (!player3.dead)
                            collisionDetection.standingInLightning(3);
                    if (player4 != null)
                        if (!player4.dead)
                            collisionDetection.standingInLightning(4);
                }
            });
            lightningCheck.start();
        } else if (randSpawnTypeD >= 0.33 && randSpawnTypeD <= 0.70) {
            /* ## Timer: Waterspawn ## */
            water = true;
            int waterIncrementTime = 3000;
            Timer waterTimer = new Timer(waterIncrementTime, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    waterFlood.increaseWater();
                }
            });
            waterTimer.start();

            /* ## Timers(2): Water Damage To Seperate Player ## */
            int waterCheckFreq = 750;
            waterCheck1 = new Timer(waterCheckFreq, new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    if (!player1.dead)
                        player1.decrementLives();
                }
            });
            waterCheck1.start();
            waterCheck2 = new Timer(waterCheckFreq, new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    if (!player2.dead)
                        player2.decrementLives();
                }
            });
            waterCheck2.start();
            if (player3 != null) {
                waterCheck3 = new Timer(waterCheckFreq, new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (!player3.dead)
                            player3.decrementLives();
                    }
                });
                waterCheck3.start();
            }
            if (player4 != null) {
                waterCheck4 = new Timer(waterCheckFreq, new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (!player4.dead)
                            player4.decrementLives();
                    }
                });
                waterCheck4.start();
            }
        } else if (randSpawnTypeD > 0.70) {
            fire = true;
            /* ## Timer: Firespawn ## */
            // TESTING FIRE
            int flameIncrementTime = 3000;
            Timer fireTimer = new Timer(flameIncrementTime, new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    fireRing.increaseFlames();
                }
            });
            fireTimer.start();

            /* ## Timer: Fire Damage To Players ## */
            int flameCheckFreq = 750;
            Timer flameCheck = new Timer(flameCheckFreq, new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    if (!player1.dead)
                        collisionDetection.standingInFire(1);
                    if (!player2.dead)
                        collisionDetection.standingInFire(2);
                    if (player3 != null)
                        if (!player3.dead)
                            collisionDetection.standingInFire(3);
                    if (player4 != null)
                        if (!player4.dead)
                            collisionDetection.standingInFire(4);
                }
            });
            flameCheck.start();
        }
    }

}
