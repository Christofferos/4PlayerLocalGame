import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import java.awt.event.*;

public class CollisionDetection implements Serializable {
    private static final long serialVersionUID = 1L;

    Player1 player1;
    Player2 player2;
    Player3 player3;
    Player4 player4;
    PlayerMovement playerMovement;

    ArrayList<Obstacle> obstacles;
    ArrayList<HealthPack> healthPacks;
    ArrayList<PowerUp> powerUps;
    ArrayList<FireBlock> fireBlocks;
    ArrayList<WaterBlock> waterBlocks;
    ArrayList<LightningBlock> lightningBlocks;

    ArrayList<Boolean> stopSoundEffects;

    /* Used to limit dmg to 2 when hit by explosion */
    boolean explosionDmgTaken1 = false;
    boolean explosionDmgTaken2 = false;
    boolean explosionDmgTaken3 = false;
    boolean explosionDmgTaken4 = false;

    public CollisionDetection(Player1 player1, Player2 player2, Player3 player3, Player4 player4,
            PlayerMovement playerMovement, ArrayList<Obstacle> obstacles, ArrayList<HealthPack> healthPacks,
            ArrayList<PowerUp> powerUps, ArrayList<FireBlock> fireBlocks, ArrayList<WaterBlock> waterBlocks,
            ArrayList<LightningBlock> lightningBlocks, ArrayList<Boolean> stopSoundEffects) {
        this.player1 = player1;
        this.player2 = player2;
        this.player3 = player3;
        this.player4 = player4;
        this.playerMovement = playerMovement;
        this.obstacles = obstacles;
        this.healthPacks = healthPacks;
        this.fireBlocks = fireBlocks;
        this.waterBlocks = waterBlocks;
        this.lightningBlocks = lightningBlocks;
        this.powerUps = powerUps;
        this.stopSoundEffects = stopSoundEffects;

        /* ## Timer (Polling): Check collision with explosions ## */
        int explosionFreq = 50;
        Timer explosionCheck = new Timer(explosionFreq, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                standingInExplosion(1);
                standingInExplosion(2);
                if (player3 != null)
                    standingInExplosion(3);
                if (player4 != null)
                    standingInExplosion(4);
            }
        });
        explosionCheck.start();
    }

    /* CoordinateXY: [] */
    public class CoordinateXY {
        int newX;
        int newY;

        public CoordinateXY(int x, int y) {
            newX = x;
            newY = y;
        }

        public int getX() {
            return newX;
        }

        public int getY() {
            return newY;
        }
    }

    public Player getPlayer(int playerID) {
        Player playerObj = player1; // Default
        if (playerID == 1)
            playerObj = (Player1) player1;
        else if (playerID == 2)
            playerObj = (Player2) player2;
        else if (playerID == 3)
            playerObj = (Player3) player3;
        else if (playerID == 4)
            playerObj = (Player4) player4;
        return playerObj;
    }

    // LIMITATION: Collision only works if (obstacle size) >= (player size).
    public boolean collision(int player, boolean mustBeMovable) {
        int x1 = 0;
        int x2 = 0;
        int y1 = 0;
        int y2 = 0;
        Player playerObj = getPlayer(player);

        if (playerObj.getDirection() == Player.Direction.UP) {
            x1 = playerObj.getXpos();
            y1 = playerObj.getYpos() - 1;
            x2 = playerObj.getXpos() + playerObj.getWidth();
            y2 = playerObj.getYpos() - 1;
        } else if (playerObj.getDirection() == Player.Direction.DOWN) {
            x1 = playerObj.getXpos();
            y1 = playerObj.getYpos() + 1 + playerObj.getHeight();
            x2 = playerObj.getXpos() + playerObj.getWidth();
            y2 = playerObj.getYpos() + 1 + playerObj.getHeight();
        } else if (playerObj.getDirection() == Player.Direction.LEFT) {
            x1 = playerObj.getXpos() - 1;
            y1 = playerObj.getYpos();
            x2 = playerObj.getXpos() - 1;
            y2 = playerObj.getYpos() + playerObj.getHeight();
        } else if (playerObj.getDirection() == Player.Direction.RIGHT) {
            x1 = playerObj.getXpos() + 1 + playerObj.getWidth();
            y1 = playerObj.getYpos();
            x2 = playerObj.getXpos() + 1 + playerObj.getWidth();
            y2 = playerObj.getYpos() + playerObj.getHeight();
        }

        for (int i = 0; i < obstacles.size(); i++) {
            Rectangle obstacleArea = obstacles.get(i).getBoundary();
            if (obstacleArea.contains(x1, y1) || obstacleArea.contains(x2, y2)) {
                /* Collision with movable obstacles. */
                if (mustBeMovable) {
                    if (obstacles.get(i).movable()) {
                        obstacles.remove(i);
                        return true;
                    }
                }
                /* Collision with solid obstacles. */
                else {
                    return true;
                }
            }
        }

        /* Collision between players. */
        if (!mustBeMovable) {
            Rectangle p1 = player1.getBoundary();
            Rectangle p2 = player2.getBoundary();
            Rectangle p3 = (player3 != null) ? player3.getBoundary() : null;
            Rectangle p4 = (player4 != null) ? player4.getBoundary() : null;

            switch (player) {
                case 1:
                    if ((p2.contains(x1, y1) || p2.contains(x2, y2)) && !player2.dead)
                        return true;
                    if (player3 != null) {
                        if ((p3.contains(x1, y1) || p3.contains(x2, y2)) && !player3.dead)
                            return true;
                    }
                    if (player4 != null) {
                        if ((p4.contains(x1, y1) || p4.contains(x2, y2)) && !player4.dead)
                            return true;
                    }
                    break;
                case 2:
                    if ((p1.contains(x1, y1) || p1.contains(x2, y2)) && !player1.dead)
                        return true;
                    if (player3 != null) {
                        if ((p3.contains(x1, y1) || p3.contains(x2, y2)) && !player3.dead)
                            return true;
                    }
                    if (player4 != null) {
                        if ((p4.contains(x1, y1) || p4.contains(x2, y2)) && !player4.dead)
                            return true;
                    }
                    break;
                case 3:
                    if ((p1.contains(x1, y1) || p1.contains(x2, y2)) && !player1.dead)
                        return true;
                    if ((p2.contains(x1, y1) || p2.contains(x2, y2)) && !player2.dead)
                        return true;
                    if (player4 != null) {
                        if ((p4.contains(x1, y1) || p4.contains(x2, y2)) && !player4.dead)
                            return true;
                    }
                    break;
                case 4:
                    if ((p1.contains(x1, y1) || p1.contains(x2, y2)) && !player1.dead)
                        return true;
                    if ((p2.contains(x1, y1) || p2.contains(x2, y2)) && !player2.dead)
                        return true;
                    if (player3 != null) {
                        if ((p3.contains(x1, y1) || p3.contains(x2, y2)) && !player3.dead)
                            return true;
                    }
                    break;
            }
        }
        return false;
    }

    /* SpaceToDropObstacle: [Check if player can drop movable obstacle] */
    public boolean spaceToDropObstacle(int player) {
        Rectangle space = new Rectangle(0, 0, 0, 0);

        Player playerObj = getPlayer(player);
        Player.Direction dir = playerObj.direction;
        int x = playerObj.getXpos();
        int y = playerObj.getYpos();

        Rectangle object;
        int obstacleSize = playerObj.inventoryObstacleSize;
        int offset = (obstacleSize % 8) / 2;
        int fixPos = 0;

        /* Adjustments were needed at changed size. */
        if (obstacleSize == 12)
            fixPos = 1;
        if (obstacleSize == 14)
            fixPos = 2;

        if (dir == Player.Direction.UP) {
            space = new Rectangle(x - offset, y - obstacleSize, obstacleSize, obstacleSize);
        } else if (dir == Player.Direction.DOWN) {
            space = new Rectangle(x - offset, y + obstacleSize - offset - fixPos, obstacleSize, obstacleSize);
        } else if (dir == Player.Direction.LEFT) {
            space = new Rectangle(x - obstacleSize, y - offset, obstacleSize, obstacleSize);
        } else if (dir == Player.Direction.RIGHT) {
            space = new Rectangle(x + obstacleSize - offset - fixPos, y - offset, obstacleSize, obstacleSize);
        }

        for (int i = 0; i < obstacles.size(); i++) {
            object = obstacles.get(i).getBoundary();
            if (object.intersects(space)) {
                CoordinateXY xy = placementAssistance(dir, object, space, x, y, offset, obstacleSize, fixPos);
                if (xy == null)
                    return false;
                else {
                    space.setBounds(xy.getX(), xy.getY(), obstacleSize, obstacleSize);
                }
            }
        }

        // Collision between: players and obstacles one player wants to drop.
        Rectangle p1 = player1.getBoundary();
        Rectangle p2 = player2.getBoundary();
        Rectangle p3 = (player3 != null) ? player3.getBoundary() : null;
        Rectangle p4 = (player4 != null) ? player4.getBoundary() : null;

        if (p1.intersects(space) && !player1.dead)
            return false;
        if (p2.intersects(space) && !player2.dead)
            return false;
        if (player3 != null)
            if (p3.intersects(space) && !player3.dead)
                return false;
        if (player4 != null)
            if (p4.intersects(space) && !player4.dead)
                return false;

        obstacles.add(new Obstacle(space.x, space.y, true, playerObj.inventoryObstacleSize));
        return true;
    }

    /* PlacementAssistance: [Used if player stands besides a wall and tries to place an obstacle - push that obstacle onto a free area] */
    public CoordinateXY placementAssistance(Player.Direction dir, Rectangle object, Rectangle space, int x, int y,
            int offset, int obstacleSize, int fixPos) {
        CoordinateXY xy = null;

        switch (dir) {
            case UP:
                space.setBounds(x - offset - obstacleSize % 8, y - obstacleSize, obstacleSize, obstacleSize);
                if (!object.intersects(space))
                    xy = new CoordinateXY(x - offset - obstacleSize % 8, y - obstacleSize);
                space.setBounds(x - offset + obstacleSize % 8, y - obstacleSize, obstacleSize, obstacleSize);
                if (!object.intersects(space))
                    xy = new CoordinateXY(x - offset + obstacleSize % 8, y - obstacleSize);
                break;
            case DOWN:
                space.setBounds(x - offset - obstacleSize % 8, y + obstacleSize - offset - fixPos, obstacleSize,
                        obstacleSize);
                if (!object.intersects(space))
                    xy = new CoordinateXY(x - offset - obstacleSize % 8, y + obstacleSize - offset - fixPos);
                space.setBounds(x - offset + obstacleSize % 8, y + obstacleSize - offset - fixPos, obstacleSize,
                        obstacleSize);
                if (!object.intersects(space))
                    xy = new CoordinateXY(x - offset + obstacleSize % 8, y + obstacleSize - offset - fixPos);
                break;
            case LEFT:
                space.setBounds(x - obstacleSize, y - offset - obstacleSize % 8, obstacleSize, obstacleSize);
                if (!object.intersects(space))
                    xy = new CoordinateXY(x - obstacleSize, y - offset - obstacleSize % 8);
                space.setBounds(x - obstacleSize, y - offset + obstacleSize % 8, obstacleSize, obstacleSize);
                if (!object.intersects(space))
                    xy = new CoordinateXY(x - obstacleSize, y - offset + obstacleSize % 8);
                break;
            case RIGHT:
                space.setBounds(x + obstacleSize - offset - fixPos, y - offset - obstacleSize % 8, obstacleSize,
                        obstacleSize);
                if (!object.intersects(space))
                    xy = new CoordinateXY(x + obstacleSize - offset - fixPos, y - offset - obstacleSize % 8);
                space.setBounds(x + obstacleSize - offset - fixPos, y - offset + obstacleSize % 8, obstacleSize,
                        obstacleSize);
                if (!object.intersects(space))
                    xy = new CoordinateXY(x + obstacleSize - offset - fixPos, y - offset + obstacleSize % 8);
                break;
        }

        if (xy != null) {
            Rectangle objectTemp;
            Rectangle spaceTemp = new Rectangle(xy.getX(), xy.getY(), obstacleSize, obstacleSize);
            for (int j = 0; j < obstacles.size(); j++) {
                objectTemp = obstacles.get(j).getBoundary();
                if (objectTemp.intersects(spaceTemp)) {
                    xy = null;
                    break;
                }
            }
        }
        return xy;
    }

    /* StandingInFire: Decrement player health. */
    public void standingInFire(int player) {
        Rectangle object;
        for (int i = 0; i < fireBlocks.size(); i++) {
            object = fireBlocks.get(i).getBoundary();
            if (player == 1) {
                if (object.intersects(player1.getBoundary())) {
                    player1.decrementLives();
                    break;
                }
            } else if (player == 2) {
                if (object.intersects(player2.getBoundary())) {
                    player2.decrementLives();
                    break;
                }
            } else if (player == 3) {
                if (object.intersects(player3.getBoundary())) {
                    player3.decrementLives();
                    break;
                }
            } else if (player == 4) {
                if (object.intersects(player4.getBoundary())) {
                    player4.decrementLives();
                    break;
                }
            }
        }
    }

    /* StandingInLightning: Decrement player health. */
    public void standingInLightning(int player) {
        Rectangle object;
        for (int i = 0; i < lightningBlocks.size(); i++) {
            object = lightningBlocks.get(i).getBoundary();
            if (player == 1) {
                if (object.intersects(player1.getBoundary())) {
                    player1.decrementLives();
                    break;
                }
            } else if (player == 2) {
                if (object.intersects(player2.getBoundary())) {
                    player2.decrementLives();
                    break;
                }
            } else if (player == 3) {
                if (object.intersects(player3.getBoundary())) {
                    player3.decrementLives();
                    break;
                }
            } else if (player == 4) {
                if (object.intersects(player4.getBoundary())) {
                    player4.decrementLives();
                    break;
                }
            }
        }
    }

    /* StandingInWater: Decrement player health. */
    public boolean standingInWater(int player) {
        Player playerObj = getPlayer(player);
        Rectangle object;
        for (int i = 0; i < waterBlocks.size(); i++) {
            object = waterBlocks.get(i).getBoundary();
            if (object.intersects(playerObj.getBoundary())) {
                return true;
            }
        }
        return false;
    }

    /* StandingInExplosion: Decrement player health. */
    public void standingInExplosion(int player) {
        Rectangle object;
        Player playerObj = getPlayer(player);

        for (int i = 0; i < playerObj.getExplosion().size(); i++) {
            object = playerObj.getExplosion().get(i).getBoundary();
            if (player1.getBoundary() != null)
                if (object.intersects(player1.getBoundary()) && !explosionDmgTaken1) {
                    explosionDmgTaken1 = true;
                    player1.decrementLives();
                    player1.decrementLives();
                    break;
                }
            if (player2.getBoundary() != null)
                if (object.intersects(player2.getBoundary()) && !explosionDmgTaken2) {
                    explosionDmgTaken2 = true;
                    player2.decrementLives();
                    player2.decrementLives();
                    break;
                }
            if (player3 != null) {
                if (player3.getBoundary() != null)
                    if (object.intersects(player3.getBoundary()) && !explosionDmgTaken3) {
                        explosionDmgTaken3 = true;
                        player3.decrementLives();
                        player3.decrementLives();
                        break;
                    }
            }
            if (player4 != null) {
                if (player4.getBoundary() != null)
                    if (object.intersects(player4.getBoundary()) && !explosionDmgTaken4) {
                        explosionDmgTaken4 = true;
                        player4.decrementLives();
                        player4.decrementLives();
                        break;
                    }
            }
        }
    }

    /* HealthSpawn: [] */
    public boolean healthSpawn(int x, int y) {
        Rectangle healthPack = new Rectangle(x, y, 8, 8);
        Rectangle object;
        for (int i = 0; i < obstacles.size(); i++) {
            object = obstacles.get(i).getBoundary();
            if (object.intersects(healthPack)) {
                return false;
            }
        }
        for (int i = 0; i < healthPacks.size(); i++) {
            object = healthPacks.get(i).getBoundary();
            if (object.intersects(healthPack)) {
                return false;
            }
        }
        if (healthPack.intersects(player1.getBoundary()))
            return false;
        if (healthPack.intersects(player2.getBoundary()))
            return false;
        healthPacks.add(new HealthPack(x, y));
        return true;
    }

    /* HealthPickUp: [] */
    public void healthPickUp(int player) {
        Player playerObj = getPlayer(player);
        Rectangle p;
        Rectangle h;
        p = playerObj.getBoundary();
        for (int i = 0; i < healthPacks.size(); i++) {
            h = healthPacks.get(i).getBoundary();
            if (p.intersects(h)) {
                if (playerObj.getLives() != playerObj.maxLives)
                    playerObj.incrementLives();
                healthPacks.remove(i);
            }
        }
    }

    /* PowerUpSpawn: [] */
    public boolean powerUpSpawn(int x, int y) {
        Rectangle powerUp = new Rectangle(x, y, 16, 16);
        Rectangle object;
        for (int i = 0; i < obstacles.size(); i++) {
            object = obstacles.get(i).getBoundary();
            if (object.intersects(powerUp)) {
                return false;
            }
        }
        for (int i = 0; i < healthPacks.size(); i++) {
            object = healthPacks.get(i).getBoundary();
            if (object.intersects(powerUp)) {
                return false;
            }
        }
        if (powerUp.intersects(player1.getBoundary()))
            return false;
        if (powerUp.intersects(player2.getBoundary()))
            return false;
        Random rand = new Random();
        double doubleRand = rand.nextDouble();

        // TESTING: powerup percentage ratios
        if (doubleRand < 0.15)
            powerUps.add(new PowerUp(x, y, 4));
        if (doubleRand >= 0.15 && doubleRand < 0.35)
            powerUps.add(new PowerUp(x, y, 4));
        if (doubleRand >= 0.35 && doubleRand < 0.60)
            powerUps.add(new PowerUp(x, y, 4));
        if (doubleRand >= 0.60 && doubleRand < 0.70)
            powerUps.add(new PowerUp(x, y, 4));
        if (doubleRand >= 0.70 && doubleRand < 0.80)
            powerUps.add(new PowerUp(x, y, 4));
        if (doubleRand >= 0.80 && doubleRand < 0.90)
            powerUps.add(new PowerUp(x, y, 4));
        if (doubleRand >= 0.90 && doubleRand <= 1)
            powerUps.add(new PowerUp(x, y, 4));
        return true;
    }

    /* PowerUpPickUp: [] */
    public void powerUpPickUp(int player) {
        Player playerObj = getPlayer(player);
        Rectangle p;
        Rectangle h;

        p = playerObj.getBoundary();
        for (int i = 0; i < powerUps.size(); i++) {
            h = powerUps.get(i).getBoundary();
            if (p.intersects(h)) {
                if (powerUps.get(i).getType() == 1) {
                    if (playerObj.maxLives != 5)
                        playerObj.maxLives += 1;
                } else if (powerUps.get(i).getType() == 2) {
                    if (playerObj.reloadFreq >= 100)
                        playerObj.reloadFreq -= 25;
                } else if (powerUps.get(i).getType() == 3) {
                    playerObj.inventoryMaxCap += 1;
                    if (playerObj.inventoryFrequency > 100) {
                        playerObj.inventoryFrequency -= 100;
                        if (playerObj.inventoryObstacleSize != 12)
                            playerObj.inventoryObstacleSize += 2;
                    }
                    if (playerObj.inventory != playerObj.inventoryMaxCap - 1) {
                        playerObj.inventory++;
                    }
                } else if (powerUps.get(i).getType() == 4) {
                    new PowerUpMachineGun(playerObj, stopSoundEffects);
                } else if (powerUps.get(i).getType() == 5) {
                    new PowerUpSniper(playerObj);
                } else if (powerUps.get(i).getType() == 6) {
                    new PowerUpRocket(playerObj, playerMovement, stopSoundEffects);
                } else if (powerUps.get(i).getType() == 7) {
                    new PowerUpMine(playerObj, playerMovement);
                }
                powerUps.remove(i);
            }
        }
    }

    /* Hit: [Check if player hit by bullets] */
    public void hit(int player) {
        Rectangle b;
        Rectangle p1 = player1.getBoundary();
        Rectangle p2 = player2.getBoundary();
        Rectangle p3 = (player3 != null) ? player3.getBoundary() : null;
        Rectangle p4 = (player4 != null) ? player4.getBoundary() : null;

        switch (player) {
            case 1:
                if (!player1.getBullets().isEmpty()) {
                    for (int i = 0; i < player1.getBullets().size(); i++) {
                        b = player1.getBullets().get(i).getBoundary();
                        if (p2.intersects(b) && !player2.dead) {
                            player2.decrementLives();
                            player1.getBullets().remove(i);
                        } else if (p3.intersects(b)) {
                            if (!player3.dead) {
                                player3.decrementLives();
                                player1.getBullets().remove(i);
                            }
                        } else if (p4.intersects(b)) {
                            if (!player4.dead) {
                                player4.decrementLives();
                                player1.getBullets().remove(i);
                            }
                        }
                    }
                }
                break;
            case 2:
                if (!player2.getBullets().isEmpty()) {
                    player2.getBullets().toString();
                    for (int i = 0; i < player2.getBullets().size(); i++) {
                        b = player2.getBullets().get(i).getBoundary();
                        if (p1.intersects(b) && !player1.dead) {
                            player1.decrementLives();
                            player2.getBullets().remove(i);
                        } else if (p3.intersects(b)) {
                            if (!player3.dead) {
                                player3.decrementLives();
                                player2.getBullets().remove(i);
                            }
                        } else if (p4.intersects(b)) {
                            if (!player4.dead) {
                                player4.decrementLives();
                                player2.getBullets().remove(i);
                            }
                        }
                    }
                }
                break;
            case 3:
                if (!player3.getBullets().isEmpty()) {
                    player3.getBullets().toString();
                    for (int i = 0; i < player3.getBullets().size(); i++) {
                        b = player3.getBullets().get(i).getBoundary();
                        if (p1.intersects(b) && !player1.dead) {
                            player1.decrementLives();
                            player3.getBullets().remove(i);
                        } else if (p2.intersects(b) && !player2.dead) {
                            player2.decrementLives();
                            player3.getBullets().remove(i);
                        } else if (p4.intersects(b)) {
                            if (!player4.dead) {
                                player4.decrementLives();
                                player3.getBullets().remove(i);
                            }
                        }
                    }
                }
                break;
            case 4:
                if (!player4.getBullets().isEmpty()) {
                    player4.getBullets().toString();
                    for (int i = 0; i < player4.getBullets().size(); i++) {
                        b = player4.getBullets().get(i).getBoundary();
                        if (p1.intersects(b) && !player1.dead) {
                            player1.decrementLives();
                            player4.getBullets().remove(i);
                        } else if (p2.intersects(b) && !player2.dead) {
                            player2.decrementLives();
                            player4.getBullets().remove(i);
                        } else if (p3.intersects(b)) {
                            if (!player3.dead) {
                                player3.decrementLives();
                                player4.getBullets().remove(i);
                            }
                        }
                    }
                }
                break;
        }

        Player playerObj = getPlayer(player);
        ArrayList<Bullet> bullets = new ArrayList<Bullet>();
        bullets.addAll(playerObj.getBullets());
        Rectangle o;

        for (Bullet bullet : bullets) {
            b = bullet.getBoundary();
            for (Obstacle obstacle : obstacles) {
                o = obstacle.getBoundary();
                if (o.intersects(b)) {
                    playerObj.getBullets().remove(bullet);
                }
            }
        }
    }

    /* RocketHit: [Check if player hit by rocket] */
    public void rocketHit(int player) {
        Rectangle b;
        Rectangle p1 = player1.getBoundary();
        Rectangle p2 = player2.getBoundary();
        Rectangle p3 = (player3 != null) ? player3.getBoundary() : null;
        Rectangle p4 = (player4 != null) ? player4.getBoundary() : null;

        switch (player) {
            case 1:
                if (!player1.getRocket().isEmpty()) {
                    b = player1.getRocket().get(0).getBoundary();
                    if (p2.intersects(b) && !player2.dead) {
                        explosion(b.x, b.y, player1);
                        player1.getRocket().clear();
                    } else if (p3.intersects(b)) {
                        if (!player3.dead) {
                            explosion(b.x, b.y, player1);
                            player1.getRocket().clear();
                        }
                    } else if (p4.intersects(b)) {
                        if (!player4.dead) {
                            explosion(b.x, b.y, player1);
                            player1.getRocket().clear();
                        }
                    }

                    // Rocket can collide with other players' bullets.
                    List<Bullet> bullets = player2.getBullets();
                    if (player3 != null)
                        bullets.addAll(player3.getBullets());
                    if (player4 != null)
                        bullets.addAll(player4.getBullets());
                    for (Bullet bullet : bullets) {
                        if (bullet.getBoundary().intersects(b)) {
                            if (!player1.getRocket().isEmpty()) {
                                explosion(b.x, b.y, player1);
                                player1.getRocket().clear();
                            }
                        }
                    }
                }
                break;
            case 2:
                if (!player2.getRocket().isEmpty()) {
                    b = player2.getRocket().get(0).getBoundary();
                    if (p1.intersects(b) && !player1.dead) {
                        explosion(b.x, b.y, player2);
                        player2.getRocket().clear();
                    } else if (p3.intersects(b)) {
                        if (!player3.dead) {
                            explosion(b.x, b.y, player2);
                            player2.getRocket().clear();
                        }
                    } else if (p4.intersects(b)) {
                        if (!player4.dead) {
                            explosion(b.x, b.y, player2);
                            player2.getRocket().clear();
                        }
                    }

                    List<Bullet> bullets = player1.getBullets();
                    if (player3 != null)
                        bullets.addAll(player3.getBullets());
                    if (player4 != null)
                        bullets.addAll(player4.getBullets());
                    for (Bullet bullet : bullets) {
                        if (bullet.getBoundary().intersects(b)) {
                            if (!player2.getRocket().isEmpty()) {
                                explosion(b.x, b.y, player2);
                                player2.getRocket().clear();
                            }
                        }
                    }
                }
                break;
            case 3:
                if (!player3.getRocket().isEmpty()) {
                    b = player3.getRocket().get(0).getBoundary();
                    if (p1.intersects(b) && !player1.dead) {
                        explosion(b.x, b.y, player3);
                        player3.getRocket().clear();
                    } else if (p2.intersects(b) && !player2.dead) {
                        explosion(b.x, b.y, player3);
                        player3.getRocket().clear();
                    } else if (p4.intersects(b)) {
                        if (!player4.dead) {
                            explosion(b.x, b.y, player3);
                            player3.getRocket().clear();
                        }
                    }

                    List<Bullet> bullets = player1.getBullets();
                    bullets.addAll(player2.getBullets());
                    if (player4 != null)
                        bullets.addAll(player4.getBullets());
                    for (Bullet bullet : bullets) {
                        if (bullet.getBoundary().intersects(b)) {
                            if (!player3.getRocket().isEmpty()) {
                                explosion(b.x, b.y, player3);
                                player3.getRocket().clear();
                            }
                        }
                    }
                }
                break;
            case 4:
                if (!player4.getRocket().isEmpty()) {
                    b = player4.getRocket().get(0).getBoundary();
                    if (p1.intersects(b) && !player1.dead) {
                        explosion(b.x, b.y, player4);
                        player4.getRocket().clear();
                    } else if (p2.intersects(b) && !player2.dead) {
                        explosion(b.x, b.y, player4);
                        player4.getRocket().clear();
                    } else if (p3.intersects(b)) {
                        if (!player3.dead) {
                            explosion(b.x, b.y, player4);
                            player4.getRocket().clear();
                        }
                    }

                    List<Bullet> bullets = player1.getBullets();
                    bullets.addAll(player2.getBullets());
                    if (player3 != null)
                        bullets.addAll(player3.getBullets());
                    for (Bullet bullet : bullets) {
                        if (bullet.getBoundary().intersects(b)) {
                            if (!player4.getRocket().isEmpty()) {
                                explosion(b.x, b.y, player4);
                                player4.getRocket().clear();
                            }
                        }
                    }
                }
                break;
        }

        // Collision with obstacles
        Player playerObj = getPlayer(player);
        ArrayList<Rocket> rocket = playerObj.getRocket();
        Rectangle o;

        for (int i = 0; i < rocket.size(); i++) {
            Rocket rocketCheck = rocket.get(i);
            b = rocketCheck.getBoundary();
            for (Obstacle obstacle : obstacles) {
                o = obstacle.getBoundary();
                if (o.intersects(b)) {
                    playerObj.getRocket().remove(rocketCheck);
                    explosion(rocketCheck.xpos, rocketCheck.ypos, playerObj);
                    break;
                }
            }
        }
    }

    /* MineHit: [Check if player walks on invisible mine] */
    public void mineHit(int player) {
        Rectangle b;
        Rectangle p1 = player1.getBoundary();
        Rectangle p2 = player2.getBoundary();
        Rectangle p3 = (player3 != null) ? player3.getBoundary() : null;
        Rectangle p4 = (player4 != null) ? player4.getBoundary() : null;

        // Collision with player objects
        switch (player) {
            case 1:
                if (!player1.getMines().isEmpty()) {
                    for (int i = 0; i < player1.getMines().size(); i++) {
                        b = player1.getMines().get(i).getBoundary();
                        if (p2.intersects(b) && !player2.dead) {
                            explosion(b.x, b.y + 2, player1);
                            player1.getMines().remove(i);
                        } else if (p3.intersects(b)) {
                            if (!player3.dead) {
                                explosion(b.x, b.y + 2, player1);
                                player1.getMines().remove(i);
                            }
                        } else if (p4.intersects(b)) {
                            if (!player4.dead) {
                                explosion(b.x, b.y + 2, player1);
                                player1.getMines().remove(i);
                            }
                        }
                    }
                }
                break;
            case 2:
                if (!player2.getMines().isEmpty()) {
                    for (int i = 0; i < player2.getMines().size(); i++) {
                        b = player2.getMines().get(i).getBoundary();
                        if (p1.intersects(b) && !player1.dead) {
                            explosion(b.x + 2, b.y + 2, player2);
                            player2.getMines().remove(i);
                        } else if (p3.intersects(b)) {
                            if (!player3.dead) {
                                explosion(b.x + 2, b.y + 2, player2);
                                player2.getMines().remove(i);
                            }
                        } else if (p4.intersects(b)) {
                            if (!player4.dead) {
                                explosion(b.x + 2, b.y + 2, player2);
                                player2.getMines().remove(i);
                            }
                        }
                    }
                }
                break;
            case 3:
                if (!player3.getMines().isEmpty()) {
                    for (int i = 0; i < player3.getMines().size(); i++) {
                        b = player3.getMines().get(i).getBoundary();
                        if (p1.intersects(b) && !player1.dead) {
                            explosion(b.x + 2, b.y + 2, player3);
                            player3.getMines().remove(i);
                        } else if (p2.intersects(b) && !player2.dead) {
                            explosion(b.x + 2, b.y + 2, player3);
                            player3.getMines().remove(i);
                        } else if (p4.intersects(b)) {
                            if (!player4.dead) {
                                explosion(b.x + 2, b.y + 2, player3);
                                player3.getMines().remove(i);
                            }
                        }
                    }
                }
                break;
            case 4:
                if (!player4.getMines().isEmpty()) {
                    for (int i = 0; i < player4.getMines().size(); i++) {
                        b = player4.getMines().get(0).getBoundary();
                        if (p1.intersects(b) && !player1.dead) {
                            explosion(b.x + 2, b.y + 2, player4);
                            player4.getMines().remove(i);
                        } else if (p2.intersects(b) && !player2.dead) {
                            explosion(b.x + 2, b.y + 2, player4);
                            player4.getMines().remove(i);
                        } else if (p3.intersects(b)) {
                            if (!player3.dead) {
                                explosion(b.x + 2, b.y + 2, player4);
                                player4.getMines().remove(i);
                            }
                        }
                    }
                }
                break;
        }
    }

    /* Explosion: [Result of rocket or mine] */
    public void explosion(int x, int y, Player player) {
        try {
            SoundEffect soundEffect = new SoundEffect("Sound/explosion.wav");
            soundEffect.play();
        } catch (Exception ex) {
            System.out.println("Soundtrack not found");
            ex.printStackTrace();
        }

        player.rocketExplosion.add(new RocketExplosion(x, y));

        Timer explosionTime = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.rocketExplosion.clear();
                explosionDmgTaken1 = false;
                explosionDmgTaken2 = false;
                explosionDmgTaken3 = false;
                explosionDmgTaken4 = false;
                ((Timer) e.getSource()).stop();
            }
        });
        explosionTime.setInitialDelay(350);
        explosionTime.start();
    }
}