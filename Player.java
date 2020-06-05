import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Player extends Sprite {
    private static final long serialVersionUID = 1L;

    enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    enum BulletSound {
        RIFLE, SNIPER, MACHINEGUN
    }

    public int id;
    public boolean dead;

    public Timer reloadTimer;
    public boolean reload;
    public int reloadFreq = 250;

    public boolean pickUpOrDrop;
    public int inventoryFrequency = 450;
    public int inventoryObstacleSize = 8;
    public int inventory;
    public int inventoryMaxCap = 3;

    protected int lives;
    public int maxLives;

    public Direction direction;
    HashMap<String, Boolean> keyState = new HashMap<String, Boolean>();
    ArrayList<String> directionPreference = new ArrayList<String>();

    public int bulletSpeed = 3;
    public BulletSound bulletSound = BulletSound.RIFLE;

    protected List<Bullet> bullets;
    public ArrayList<Rocket> rocket;
    public ArrayList<RocketExplosion> rocketExplosion;
    public ArrayList<Mine> mines;

    public boolean holdingMachineGun;
    public int sniperAmmo = 5;

    public boolean disablePlayerMovement = false;
    public boolean disablePlayerShooting = false;

    public Player(int id, int lives, int xpos, int ypos) {
        super(xpos, ypos);
        this.id = id;
        this.lives = lives;
        maxLives = lives;
        direction = Direction.RIGHT;
        keyState.put("UP", false);
        keyState.put("DOWN", false);
        keyState.put("LEFT", false);
        keyState.put("RIGHT", false);

        width = 0;
        height = 0;
        img = null;
        bullets = new ArrayList<>();
        rocket = new ArrayList<>();
        rocketExplosion = new ArrayList<>();
        mines = new ArrayList<>();
    }

    public abstract Map<String, Integer> getKeys();

    public void onKeyPressed(String keyName) {
        if (keyState.get(keyName))
            return;
        keyState.put(keyName, true);
        if (!directionPreference.contains(keyName)) {
            directionPreference.add(keyName);
        }
    }

    public void onKeyReleased(String keyName) {
        if (!keyState.get(keyName))
            return;
        keyState.put(keyName, false);
        if (directionPreference.contains(keyName)) {
            directionPreference.remove(keyName);
        }
    }

    public Pair calculateDirection() {
        int dx = 0;
        int dy = 0;
        if (directionPreference.size() > 0) {
            String dir = directionPreference.get(directionPreference.size() - 1);
            if (dir == "RIGHT") {
                dx = 1;
                direction = Direction.RIGHT;
            } else if (dir == "LEFT") {
                dx = -1;
                direction = Direction.LEFT;
            } else if (dir == "UP") {
                dy = -1;
                direction = Direction.UP;
            } else if (dir == "DOWN") {
                dy = 1;
                direction = Direction.DOWN;
            }
        }
        return new Pair(dx, dy);
    }

    public void move(int dx, int dy) {
        if (!disablePlayerMovement) {
            xpos += dx;
            ypos += dy;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLives() {
        return lives;
    }

    public Direction getDirection() {
        return direction;
    }

    public void incrementLives() {
        lives++;
    }

    public void loadSprite(String imgName) {
        ImageIcon img_player = new ImageIcon(imgName);
        img = img_player.getImage();
        width = img.getWidth(null);
        height = img.getHeight(null);
    }

    public void shoot() {
        if (reload == false && !disablePlayerShooting) {
            reload();
            if (direction == Direction.UP) {
                bullets.add(new Bullet(xpos, ypos - height / 2));
            } else if (direction == Direction.DOWN) {
                bullets.add(new Bullet(xpos, ypos + height / 2));
            } else if (direction == Direction.LEFT) {
                bullets.add(new Bullet(xpos - width / 2, ypos));
            } else if (direction == Direction.RIGHT) {
                bullets.add(new Bullet(xpos + width / 2, ypos));
            }

            // Shooting sound effect
            String filePath = "";
            if (bulletSound == BulletSound.RIFLE)
                filePath = "Sound/shootSound.wav";
            else if (bulletSound == BulletSound.SNIPER)
                filePath = "Sound/sniper.wav";

            if (bulletSound != BulletSound.MACHINEGUN) {
                try {
                    SoundEffect soundEffect = new SoundEffect(filePath);
                    soundEffect.play();
                } catch (Exception ex) {
                    System.out.println("Soundtrack not found");
                    ex.printStackTrace();
                }
            }
        }
    }

    public void reload() {
        reload = true;
        reloadTimer = new Timer(reloadFreq, new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                reload = false;
                ((Timer) e.getSource()).stop();
            }
        });
        reloadTimer.start();
    }

    public void playSoundEffect() {
        if (lives >= 1)
            try {
                SoundEffect soundEffect = new SoundEffect("Sound/decrementHP.wav");
                soundEffect.play();
            } catch (Exception ex) {
                System.out.println("Soundtrack not found");
                ex.printStackTrace();
            }
    }

    public int getBulletSpeed() {
        return bulletSpeed;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public ArrayList<Rocket> getRocket() {
        return rocket;
    }

    public ArrayList<Mine> getMines() {
        return mines;
    }

    public ArrayList<RocketExplosion> getExplosion() {
        return rocketExplosion;
    }
}