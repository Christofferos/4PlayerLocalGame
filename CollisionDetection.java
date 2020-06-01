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

        /* ## Timer: Explosion Damage To Players ## */
        int explosionFreq = 50;
        Timer explosionCheck = new Timer(explosionFreq, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (!player1.dead)
                    standingInExplosion(1);
                if (!player2.dead)
                    standingInExplosion(2);
                if (player3 != null)
                    if (!player3.dead)
                        standingInExplosion(3);
                if (player4 != null)
                    if (!player4.dead)
                        standingInExplosion(4);
            }
        });
        explosionCheck.start();
    }

    // Collision works only if object is of equal or greater size than the player.
    public boolean collision(int player, boolean mustBeMovable) {
        int x1 = 0;
        int x2 = 0;
        int y1 = 0;
        int y2 = 0;

        Player playerObj = player1; // Default to player1.
        if (player == 1)
            playerObj = (Player1) player1;
        else if (player == 2)
            playerObj = (Player2) player2;
        else if (player == 3)
            playerObj = (Player3) player3;
        else if (player == 4)
            playerObj = (Player4) player4;

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
                if (mustBeMovable) {
                    if (obstacles.get(i).movable()) {
                        obstacles.remove(i);
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }

        // Do not allow players to walk through each other.
        if (!mustBeMovable) {
            Rectangle p1 = null;
            Rectangle p2 = null;
            Rectangle p3 = null;
            Rectangle p4 = null;

            if (player == 1) {
                p2 = player2.getBoundary();
                if ((p2.contains(x1, y1) || p2.contains(x2, y2)) && !player2.dead)
                    return true;
                if (player3 != null) {
                    p3 = player3.getBoundary();
                    if ((p3.contains(x1, y1) || p3.contains(x2, y2)) && !player3.dead)
                        return true;
                }
                if (player4 != null) {
                    p4 = player4.getBoundary();
                    if ((p4.contains(x1, y1) || p4.contains(x2, y2)) && !player4.dead)
                        return true;
                }
            } else if (player == 2) {
                p1 = player1.getBoundary();
                if ((p1.contains(x1, y1) || p1.contains(x2, y2)) && !player1.dead)
                    return true;
                if (player3 != null) {
                    p3 = player3.getBoundary();
                    if ((p3.contains(x1, y1) || p3.contains(x2, y2)) && !player3.dead)
                        return true;
                }
                if (player4 != null) {
                    p4 = player4.getBoundary();
                    if ((p4.contains(x1, y1) || p4.contains(x2, y2)) && !player4.dead)
                        return true;
                }
            } else if (player == 3) {
                p1 = player1.getBoundary();
                p2 = player2.getBoundary();
                if ((p1.contains(x1, y1) || p1.contains(x2, y2)) && !player1.dead)
                    return true;
                if ((p2.contains(x1, y1) || p2.contains(x2, y2)) && !player2.dead)
                    return true;
                if (player4 != null) {
                    p4 = player4.getBoundary();
                    if ((p4.contains(x1, y1) || p4.contains(x2, y2)) && !player4.dead)
                        return true;
                }
            } else if (player == 4) {
                p1 = player1.getBoundary();
                p2 = player2.getBoundary();
                if ((p1.contains(x1, y1) || p1.contains(x2, y2)) && !player1.dead)
                    return true;
                if ((p2.contains(x1, y1) || p2.contains(x2, y2)) && !player2.dead)
                    return true;
                if (player3 != null) {
                    p3 = player3.getBoundary();
                    if ((p3.contains(x1, y1) || p3.contains(x2, y2)) && !player3.dead)
                        return true;
                }
            }
        }
        return false;
    }

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

    public boolean standingInWater(int player) {
        Player playerObj = player1; // Default to player1.
        if (player == 1)
            playerObj = (Player1) player1;
        else if (player == 2)
            playerObj = (Player2) player2;
        else if (player == 3)
            playerObj = (Player3) player3;
        else if (player == 4)
            playerObj = (Player4) player4;

        Rectangle object;
        for (int i = 0; i < waterBlocks.size(); i++) {
            object = waterBlocks.get(i).getBoundary();
            if (object.intersects(playerObj.getBoundary())) {
                return true;
            }
        }
        return false;
    }

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

    public void standingInExplosion(int player) {
        Rectangle object;
        Player playerObj = player1; // Default to player1.
        if (player == 1)
            playerObj = (Player1) player1;
        else if (player == 2)
            playerObj = (Player2) player2;
        else if (player == 3)
            playerObj = (Player3) player3;
        else if (player == 4)
            playerObj = (Player4) player4;

        for (int i = 0; i < playerObj.getExplosion().size(); i++) {
            // NOT WORKING CORRECTLY
            object = playerObj.getExplosion().get(i).getBoundary();
            if (object.intersects(player1.getBoundary()) && !explosionDmgTaken1) {
                explosionDmgTaken1 = true;
                player1.decrementLives();
                player1.decrementLives();
                break;
            }
            if (object.intersects(player2.getBoundary()) && !explosionDmgTaken2) {
                explosionDmgTaken2 = true;
                player2.decrementLives();
                player2.decrementLives();
                break;
            }
            if (player3 != null) {
                if (object.intersects(player3.getBoundary()) && !explosionDmgTaken3) {
                    explosionDmgTaken3 = true;
                    player3.decrementLives();
                    player3.decrementLives();
                    break;
                }
            }
            if (player4 != null) {
                if (object.intersects(player4.getBoundary()) && !explosionDmgTaken4) {
                    explosionDmgTaken4 = true;
                    player4.decrementLives();
                    player4.decrementLives();
                    break;
                }
            }
        }
    }

    public boolean spaceToDropObstacle(int player) {
        Rectangle space = new Rectangle(0, 0, 0, 0);
        Rectangle object;
        Player.Direction dir = Player.Direction.UP;

        Player playerObj = player1; // Default to player1.
        if (player == 1)
            playerObj = (Player1) player1;
        else if (player == 2)
            playerObj = (Player2) player2;
        else if (player == 3)
            playerObj = (Player3) player3;
        else if (player == 4)
            playerObj = (Player4) player4;

        dir = playerObj.direction;
        int x = playerObj.getXpos();
        int y = playerObj.getYpos();

        int obstacleSize = 0;

        obstacleSize = playerObj.inventoryObstacleSize;

        int offset = (obstacleSize % 8) / 2;
        int fixPos = 0;
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
                // Placement assistance does not succeed if xy == null.
                if (xy == null)
                    return false;
                else {
                    space.setBounds(xy.getX(), xy.getY(), obstacleSize, obstacleSize);
                }
            }
        }

        // Stop player from placing an obstacle on top of another player.
        Rectangle p1 = null;
        Rectangle p2 = null;
        Rectangle p3 = null;
        Rectangle p4 = null;
        if (player == 1) {
            p2 = player2.getBoundary();
            if (p2.intersects(space) && !player2.dead)
                return false;
            if (player3 != null) {
                p3 = player3.getBoundary();
                if (p3.intersects(space) && !player3.dead)
                    return false;
            }
            if (player4 != null) {
                p4 = player4.getBoundary();
                if (p4.intersects(space) && !player4.dead)
                    return false;
            }
        } else if (player == 2) {
            p1 = player1.getBoundary();
            if (p1.intersects(space) && !player1.dead)
                return false;
            if (player3 != null) {
                p3 = player3.getBoundary();
                if (p3.intersects(space) && !player3.dead)
                    return false;
            }
            if (player4 != null) {
                p4 = player4.getBoundary();
                if (p4.intersects(space) && !player4.dead)
                    return false;
            }
        } else if (player == 3) {
            p1 = player1.getBoundary();
            p2 = player2.getBoundary();
            if (p1.intersects(space) && !player1.dead)
                return false;
            if (p2.intersects(space) && !player2.dead)
                return false;
            if (player4 != null) {
                p4 = player4.getBoundary();
                if (p4.intersects(space) && !player4.dead)
                    return false;
            }
        } else if (player == 4) {
            p1 = player1.getBoundary();
            p2 = player2.getBoundary();
            if (p1.intersects(space) && !player1.dead)
                return false;
            if (p2.intersects(space) && !player2.dead)
                return false;
            if (player3 != null) {
                p3 = player3.getBoundary();
                if (p3.intersects(space) && !player3.dead)
                    return false;
            }
        }

        obstacles.add(new Obstacle(space.x, space.y, true, playerObj.inventoryObstacleSize));
        return true;
    }

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

    public void healthPickUp(int player) {
        Player playerObj = player1; // Default to player1.
        if (player == 1)
            playerObj = (Player1) player1;
        else if (player == 2)
            playerObj = (Player2) player2;
        else if (player == 3)
            playerObj = (Player3) player3;
        else if (player == 4)
            playerObj = (Player4) player4;

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
        // TESTING powerup
        if (doubleRand < 0.20)
            powerUps.add(new PowerUp(x, y, 6));
        if (doubleRand >= 0.20 && doubleRand < 0.40)
            powerUps.add(new PowerUp(x, y, 6));
        if (doubleRand >= 0.40 && doubleRand < 0.60)
            powerUps.add(new PowerUp(x, y, 6));
        if (doubleRand >= 0.60 && doubleRand < 0.70)
            powerUps.add(new PowerUp(x, y, 6));
        if (doubleRand >= 0.70 && doubleRand < 0.80)
            powerUps.add(new PowerUp(x, y, 6));
        if (doubleRand >= 0.80 && doubleRand < 0.90)
            powerUps.add(new PowerUp(x, y, 6));
        if (doubleRand >= 0.90 && doubleRand <= 1)
            powerUps.add(new PowerUp(x, y, 6));
        return true;
    }

    public void powerUpPickUp(int player) {
        Player playerObj = player1; // Default to player1.
        if (player == 1)
            playerObj = (Player1) player1;
        else if (player == 2)
            playerObj = (Player2) player2;
        else if (player == 3)
            playerObj = (Player3) player3;
        else if (player == 4)
            playerObj = (Player4) player4;

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

    /* Hit: Checking for null values because player 3 and 4 does not always exist. */
    public void hit(int player) {
        Rectangle b;
        Rectangle p1 = new Rectangle(0, 0, 0, 0);
        Rectangle p2 = new Rectangle(0, 0, 0, 0);
        Rectangle p3 = new Rectangle(0, 0, 0, 0);
        Rectangle p4 = new Rectangle(0, 0, 0, 0);

        if (player == 1) {
            p2 = player2.getBoundary();
            p3 = (player3 != null) ? player3.getBoundary() : new Rectangle(0, 0, 0, 0);
            p4 = (player4 != null) ? player4.getBoundary() : new Rectangle(0, 0, 0, 0);
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
        } else if (player == 2) {
            p1 = player1.getBoundary();
            p3 = (player3 != null) ? player3.getBoundary() : new Rectangle(0, 0, 0, 0);
            p4 = (player4 != null) ? player4.getBoundary() : new Rectangle(0, 0, 0, 0);
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
        } else if (player == 3) {
            p1 = player1.getBoundary();
            p2 = player2.getBoundary();
            p4 = (player4 != null) ? player4.getBoundary() : new Rectangle(0, 0, 0, 0);
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
        } else if (player == 4) {
            p1 = player1.getBoundary();
            p2 = player2.getBoundary();
            p3 = (player3 != null) ? player3.getBoundary() : new Rectangle(0, 0, 0, 0);
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
        }

        Player playerObj = player1; // Default to player1.
        if (player == 1)
            playerObj = (Player1) player1;
        else if (player == 2)
            playerObj = (Player2) player2;
        else if (player == 3)
            playerObj = (Player3) player3;
        else if (player == 4)
            playerObj = (Player4) player4;

        Rectangle o;
        ArrayList<Bullet> bullets = new ArrayList<Bullet>();

        bullets.addAll(playerObj.getBullets());

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

    public void rocketHit(int player) {
        Rectangle b;
        Rectangle p1 = new Rectangle(0, 0, 0, 0);
        Rectangle p2 = new Rectangle(0, 0, 0, 0);
        Rectangle p3 = new Rectangle(0, 0, 0, 0);
        Rectangle p4 = new Rectangle(0, 0, 0, 0);

        // Collision with player objects
        if (player == 1) {
            p2 = player2.getBoundary();
            p3 = (player3 != null) ? player3.getBoundary() : new Rectangle(0, 0, 0, 0);
            p4 = (player4 != null) ? player4.getBoundary() : new Rectangle(0, 0, 0, 0);
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
        } else if (player == 2) {
            p1 = player1.getBoundary();
            p3 = (player3 != null) ? player3.getBoundary() : new Rectangle(0, 0, 0, 0);
            p4 = (player4 != null) ? player4.getBoundary() : new Rectangle(0, 0, 0, 0);
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

                // Rocket can collide with other players' bullets.
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
        } else if (player == 3) {
            p1 = player1.getBoundary();
            p2 = player2.getBoundary();
            p4 = (player4 != null) ? player4.getBoundary() : new Rectangle(0, 0, 0, 0);
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

                // Rocket can collide with other players' bullets.
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
        } else if (player == 4) {
            p1 = player1.getBoundary();
            p2 = player2.getBoundary();
            p3 = (player3 != null) ? player3.getBoundary() : new Rectangle(0, 0, 0, 0);
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

                // Rocket can collide with other players' bullets.
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
        }

        // Collision with obstacles
        Player playerObj = player1; // Default to player1.
        if (player == 1)
            playerObj = (Player1) player1;
        else if (player == 2)
            playerObj = (Player2) player2;
        else if (player == 3)
            playerObj = (Player3) player3;
        else if (player == 4)
            playerObj = (Player4) player4;

        Rectangle o;
        ArrayList<Rocket> rocket = playerObj.getRocket();

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

    /* Check for mine collision */
    public void mineHit(int player) {
        Rectangle b;
        Rectangle p1 = new Rectangle(0, 0, 0, 0);
        Rectangle p2 = new Rectangle(0, 0, 0, 0);
        Rectangle p3 = new Rectangle(0, 0, 0, 0);
        Rectangle p4 = new Rectangle(0, 0, 0, 0);

        // Collision with player objects
        if (player == 1) {
            p2 = player2.getBoundary();
            p3 = (player3 != null) ? player3.getBoundary() : new Rectangle(0, 0, 0, 0);
            p4 = (player4 != null) ? player4.getBoundary() : new Rectangle(0, 0, 0, 0);
            if (!player1.getMines().isEmpty()) {
                b = player1.getMines().get(0).getBoundary();
                if (p2.intersects(b) && !player2.dead) {
                    explosion(b.x, b.y, player1);
                    player1.getMines().clear();
                } else if (p3.intersects(b)) {
                    if (!player3.dead) {
                        explosion(b.x, b.y, player1);
                        player1.getMines().clear();
                    }
                } else if (p4.intersects(b)) {
                    if (!player4.dead) {
                        explosion(b.x, b.y, player1);
                        player1.getMines().clear();
                    }
                }
            }
        } else if (player == 2) {
            p1 = player1.getBoundary();
            p3 = (player3 != null) ? player3.getBoundary() : new Rectangle(0, 0, 0, 0);
            p4 = (player4 != null) ? player4.getBoundary() : new Rectangle(0, 0, 0, 0);
            if (!player2.getMines().isEmpty()) {
                b = player2.getMines().get(0).getBoundary();
                if (p1.intersects(b) && !player1.dead) {
                    explosion(b.x, b.y, player2);
                    player2.getMines().clear();
                } else if (p3.intersects(b)) {
                    if (!player3.dead) {
                        explosion(b.x, b.y, player2);
                        player2.getMines().clear();
                    }
                } else if (p4.intersects(b)) {
                    if (!player4.dead) {
                        explosion(b.x, b.y, player2);
                        player2.getMines().clear();
                    }
                }
            }
        } else if (player == 3) {
            p1 = player1.getBoundary();
            p2 = player2.getBoundary();
            p4 = (player4 != null) ? player4.getBoundary() : new Rectangle(0, 0, 0, 0);
            if (!player3.getMines().isEmpty()) {
                b = player3.getMines().get(0).getBoundary();
                if (p1.intersects(b) && !player1.dead) {
                    explosion(b.x, b.y, player3);
                    player3.getMines().clear();
                } else if (p2.intersects(b) && !player2.dead) {
                    explosion(b.x, b.y, player3);
                    player3.getMines().clear();
                } else if (p4.intersects(b)) {
                    if (!player4.dead) {
                        explosion(b.x, b.y, player3);
                        player3.getMines().clear();
                    }
                }
            }
        } else if (player == 4) {
            p1 = player1.getBoundary();
            p2 = player2.getBoundary();
            p3 = (player3 != null) ? player3.getBoundary() : new Rectangle(0, 0, 0, 0);
            if (!player4.getMines().isEmpty()) {
                b = player4.getMines().get(0).getBoundary();
                if (p1.intersects(b) && !player1.dead) {
                    explosion(b.x, b.y, player4);
                    player4.getMines().clear();
                } else if (p2.intersects(b) && !player2.dead) {
                    explosion(b.x, b.y, player4);
                    player4.getMines().clear();
                } else if (p3.intersects(b)) {
                    if (!player3.dead) {
                        explosion(b.x, b.y, player4);
                        player4.getMines().clear();
                    }
                }
            }
        }
    }

    public void explosion(int x, int y, Player player) {
        // Sound effect
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
                if (player.id == 1)
                    explosionDmgTaken1 = false;
                if (player.id == 2)
                    explosionDmgTaken2 = false;
                if (player.id == 3)
                    explosionDmgTaken3 = false;
                if (player.id == 4)
                    explosionDmgTaken4 = false;
                ((Timer) e.getSource()).stop();
            }
        });
        explosionTime.setInitialDelay(350);
        explosionTime.start();
    }
}