import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.*;
//import java.text.SimpleDateFormat;
//import java.io.Serializable;

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
    private int winner = 0;

    /* # Player Inventory # */
    PlayerInventory playerInventory;

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

    int maxHPSpawnCap;
    int maxPowerUpSpawnCap;

    /* # Damage To Player Boolean # */
    boolean underWaterP1;
    boolean underWaterP2;
    boolean underWaterP3;
    boolean underWaterP4;

    /* # Player Movement # */
    private InputMap im;
    private ActionMap am;
    private PlayerMovement playerMovement;

    public TAdapter tAdapter;

    public ArrayList<Boolean> stopSoundEffects;

    // private long countDownStartTime = -1;
    // private long countDownDuration = 120000;
    // public JLabel countDownLabel = new JLabel("...", JLabel.CENTER);

    public Battletronics(int height, int width, int nrOfPlayers) {
        /* Add Countdown Timer */
        // countDownLabel.setForeground(Color.white);
        // add(countDownLabel);

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

        /* ## Player Movement ## */
        playerMovement = new PlayerMovement();
        im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        am = getActionMap();
        new SetKeyBindings(im, am, playerMovement); // Void

        /* ## Player Objects ## */
        // TESTING
        this.nrOfPlayers = nrOfPlayers;
        player1 = new Player1(1, 3, 18, 18); // 18 18 // 120 120
        player2 = new Player2(2, 3, 232, 226); // 232 226 // 140 140
        if (nrOfPlayers >= 3)
            player3 = new Player3(3, 3, 18, 226);
        if (nrOfPlayers == 4)
            player4 = new Player4(4, 3, 232, 18);

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
        collisionDetection = new CollisionDetection(player1, player2, player3, player4, playerMovement, obstacles,
                healthPacks, powerUps, fireBlocks, waterBlocks, lightningBlocks, stopSoundEffects);

        /* ## Player Actions ## */
        tAdapter = new TAdapter();
        addKeyListener(tAdapter);
        playerInventory = new PlayerInventory(player1, player2, player3, player4, collisionDetection);

        /* ## Fire and Water Objects ## */
        fireRing = new FireRing(fireBlocks, width, height, xOffset, yOffset);
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

    public int getWinner() {
        return winner;
    }

    public boolean ongoingGame() {
        return ongoingGame;
    }

    public void shutdown() {
        ongoingGame = false;
    }

    public Player1 getPlayer1() {
        return player1;
    }

    public Player2 getPlayer2() {
        return player2;
    }

    /* ## GameLoop ## */
    @Override
    public void actionPerformed(ActionEvent e) {
        updateBulltes();
        updateRockets();
        checkIfPlayerIsUnderWater();
        healthPackSpawn.checkHealthPackCount();
        powerUpSpawn.checkPowerUpCount();

        // Collect player input data
        if (!player1.dead)
            player1.dirKeyPress(playerMovement.movement1.xStep, playerMovement.movement1.yStep);
        if (!player2.dead)
            player2.dirKeyPress(playerMovement.movement2.xStep, playerMovement.movement2.yStep);
        if (nrOfPlayers >= 3) {
            if (!player3.dead)
                player3.dirKeyPress(playerMovement.movement3.xStep, playerMovement.movement3.yStep);
        }
        if (nrOfPlayers == 4) {
            if (!player4.dead)
                player4.dirKeyPress(playerMovement.movement4.xStep, playerMovement.movement4.yStep);
        }

        // Move players according to input
        if (nrOfPlayers == 2)
            movePlayers(playerMovement.movement1.xStep, playerMovement.movement1.yStep, playerMovement.movement2.xStep,
                    playerMovement.movement2.yStep);
        else if (nrOfPlayers == 3)
            movePlayers(playerMovement.movement1.xStep, playerMovement.movement1.yStep, playerMovement.movement2.xStep,
                    playerMovement.movement2.yStep, playerMovement.movement3.xStep, playerMovement.movement3.yStep);
        else if (nrOfPlayers == 4)
            movePlayers(playerMovement.movement1.xStep, playerMovement.movement1.yStep, playerMovement.movement2.xStep,
                    playerMovement.movement2.yStep, playerMovement.movement3.xStep, playerMovement.movement3.yStep,
                    playerMovement.movement4.xStep, playerMovement.movement4.yStep);

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

        repaint();
    }

    /* ## Repaint: Called from Game Loop [Called from GameLoop] ## */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (nrOfPlayers == 2) {
            if (player1.getLives() > 0 && player2.getLives() > 0 && ongoingGame == true) {
                draw(g);
                // countDown(g); Currently not in use
            } else {
                gameOver(g);
            }
            // Toolkit.getDefaultToolkit().sync();
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

        List<Bullet> bulletsP1 = player1.getBullets();
        List<Bullet> bulletsP2 = player2.getBullets();
        List<Bullet> bulletsP3 = null;
        List<Bullet> bulletsP4 = null;
        if (nrOfPlayers >= 3)
            bulletsP3 = player3.getBullets();
        if (nrOfPlayers == 4)
            bulletsP4 = player4.getBullets();

        // DRAW BULLETS
        for (Bullet bulletP1 : bulletsP1) {
            g2.drawImage(bulletP1.getImage(), bulletP1.getXpos() + 2, bulletP1.getYpos() + 2, this); // +2 to center.
        }
        for (Bullet bulletP2 : bulletsP2) {
            g2.drawImage(bulletP2.getImage(), bulletP2.getXpos() + 2, bulletP2.getYpos() + 2, this);
        }
        if (nrOfPlayers >= 3)
            for (Bullet bulletP3 : bulletsP3) {
                g2.drawImage(bulletP3.getImage(), bulletP3.getXpos() + 2, bulletP3.getYpos() + 2, this);
            }
        if (nrOfPlayers == 4)
            for (Bullet bulletP4 : bulletsP4) {
                g2.drawImage(bulletP4.getImage(), bulletP4.getXpos() + 2, bulletP4.getYpos() + 2, this);
            }

        // DRAW ROCKETS
        for (Rocket rocketP1 : player1.getRocket()) {
            g2.drawImage(rocketP1.getImage(), rocketP1.getXpos() + 2, rocketP1.getYpos() + 2, this); // +2 to center.
        }
        for (Rocket rocketP2 : player2.getRocket()) {
            g2.drawImage(rocketP2.getImage(), rocketP2.getXpos() + 2, rocketP2.getYpos() + 2, this); // +2 to center.
        }
        if (nrOfPlayers >= 3)
            for (Rocket rocketP3 : player3.getRocket()) {
                g2.drawImage(rocketP3.getImage(), rocketP3.getXpos() + 2, rocketP3.getYpos() + 2, this); // +2 to center.
            }
        if (nrOfPlayers == 4)
            for (Rocket rocketP4 : player4.getRocket()) {
                g2.drawImage(rocketP4.getImage(), rocketP4.getXpos() + 2, rocketP4.getYpos() + 2, this); // +2 to center.
            }

        // DRAW MINES 
        for (Mine mineP1 : player1.getMines()) {
            if (mineP1.isVisible())
                g2.drawImage(mineP1.getImage(), mineP1.getXpos(), mineP1.getYpos(), this);
        }
        for (Mine mineP2 : player2.getMines()) {
            if (mineP2.isVisible())
                g2.drawImage(mineP2.getImage(), mineP2.getXpos(), mineP2.getYpos(), this);
        }
        if (nrOfPlayers >= 3)
            for (Mine mineP3 : player3.getMines()) {
                if (mineP3.isVisible())
                    g2.drawImage(mineP3.getImage(), mineP3.getXpos(), mineP3.getYpos(), this);
            }
        if (nrOfPlayers == 4)
            for (Mine mineP4 : player4.getMines()) {
                if (mineP4.isVisible())
                    g2.drawImage(mineP4.getImage(), mineP4.getXpos(), mineP4.getYpos(), this);
            }

        // DRAW EXPLOSIONS
        for (RocketExplosion explosionP1 : player1.getExplosion()) {
            g2.drawImage(explosionP1.getImage(), explosionP1.getXpos() - 14, explosionP1.getYpos() - 16, this);
        }
        for (RocketExplosion explosionP2 : player2.getExplosion()) {
            g2.drawImage(explosionP2.getImage(), explosionP2.getXpos() - 14, explosionP2.getYpos() - 16, this);
        }
        if (nrOfPlayers >= 3)
            for (RocketExplosion explosionP3 : player3.getExplosion()) {
                g2.drawImage(explosionP3.getImage(), explosionP3.getXpos() - 14, explosionP3.getYpos() - 16, this);
            }
        if (nrOfPlayers == 4)
            for (RocketExplosion explosionP4 : player4.getExplosion()) {
                g2.drawImage(explosionP4.getImage(), explosionP4.getXpos() - 14, explosionP4.getYpos() - 16, this);
            }

        // DRAW movable OBSTACLES
        for (Obstacle obstacle : obstacles) {
            if (obstacle.movable())
                g.drawImage(obstacle.getImage(), obstacle.getXpos(), obstacle.getYpos(), this);
        }

        for (WaterBlock water : waterBlocks) {
            g.drawImage(water.getImage(), water.getXpos(), water.getYpos(), this);
        }

        for (LightningBlock lightning : lightningBlocks) {
            g.drawImage(lightning.getImage(), lightning.getXpos(), lightning.getYpos(), this);
        }

        for (HealthPack health : healthPacks) {
            g.drawImage(health.getImage(), health.getXpos(), health.getYpos(), this);
        }

        for (Obstacle obstacle : obstacles) {
            if (!obstacle.movable())
                g.drawImage(obstacle.getImage(), obstacle.getXpos(), obstacle.getYpos(), this);
        }

        for (PowerUp powerUp : powerUps) {
            g.drawImage(powerUp.getImage(), powerUp.getXpos(), powerUp.getYpos(), this);
        }

        for (FireBlock fire : fireBlocks) {
            g.drawImage(fire.getImage(), fire.getXpos(), fire.getYpos(), this);
        }

        if (!player1.dead) {
            g2.drawImage(player1.getImage(), player1.getXpos(), player1.getYpos(), this);
            // g2.drawImage(new ImageIcon("Images/p1Hp3.png").getImage(), player1.getXpos(), player1.getYpos() - 3, this);
        }
        if (!player2.dead)
            g2.drawImage(player2.getImage(), player2.getXpos(), player2.getYpos(), this);
        if (player3 != null)
            if (!player3.dead)
                g2.drawImage(player3.getImage(), player3.getXpos(), player3.getYpos(), this);
        if (player4 != null)
            if (!player4.dead)
                g2.drawImage(player4.getImage(), player4.getXpos(), player4.getYpos(), this);

        g.setColor(Color.WHITE);
        g.drawString("Red lives: " + player1.getLives() + " / " + player1.maxLives, 5, 265);
        g.drawString("Green lives: " + player2.getLives() + " / " + player2.maxLives, 165, 265);
        if (nrOfPlayers >= 3)
            g.drawString("Purple lives: " + player3.getLives() + " / " + player3.maxLives, 5, 280);
        if (nrOfPlayers == 4)
            g.drawString("Orange lives: " + player4.getLives() + " / " + player4.maxLives, 160, 280);

        if (nrOfPlayers == 2) {
            g.drawString("Inventory:  " + player1.inventory + " / " + player1.inventoryMaxCap, 5, 280);
            g.drawString(
                    "Fire-rate:   " + (double) Math.round((1.0 / ((double) player1.reloadFreq / 1000)) * 100d) / 100d,
                    5, 295);
            g.drawString("Inventory:      " + player2.inventory + " / " + player2.inventoryMaxCap, 165, 280);
            g.drawString(
                    "Fire-rate:       "
                            + (double) Math.round((1.0 / ((double) player2.reloadFreq / 1000)) * 100d) / 100d,
                    165, 295);
        }
        /*
         * ImageIcon iconHP = new ImageIcon("Images/healthBar.png"); Image imgHP =
         * iconHP.getImage(); int widthHP = imgHP.getWidth(null); int heightHp =
         * imgHP.getHeight(null); g.drawImage(imgHP, 100, 270, this);
         */
    }

    /* ## GameOver: Prepare Window to be repainted [Called from GameLoop] ## */
    private void gameOver(Graphics g) {
        String color = "";
        if (nrOfPlayers == 2) {
            if (player1.getLives() <= 0) {
                winner = 2;
            } else if (player2.getLives() <= 0) {
                winner = 1;
            }
            if (winner == 1) {
                color = "Red";
                setBackground(Color.red);
                g.setColor(Color.white);
            } else if (winner == 2) {
                color = "Green";
                setBackground(Color.green);
                g.setColor(Color.black);
            }
        } else if (nrOfPlayers >= 3) {
            if (!player1.dead) {
                winner = 1;
                color = "Red";
                setBackground(Color.red);
                g.setColor(Color.white);
            } else if (!player2.dead) {
                winner = 2;
                color = "Green";
                setBackground(Color.green);
                g.setColor(Color.black);
            } else if (!player3.dead) {
                winner = 3;
                color = "Purple";
                setBackground(Color.pink);
                g.setColor(Color.black);
            } else if (nrOfPlayers == 4) {
                if (!player4.dead) {
                    winner = 4;
                    color = "Orange";
                    setBackground(Color.orange);
                    g.setColor(Color.black);
                }
            }
        }
        String message;
        if (color == "") {
            message = "No player wins!";
        } else {
            message = color + " player wins!";
        }
        String message2 = "Press R to restart";
        Font f = new Font("Nunito", Font.BOLD, 14);
        g.setFont(f);
        g.drawString(message, 75, 130);
        g.drawString(message2, 73, 185);
        startRestartGameOption();
        stopSoundEffects.add(true);
        removeKeyListener(tAdapter);
        ongoingGame = false;
    }

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

    private class RestartGameListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_R) {
                restartGame();
                ((JPanel) e.getSource()).removeKeyListener(this);
            }
        }
    }

    public void restartGame() {
        winner = 0;
        ongoingGame = true;
        tAdapter = new TAdapter();
        addKeyListener(tAdapter);
        stopSoundEffects.clear();
        setBackground(Color.black);
        setFocusable(true);
        gameLoopTimer.start();
        player1 = new Player1(1, 3, 18, 18);
        player2 = new Player2(2, 3, 232, 226);
        if (nrOfPlayers >= 3)
            player3 = new Player3(3, 3, 18, 226);
        if (nrOfPlayers == 4)
            player4 = new Player4(4, 3, 232, 18);
        obstacles = new ArrayList<Obstacle>();
        healthPacks = new ArrayList<HealthPack>();
        powerUps = new ArrayList<PowerUp>();
        fireBlocks = new ArrayList<FireBlock>();
        waterBlocks = new ArrayList<WaterBlock>();
        lightningBlocks = new ArrayList<LightningBlock>();
        createBattlefield = new CreateBattlefield(obstacles, width, height, xOffset, yOffset);
        createBattlefield.createBorder();
        createBattlefield.createBattlefield();
        collisionDetection = new CollisionDetection(player1, player2, player3, player4, playerMovement, obstacles,
                healthPacks, powerUps, fireBlocks, waterBlocks, lightningBlocks, stopSoundEffects);
        playerInventory = new PlayerInventory(player1, player2, player3, player4, collisionDetection);
        fireRing = new FireRing(fireBlocks, width, height, xOffset, yOffset);

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
    }

    /* ## RocketPowerUp Movement [Called from GameLoop] ## */
    // UNDER CONSTRUCTION
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
    }

    /* ## Player Movement: [Called from GameLoop] ## */
    public void movePlayers(int P1x, int P1y, int P2x, int P2y) {
        if (!collisionDetection.collision(1, false)) {
            player1.move(P1x, P1y);
        }
        if (!collisionDetection.collision(2, false)) {
            player2.move(P2x, P2y);
        }
    }

    /* ## Player Movement: 3 Players ## */
    public void movePlayers(int P1x, int P1y, int P2x, int P2y, int P3x, int P3y) {
        if (!collisionDetection.collision(1, false)) {
            player1.move(P1x, P1y);
        }
        if (!collisionDetection.collision(2, false)) {
            player2.move(P2x, P2y);
        }
        if (!collisionDetection.collision(3, false)) {
            player3.move(P3x, P3y);
        }
    }

    /* ## Player Movement: 4 Players ## */
    public void movePlayers(int P1x, int P1y, int P2x, int P2y, int P3x, int P3y, int P4x, int P4y) {
        if (!collisionDetection.collision(1, false)) {
            player1.move(P1x, P1y);
        }
        if (!collisionDetection.collision(2, false)) {
            player2.move(P2x, P2y);
        }
        if (!collisionDetection.collision(3, false)) {
            player3.move(P3x, P3y);
        }
        if (!collisionDetection.collision(4, false)) {
            player4.move(P4x, P4y);
        }
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

    /* ## CountDownTimer: [Called from GameLoop] ## */
    /*
     * public void countDown(Graphics g) { if (countDownStartTime < 0) {
     * countDownStartTime = System.currentTimeMillis(); } long now =
     * System.currentTimeMillis(); long clockTime = now - countDownStartTime; if
     * (clockTime >= countDownDuration) { clockTime = countDownDuration; }
     * SimpleDateFormat df = new SimpleDateFormat("mm:ss");
     * countDownLabel.setText(df.format(countDownDuration - clockTime)); }
     */

}
