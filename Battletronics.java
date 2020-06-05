import javax.swing.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.awt.*;
import java.awt.event.*;

public class Battletronics extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L; // Class ID

    /* # Game Loop # */
    private boolean ongoingGame = true;
    public Timer gameLoopTimer;
    private final int gameLoopUpdateRate = 10; // milliseconds

    /* # Window # */
    private int height;
    private int width;
    private int xOffset;
    private int yOffset;

    /* # Players # */
    public int nrOfPlayers;
    public Player1 player1;
    public Player2 player2;
    public Player3 player3;
    public Player4 player4;
    private List<Player> players;
    private int winner = 0;
    private String endGameColor = "";

    /* Bullet types */
    HashSet<Bullet> allBullets = new HashSet<Bullet>();
    HashSet<Rocket> allRockets = new HashSet<Rocket>();
    HashSet<Mine> allMines = new HashSet<Mine>();
    HashSet<RocketExplosion> allExplosions = new HashSet<RocketExplosion>();

    /* # Player Inventory # */
    PlayerInventory playerInventory;
    public TAdapter tAdapter;

    /* # Terrain on playingfield # */
    private CreateBattlefield createBattlefield;
    private CollisionDetection collisionDetection;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<HealthPack> healthPacks;
    private ArrayList<PowerUp> powerUps;
    private ArrayList<FireBlock> fireBlocks;
    private ArrayList<WaterBlock> waterBlocks;
    private ArrayList<LightningBlock> lightningBlocks;
    private HealthPackSpawn healthPackSpawn;
    private PowerUpSpawn powerUpSpawn;
    private FireRing fireRing;
    private WaterFlood waterFlood;
    private LightningStorm lightningStorm;
    Environment environment;

    /* Stop soundeffects when game is ended */
    public ArrayList<Boolean> stopSoundEffects;

    /* # Damage To Player Boolean # */
    boolean underWaterP1;
    boolean underWaterP2;
    boolean underWaterP3;
    boolean underWaterP4;

    /* Machine gun indicator */
    double progressBar1 = 360;
    double progressBar2 = 360;
    double progressBar3 = 360;
    double progressBar4 = 360;

    /* # Player Movement # */
    private InputMap im;
    private ActionMap am;

    public Battletronics(int height, int width, int nrOfPlayers) {

        /* ## Window Metrics ## */
        this.height = height;
        this.width = width;
        xOffset = 65;
        yOffset = 32;
        setBackground(Color.black);
        setFocusable(true);

        /* ## Timer: For Game Loop ## */
        gameLoopTimer = new Timer(gameLoopUpdateRate, this);
        gameLoopTimer.start();

        /* ## Player Objects ## 
                @params: ID, startingHealth, xPos, yPos ## */
        this.nrOfPlayers = nrOfPlayers;
        player1 = new Player1(1, 3, 18, 18);
        player2 = new Player2(2, 3, 232, 226);
        if (nrOfPlayers >= 3)
            player3 = new Player3(3, 3, 18, 226);
        if (nrOfPlayers == 4)
            player4 = new Player4(4, 3, 232, 18);
        players = Arrays.asList(player1, player2, player3, player4);

        /* ## Player Movement ## */
        im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        am = getActionMap();
        new SetKeyBindings(players, im, am); // Void

        /* ## Object Lists ## */
        obstacles = new ArrayList<Obstacle>();
        healthPacks = new ArrayList<HealthPack>();
        powerUps = new ArrayList<PowerUp>();
        fireBlocks = new ArrayList<FireBlock>();
        waterBlocks = new ArrayList<WaterBlock>();
        lightningBlocks = new ArrayList<LightningBlock>();
        stopSoundEffects = new ArrayList<Boolean>();

        /* ## Battlefield Object ## */
        createBattlefield = new CreateBattlefield(obstacles, width, height, xOffset, yOffset);
        createBattlefield.createBorder();
        createBattlefield.createBattlefield();

        /* ## Collision Object ## */
        collisionDetection = new CollisionDetection(player1, player2, player3, player4, obstacles, healthPacks,
                powerUps, fireBlocks, waterBlocks, lightningBlocks, stopSoundEffects);

        /* ## Player Actions ## */
        tAdapter = new TAdapter();
        addKeyListener(tAdapter);
        playerInventory = new PlayerInventory(player1, player2, player3, player4, collisionDetection);

        /* ## Environment System Objects ## */
        fireRing = new FireRing(fireBlocks, width, height, xOffset, yOffset, stopSoundEffects);
        waterFlood = new WaterFlood(waterBlocks, width, height, xOffset, yOffset, stopSoundEffects);
        lightningStorm = new LightningStorm(lightningBlocks, width, height, xOffset, yOffset);

        /* ## Environment: decides terrain on playing field */
        environment = new Environment(player1, player2, player3, player4, waterFlood, fireRing, lightningStorm,
                collisionDetection, -1);

        /* ## HealthPacks: spawn and limit spawn count */
        healthPackSpawn = new HealthPackSpawn(healthPacks, width, height, xOffset, yOffset, collisionDetection);

        /* ## PowerUpSpawn: spawn and limit spawn count */
        powerUpSpawn = new PowerUpSpawn(powerUps, width, height, xOffset, yOffset, collisionDetection);
    }

    /* ## GameLoop: Responsible for all update to the game ## */
    @Override
    public void actionPerformed(ActionEvent e) {
        updateBulltes();
        updateRockets();
        updateMines();
        updateExplosions();

        checkIfPlayerIsUnderWater();
        healthPackSpawn.checkHealthPackCount();
        powerUpSpawn.checkPowerUpCount();

        // Update player Movement
        if (!player1.dead) {
            Pair dxdy = player1.calculateDirection();
            if (!collisionDetection.collision(1, false)) {
                player1.move(dxdy.dx, dxdy.dy);
            }
        }
        if (!player2.dead) {
            Pair dxdy = player2.calculateDirection();
            if (!collisionDetection.collision(2, false)) {
                player2.move(dxdy.dx, dxdy.dy);
            }
        }
        if (nrOfPlayers >= 3) {
            if (!player3.dead) {
                Pair dxdy = player3.calculateDirection();
                if (!collisionDetection.collision(3, false)) {
                    player3.move(dxdy.dx, dxdy.dy);
                }
            }
        }
        if (nrOfPlayers == 4) {
            if (!player4.dead) {
                Pair dxdy = player4.calculateDirection();
                if (!collisionDetection.collision(4, false)) {
                    player4.move(dxdy.dx, dxdy.dy);
                }
            }
        }

        // Check healthpack pickup
        collisionDetection.healthPickUp(1);
        collisionDetection.healthPickUp(2);
        if (nrOfPlayers >= 3)
            collisionDetection.healthPickUp(3);
        if (nrOfPlayers == 4)
            collisionDetection.healthPickUp(4);

        // Check powerup pickup
        collisionDetection.powerUpPickUp(1);
        collisionDetection.powerUpPickUp(2);
        if (nrOfPlayers >= 3)
            collisionDetection.powerUpPickUp(3);
        if (nrOfPlayers == 4)
            collisionDetection.powerUpPickUp(4);

        // Check bullet collision
        collisionDetection.hit(1);
        collisionDetection.hit(2);
        if (nrOfPlayers >= 3)
            collisionDetection.hit(3);
        if (nrOfPlayers == 4)
            collisionDetection.hit(4);

        // Check rocket collision
        collisionDetection.rocketHit(1);
        collisionDetection.rocketHit(2);
        if (nrOfPlayers >= 3)
            collisionDetection.rocketHit(3);
        if (nrOfPlayers == 4)
            collisionDetection.rocketHit(4);

        // Check mine collision
        collisionDetection.mineHit(1);
        collisionDetection.mineHit(2);
        if (nrOfPlayers >= 3)
            collisionDetection.mineHit(3);
        if (nrOfPlayers == 4)
            collisionDetection.mineHit(4);

        // Check inventory actions
        if (!player1.dead)
            playerInventory.playerInventoryAction(1);
        if (!player2.dead)
            playerInventory.playerInventoryAction(2);
        if (nrOfPlayers >= 3)
            if (!player3.dead)
                playerInventory.playerInventoryAction(3);
        if (nrOfPlayers == 4)
            if (!player4.dead)
                playerInventory.playerInventoryAction(4);

        // Identify which players have lost
        if (nrOfPlayers >= 3) {
            if (player1.lives <= 0) {
                player1.dead = true;
            }
            if (player2.lives <= 0) {
                player2.dead = true;
            }
            if (player3.lives <= 0) {
                player3.dead = true;
            }
            if (nrOfPlayers == 4) {
                if (player4.lives <= 0) {
                    player4.dead = true;
                }
            }
        }

        // Paint all these updates!
        repaint();
    }

    /* ## Repaint: [Called from GameLoop] ## */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (nrOfPlayers == 2) {
            if (player1.getLives() > 0 && player2.getLives() > 0 && ongoingGame == true) {
                draw(g);
            } else {
                gameOver(g);
            }
        } else if (nrOfPlayers == 3) {
            int p1Alive = player1.getLives() <= 0 ? 0 : 1;
            int p2Alive = player2.getLives() <= 0 ? 0 : 1;
            int p3Alive = player3.getLives() <= 0 ? 0 : 1;
            if (p1Alive + p2Alive + p3Alive > 1 && ongoingGame == true) {
                draw(g);
            } else {
                gameOver(g);
            }
        } else if (nrOfPlayers == 4) {
            int p1Alive = player1.getLives() <= 0 ? 0 : 1;
            int p2Alive = player2.getLives() <= 0 ? 0 : 1;
            int p3Alive = player3.getLives() <= 0 ? 0 : 1;
            int p4Alive = player4.getLives() <= 0 ? 0 : 1;
            if (p1Alive + p2Alive + p3Alive + p4Alive > 1 && ongoingGame == true) {
                draw(g);
            } else {
                gameOver(g);
            }
        }
    }

    /* ## Draw: Window Graphics [Called from GameLoop] ## */
    private void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // DRAW BULLETS
        for (Bullet bullet : allBullets) {
            g2.drawImage(bullet.getImage(), bullet.getXpos() + 2, bullet.getYpos() + 2, this); // +2 to center.
        }

        // DRAW ROCKETS
        for (Rocket rocket : allRockets) {
            g2.drawImage(rocket.getImage(), rocket.getXpos() + 2, rocket.getYpos() + 2, this); // +2 to center.
        }

        // DRAW MINES 
        for (Mine mine : allMines) {
            if (mine.isVisible())
                g2.drawImage(mine.getImage(), mine.getXpos() - 4, mine.getYpos() - 4, this);
        }

        // DRAW EXPLOSIONS
        for (RocketExplosion explosion : allExplosions) {
            g2.drawImage(explosion.getImage(), explosion.getXpos() - 14, explosion.getYpos() - 16, this);
        }

        // DRAW MOVABLE OBSTACLES
        for (Obstacle obstacle : obstacles) {
            if (obstacle.movable())
                g.drawImage(obstacle.getImage(), obstacle.getXpos(), obstacle.getYpos(), this);
        }

        // DRAW WATER
        for (WaterBlock water : waterBlocks) {
            g.drawImage(water.getImage(), water.getXpos(), water.getYpos(), this);
        }

        // DRAW LIGHTNING
        for (LightningBlock lightning : lightningBlocks) {
            g.drawImage(lightning.getImage(), lightning.getXpos(), lightning.getYpos(), this);
        }

        // DRAW HEALTH
        for (HealthPack health : healthPacks) {
            g.drawImage(health.getImage(), health.getXpos(), health.getYpos(), this);
        }

        // DRAW SOLID OBSTACLES
        for (Obstacle obstacle : obstacles) {
            if (!obstacle.movable())
                g.drawImage(obstacle.getImage(), obstacle.getXpos(), obstacle.getYpos(), this);
        }

        // DRAW POWERUPS
        for (PowerUp powerUp : powerUps) {
            g.drawImage(powerUp.getImage(), powerUp.getXpos(), powerUp.getYpos(), this);
        }

        // DRAW FIRE
        for (FireBlock fire : fireBlocks) {
            g.drawImage(fire.getImage(), fire.getXpos(), fire.getYpos(), this);
        }

        // DRAW PLAYERS
        if (!player1.dead) {
            g2.drawImage(player1.getImage(), player1.getXpos(), player1.getYpos(), this);
            g.setFont(new Font("Nunito", Font.PLAIN, 7));
            g.setColor(Color.WHITE);
            g.drawString(player1.inventory + " / " + player1.inventoryMaxCap, player1.getXpos() - 3,
                    player1.getYpos() - 2);
        }

        if (!player2.dead) {
            g2.drawImage(player2.getImage(), player2.getXpos(), player2.getYpos(), this);
            g.setFont(new Font("Nunito", Font.PLAIN, 7));
            g.setColor(Color.WHITE);
            g.drawString(player2.inventory + " / " + player2.inventoryMaxCap, player2.getXpos() - 3,
                    player2.getYpos() - 2);
        }

        if (player3 != null)
            if (!player3.dead) {
                g2.drawImage(player3.getImage(), player3.getXpos(), player3.getYpos(), this);
                g.setFont(new Font("Nunito", Font.PLAIN, 7));
                g.setColor(Color.WHITE);
                g.drawString(player3.inventory + " / " + player3.inventoryMaxCap, player3.getXpos() - 3,
                        player3.getYpos() - 2);
            }
        if (player4 != null)
            if (!player4.dead) {
                g2.drawImage(player4.getImage(), player4.getXpos(), player4.getYpos(), this);
                g.setFont(new Font("Nunito", Font.PLAIN, 7));
                g.setColor(Color.WHITE);
                g.drawString(player4.inventory + " / " + player4.inventoryMaxCap, player4.getXpos() - 3,
                        player4.getYpos() - 2);
            }

        // WRITE PLAYER STATS
        g.setFont(new Font("Nunito", Font.BOLD, 13)); // Optional
        g.setColor(Color.RED); // Optional
        g.drawString("HP: " + player1.getLives() + " / " + player1.maxLives, (width - xOffset) / 4 - 3, 268);

        g.setColor(Color.GREEN); // Optional
        g.drawString("HP: " + player2.getLives() + " / " + player2.maxLives, (width - xOffset) / 2 + 10, 268);

        g.setColor(Color.MAGENTA); // Optional
        if (nrOfPlayers >= 3)
            g.drawString("HP: " + player3.getLives() + " / " + player3.maxLives, (width - xOffset) / 4 - 3, 288);

        g.setColor(Color.ORANGE); // Optional
        if (nrOfPlayers == 4)
            g.drawString("HP: " + player4.getLives() + " / " + player4.maxLives, (width - xOffset) / 2 + 10, 288);

        /*
        if (nrOfPlayers == 2) {
        g.setColor(Color.RED); // Optional
        g.drawString("Inventory:  " + player1.inventory + " / " + player1.inventoryMaxCap, 5, 280);
        g.drawString(
                "Fire-rate:   " + (double) Math.round((1.0 / ((double) player1.reloadFreq / 1000)) * 100d) / 100d,
                5, 295);
        g.setColor(Color.GREEN); // Optional
        g.drawString("Inventory:     " + player2.inventory + " / " + player2.inventoryMaxCap, 165, 280);
        g.drawString(
                "Fire-rate:      "
                        + (double) Math.round((1.0 / ((double) player2.reloadFreq / 1000)) * 100d) / 100d,
                165, 295);
        }*/

        // DRAW MACHINE GUN PROGRESS BAR
        if (player1.holdingMachineGun) {
            machineGunProgressBar(g2, player1, progressBar1);
            progressBar1 -= 0.54;
            if (progressBar1 <= 0)
                progressBar1 = 360;
        }
        if (player2.holdingMachineGun) {
            machineGunProgressBar(g2, player2, progressBar2);
            progressBar2 -= 0.54;
            if (progressBar2 <= 0)
                progressBar2 = 360;
        }
        if (player3 != null)
            if (player3.holdingMachineGun) {
                machineGunProgressBar(g2, player3, progressBar3);
                progressBar3 -= 0.54;
                if (progressBar3 <= 0)
                    progressBar3 = 360;
            }
        if (player4 != null) {
            if (player4.holdingMachineGun) {
                machineGunProgressBar(g2, player4, progressBar4);
                progressBar4 -= 0.54;
            }
            if (progressBar4 <= 0)
                progressBar4 = 360;
        }

        // DRAW SNIPER BULLET COUNT
        if (!player1.dead && player1.bulletSound == Player.BulletSound.SNIPER) {
            sniperAmmo(g2, player1);
        }
        if (!player2.dead && player2.bulletSound == Player.BulletSound.SNIPER) {
            sniperAmmo(g2, player2);
        }
        if (player3 != null)
            if (!player3.dead && player3.bulletSound == Player.BulletSound.SNIPER) {
                sniperAmmo(g2, player3);
            }
        if (player4 != null) {
            if (!player4.dead && player4.bulletSound == Player.BulletSound.SNIPER) {
                sniperAmmo(g2, player4);
            }
        }
    }

    public void machineGunProgressBar(Graphics2D g2, Player player, double progress) {
        g2.setColor(Color.WHITE);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.translate(player.getXpos() + 17, player.getYpos() - 5);
        Arc2D.Float arc = new Arc2D.Float(Arc2D.PIE);
        arc.setFrameFromCenter(new Point(0, 0), new Point(3, 3));
        arc.setAngleStart(90);
        arc.setAngleExtent(-progress);
        g2.draw(arc);
        g2.fill(arc);
    }

    public void sniperAmmo(Graphics2D g2, Player player) {
        g2.setColor(Color.LIGHT_GRAY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.translate(player.getXpos() + 15, player.getYpos() - 5);
        Rectangle2D.Float rec = new Rectangle2D.Float();
        if (player.sniperAmmo >= 1) {
            rec.setFrameFromCenter(new Point(0, 0), new Point(1, 1));
            g2.fill(rec);
        }
        if (player.sniperAmmo >= 2) {
            rec.setFrameFromCenter(new Point(0, 3), new Point(1, 4));
            g2.fill(rec);
        }
        if (player.sniperAmmo >= 3) {
            rec.setFrameFromCenter(new Point(0, 6), new Point(1, 7));
            g2.fill(rec);
        }
        if (player.sniperAmmo >= 4) {
            rec.setFrameFromCenter(new Point(0, 9), new Point(1, 10));
            g2.fill(rec);
        }
        if (player.sniperAmmo >= 5) {
            rec.setFrameFromCenter(new Point(0, 12), new Point(1, 13));
            g2.fill(rec);
        }
    }

    /* ## GameOver: Prepare Window to be repainted [Called from GameLoop] ## */
    private void gameOver(Graphics g) {

        if (nrOfPlayers == 2) {
            if (player1.getLives() <= 0) {
                winner = 2;
            } else if (player2.getLives() <= 0) {
                winner = 1;
            }
            if (winner == 1) {
                endGameColor = "Red";
                setBackground(Color.red);
                g.setColor(Color.white);
            } else if (winner == 2) {
                endGameColor = "Green";
                setBackground(Color.green);
                g.setColor(Color.black);
            }
        } else if (nrOfPlayers >= 3) {
            if (!player1.dead) {
                player1.dead = true;
                winner = 1;
                endGameColor = "Red";
                setBackground(Color.red);
                g.setColor(Color.WHITE);
            } else if (!player2.dead) {
                player2.dead = true;
                winner = 2;
                endGameColor = "Green";
                setBackground(Color.green);
                g.setColor(Color.BLACK);
            } else if (!player3.dead) {
                player3.dead = true;
                winner = 3;
                endGameColor = "Purple";
                setBackground(Color.MAGENTA);
                g.setColor(Color.BLACK);
            } else if (nrOfPlayers == 4) {
                if (!player4.dead) {
                    player4.dead = true;
                    winner = 4;
                    endGameColor = "Orange";
                    setBackground(Color.orange);
                    g.setColor(Color.WHITE);
                }
            }
        }
        String message;
        if (endGameColor == "") {
            message = "No player wins!";
        } else {
            message = endGameColor + " player wins!";
        }
        String message2 = "Press R to restart";
        Font f = new Font("Nunito", Font.BOLD, 14);
        g.setFont(f);
        g.setColor(Color.WHITE);
        g.drawString(message, 75, 130);
        g.drawString(message2, 73, 185);
        startRestartGameOption();
        stopSoundEffects.add(true);
        removeKeyListener(tAdapter);
        ongoingGame = false;
    }

    /* ## StartRestartGameOption: Stop game loop and activate restart key ## */
    public void startRestartGameOption() {
        if (ongoingGame) {
            Timer delayEndScreen = new Timer(10, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    gameLoopTimer.stop();
                    addKeyListener(new RestartGameListener());
                }
            });
            delayEndScreen.setRepeats(false);
            delayEndScreen.start();
        }
    }

    private class RestartGameListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_R) {
                restartGame();
                ((JPanel) e.getSource()).removeKeyListener(this);
            }
        }
    }

    /* ## Player Actions: KeyEventListener ## */
    private class TAdapter extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            if (!player1.dead)
                player1.btnRelease(e);
            if (!player2.dead)
                player2.btnRelease(e);
            if (nrOfPlayers >= 3) {
                if (!player3.dead)
                    player3.btnRelease(e);
            }
            if (nrOfPlayers == 4)
                if (!player4.dead)
                    player4.btnRelease(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (!player1.dead)
                player1.btnPress(e);
            if (!player2.dead)
                player2.btnPress(e);
            if (nrOfPlayers >= 3) {
                if (!player3.dead)
                    player3.btnPress(e);
                if (nrOfPlayers == 4)
                    if (!player4.dead)
                        player4.btnPress(e);
            }
        }
    }

    public void restartGame() {
        endGameColor = "";
        winner = 0;
        ongoingGame = true;
        tAdapter = new TAdapter();
        addKeyListener(tAdapter);
        setBackground(Color.black);
        setFocusable(true);
        stopSoundEffects.clear();
        gameLoopTimer.start();

        progressBar1 = 360;
        progressBar2 = 360;
        progressBar3 = 360;
        progressBar4 = 360;

        player1 = new Player1(1, 3, 18, 18);
        player2 = new Player2(2, 3, 232, 226);
        if (nrOfPlayers >= 3)
            player3 = new Player3(3, 3, 18, 226);
        if (nrOfPlayers == 4)
            player4 = new Player4(4, 3, 232, 18);

        players = Arrays.asList(player1, player2, player3, player4);
        new SetKeyBindings(players, im, am);

        // Stop timers from earlier rounds.
        if (environment.fireTimer != null)
            environment.fireTimer.stop();
        if (environment.waterTimer != null)
            environment.waterTimer.stop();
        if (environment.lightningTimer != null)
            environment.lightningTimer.stop();

        obstacles = new ArrayList<Obstacle>();
        healthPacks = new ArrayList<HealthPack>();
        powerUps = new ArrayList<PowerUp>();
        fireBlocks = new ArrayList<FireBlock>();
        waterBlocks = new ArrayList<WaterBlock>();
        lightningBlocks = new ArrayList<LightningBlock>();

        createBattlefield = new CreateBattlefield(obstacles, width, height, xOffset, yOffset);
        createBattlefield.createBorder();
        createBattlefield.createBattlefield();
        collisionDetection = new CollisionDetection(player1, player2, player3, player4, obstacles, healthPacks,
                powerUps, fireBlocks, waterBlocks, lightningBlocks, stopSoundEffects);
        playerInventory = new PlayerInventory(player1, player2, player3, player4, collisionDetection);
        fireRing = new FireRing(fireBlocks, width, height, xOffset, yOffset, stopSoundEffects);

        if (waterFlood.waitUnitlNextTsunami != null)
            waterFlood.waitUnitlNextTsunami.stop();
        if (waterFlood.tsunamiTimer != null)
            waterFlood.tsunamiTimer.stop();
        waterFlood.waterCount = 0;

        waterFlood = new WaterFlood(waterBlocks, width, height, xOffset, yOffset, stopSoundEffects);
        lightningStorm = new LightningStorm(lightningBlocks, width, height, xOffset, yOffset);
        healthPackSpawn = new HealthPackSpawn(healthPacks, width, height, xOffset, yOffset, collisionDetection);
        powerUpSpawn = new PowerUpSpawn(powerUps, width, height, xOffset, yOffset, collisionDetection);

        boolean fireRecently = environment.fire;
        boolean waterRecently = environment.water;
        boolean lightningRecently = environment.lightning;
        int recentEnvironment = -1;
        if (fireRecently)
            recentEnvironment = 1;
        else if (waterRecently)
            recentEnvironment = 2;
        else if (lightningRecently)
            recentEnvironment = 3;

        environment.fire = false;
        environment.water = false;
        environment.lightning = false;

        environment = new Environment(player1, player2, player3, player4, waterFlood, fireRing, lightningStorm,
                collisionDetection, recentEnvironment);
    }

    /* ## Bullet Movement [Called from GameLoop] ## */
    private void updateBulltes() {
        List<Bullet> bulletsP1 = player1.getBullets();
        if (!player1.dead) {
            for (int i = 0; i < bulletsP1.size(); i++) {
                Bullet bulletP1 = bulletsP1.get(i);
                if (bulletP1.isVisible()) {
                    bulletP1.movement(player1.getDirection(), player1.getBulletSpeed());
                } else {
                    bulletsP1.remove(i);
                }
            }
        } else {
            bulletsP1.clear();
        }

        List<Bullet> bulletsP2 = player2.getBullets();
        if (!player2.dead) {
            for (int i = 0; i < bulletsP2.size(); i++) {
                Bullet bulletP2 = bulletsP2.get(i);
                if (bulletP2.isVisible()) {
                    bulletP2.movement(player2.getDirection(), player2.getBulletSpeed());
                } else {
                    bulletsP2.remove(i);
                }
            }
        } else {
            bulletsP2.clear();
        }

        if (nrOfPlayers >= 3) {
            List<Bullet> bulletsP3 = player3.getBullets();
            if (!player3.dead) {
                for (int i = 0; i < bulletsP3.size(); i++) {
                    Bullet bulletP3 = bulletsP3.get(i);
                    if (bulletP3.isVisible()) {
                        bulletP3.movement(player3.getDirection(), player3.getBulletSpeed());
                    } else {
                        bulletsP3.remove(i);
                    }
                }
            } else {
                bulletsP3.clear();
            }
        }
        if (nrOfPlayers == 4) {
            List<Bullet> bulletsP4 = player4.getBullets();
            if (!player4.dead) {
                for (int i = 0; i < bulletsP4.size(); i++) {
                    Bullet bulletP4 = bulletsP4.get(i);
                    if (bulletP4.isVisible()) {
                        bulletP4.movement(player4.getDirection(), player4.getBulletSpeed());
                    } else {
                        bulletsP4.remove(i);
                    }
                }
            } else {
                bulletsP4.clear();
            }
        }

        allBullets.clear();
        allBullets.addAll(player1.getBullets());
        allBullets.addAll(player2.getBullets());
        if (nrOfPlayers >= 3)
            allBullets.addAll(player3.getBullets());
        if (nrOfPlayers == 4)
            allBullets.addAll(player4.getBullets());
    }

    /* ## Rocket Movement [Called from GameLoop] ## */
    private void updateRockets() {
        ArrayList<Rocket> rocketP1 = player1.getRocket();
        if (!player1.dead) {
            for (int i = 0; i < rocketP1.size(); i++) {
                Rocket rocket = rocketP1.get(i);
                if (rocket.isVisible()) {
                    rocket.movement(player1.getDirection(), 1);
                } else {
                    rocketP1.remove(i);
                }
            }
        } else {
            rocketP1.clear();
        }

        ArrayList<Rocket> rocketP2 = player2.getRocket();
        if (!player2.dead) {
            for (int i = 0; i < rocketP2.size(); i++) {
                Rocket rocket = rocketP2.get(i);
                if (rocket.isVisible()) {
                    rocket.movement(player2.getDirection(), 1);
                } else {
                    rocketP2.remove(i);
                }
            }
        } else {
            rocketP2.clear();
        }

        if (nrOfPlayers >= 3) {
            ArrayList<Rocket> rocketP3 = player3.getRocket();
            if (!player3.dead) {
                for (int i = 0; i < rocketP3.size(); i++) {
                    Rocket rocket = rocketP3.get(i);
                    if (rocket.isVisible()) {
                        rocket.movement(player3.getDirection(), 1);
                    } else {
                        rocketP3.remove(i);
                    }
                }
            } else {
                rocketP3.clear();
            }
        }

        if (nrOfPlayers == 4) {
            ArrayList<Rocket> rocketP4 = player4.getRocket();
            if (!player4.dead) {
                for (int i = 0; i < rocketP4.size(); i++) {
                    Rocket rocket = rocketP4.get(i);
                    if (rocket.isVisible()) {
                        rocket.movement(player4.getDirection(), 1);
                    } else {
                        rocketP4.remove(i);
                    }
                }
            } else {
                rocketP4.clear();
            }
        }
        allRockets.clear();
        allRockets.addAll(player1.getRocket());
        allRockets.addAll(player2.getRocket());
        if (nrOfPlayers >= 3)
            allRockets.addAll(player3.getRocket());
        if (nrOfPlayers == 4)
            allRockets.addAll(player4.getRocket());
    }

    /* ## Mines: [Called from GameLoop] ## */
    public void updateMines() {
        allMines.clear();
        allMines.addAll(player1.getMines());
        allMines.addAll(player2.getMines());
        if (nrOfPlayers >= 3)
            allMines.addAll(player3.getMines());
        if (nrOfPlayers == 4)
            allMines.addAll(player4.getMines());
    }

    /* ## Explosions: [Called from GameLoop] ## */
    public void updateExplosions() {
        allExplosions.clear();
        allExplosions.addAll(player1.getExplosion());
        allExplosions.addAll(player2.getExplosion());
        if (nrOfPlayers >= 3)
            allExplosions.addAll(player3.getExplosion());
        if (nrOfPlayers == 4)
            allExplosions.addAll(player4.getExplosion());
    }

    /* ## Water DMG: Clock reset if DMG ticked [Called from GameLoop] ## */
    public void checkIfPlayerIsUnderWater() {
        if (environment.isWaterEnvironment()) {
            if (!player1.dead) {
                underWaterP1 = collisionDetection.standingInWater(1);
                if (underWaterP1 == false) {
                    environment.restartWaterClock1();
                }
            }
            if (!player2.dead) {
                underWaterP2 = collisionDetection.standingInWater(2);
                if (underWaterP2 == false) {
                    environment.restartWaterClock2();
                }
            }
            if (nrOfPlayers >= 3) {
                if (!player3.dead) {
                    underWaterP3 = collisionDetection.standingInWater(3);
                    if (underWaterP3 == false && nrOfPlayers >= 3) {
                        environment.restartWaterClock3();
                    }
                }
            }
            if (nrOfPlayers == 4) {
                if (!player4.dead) {
                    underWaterP4 = collisionDetection.standingInWater(4);
                    if (underWaterP4 == false && nrOfPlayers == 4) {
                        environment.restartWaterClock4();
                    }
                }
            }
        }
    }
}
