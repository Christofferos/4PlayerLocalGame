import javax.swing.*;
import java.util.List;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Player extends Sprite {
    private static final long serialVersionUID = 1L;

    enum Direction {
        UP {
            public String toString() {
                return "UP";
            }
        },
        DOWN {
            public String toString() {
                return "DOWN";
            }
        },
        LEFT {
            public String toString() {
                return "LEFT";
            }
        },
        RIGHT {
            public String toString() {
                return "RIGHT";
            }
        }
    }

    enum BulletSound {
        RIFLE, SNIPER
    }

    public boolean dead;
    public int id;
    public Timer reloadTimer;
    public boolean reload;
    public int reloadFreq = 250;
    public boolean pickUpOrDrop;
    public int inventoryFrequency = 400;
    public int inventoryObstacleSize = 8;
    protected int lives;
    public Direction direction;
    public int inventory;
    public int inventoryMaxCap = 3;
    public int maxLives;
    protected List<Bullet> bullets;
    public int bulletSpeed = 3;
    public BulletSound bulletSound = BulletSound.RIFLE;
    public ArrayList<Rocket> rocket;
    public ArrayList<RocketExplosion> rocketExplosion;
    public ArrayList<Mine> mines;

    public boolean disablePlayerMovement = false;
    public boolean disablePlayerShooting = false;

    public Player(int id, int lives, int xpos, int ypos) {
        super(xpos, ypos);
        this.id = id;
        this.lives = lives;
        direction = Direction.RIGHT;
        maxLives = lives;
        width = 0;
        height = 0;
        img = null;
        bullets = new ArrayList<>();
        rocket = new ArrayList<>();
        rocketExplosion = new ArrayList<>();
        mines = new ArrayList<>();
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

    public int getInventory() {
        return inventory;
    }

    public Direction getDirection() {
        return direction;
    }

    public void pickUpObstacle() {
        inventory++;
    }

    public void throwAwayObstacle() {
        inventory--;
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

    public void dirKeyPress(int xDir, int yDir) {
        if (yDir < 0) {
            changeDirection(Direction.UP);
        } else if (yDir > 0) {
            changeDirection(Direction.DOWN);
        } else if (xDir < 0) {
            changeDirection(Direction.LEFT);
        } else if (xDir > 0) {
            changeDirection(Direction.RIGHT);
        }
    }

    public void changeDirection(Direction dir) {
        if (dir == Direction.UP) {
            direction = Direction.UP;
        } else if (dir == Direction.DOWN) {
            direction = Direction.DOWN;
        } else if (dir == Direction.LEFT) {
            direction = Direction.LEFT;
        } else if (dir == Direction.RIGHT) {
            direction = Direction.RIGHT;
        }
    }

    public void move(int xStep, int yStep) {
        if (!disablePlayerMovement) {
            if (direction == Direction.UP) {
                ypos += yStep;
            } else if (direction == Direction.DOWN) {
                ypos += yStep;
            } else if (direction == Direction.RIGHT) {
                xpos += xStep;
            } else if (direction == Direction.LEFT) {
                xpos += xStep;
            }
        }
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

            try {
                SoundEffect soundEffect = new SoundEffect(filePath);
                soundEffect.play();
            } catch (Exception ex) {
                System.out.println("Soundtrack not found");
                ex.printStackTrace();
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