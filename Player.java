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

    public boolean dead;
    public Timer reloadTimer;
    public boolean reload;
    public int reloadFreq = 250;
    public boolean pickUpOrDrop;
    public int inventoryFrequency = 500;
    public int inventoryObstacleSize = 8;
    protected int lives;
    public Direction direction;
    public int inventory;
    public int inventoryMaxCap = 3;
    public int maxLives;
    protected List<Bullet> bullets;

    public Player(int lives, int xpos, int ypos) {
        super(xpos, ypos);
        this.lives = lives;
        direction = Direction.RIGHT;
        maxLives = lives;
        width = 0;
        height = 0;
        img = null;
        bullets = new ArrayList<>();
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

    public String toString() {
        return "Player [ X-position: " + xpos + " Y-position: " + ypos + " Lives: " + lives + " Inventory: " + inventory
                + " ]";
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

    public void shoot() {
        if (reload == false) {
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

    public List<Bullet> getBullets() {
        return bullets;
    }

}