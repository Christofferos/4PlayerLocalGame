import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import java.awt.Rectangle;
import java.awt.event.ActionListener;

public class FireRing implements ActionListener {

    ArrayList<FireBlock> fireBlocks;
    ArrayList<Boolean> stopSoundEffects;
    int width;
    int height;
    int xOffset;
    int yOffset;
    int flameCount = 0;
    int increaseCount = 1;
    Timer moveTimer;
    Timer moveTimer2;
    Timer goalTimer;

    int xGoal = 0;
    int yGoal = 0;

    int moveIteration = 1;
    int moveDirCounter = 1;

    int xCenterOfNoDieZone;
    int yCenterOfNoDieZone;
    int noDieZoneRadius = 62;
    int r = noDieZoneRadius;
    int iteration = 0;

    public FireRing(ArrayList<FireBlock> fireBlocks, int width, int height, int xOffset, int yOffset,
            ArrayList<Boolean> stopSoundEffects) {
        this.stopSoundEffects = stopSoundEffects;
        this.fireBlocks = fireBlocks;
        this.width = width;
        this.height = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    /* ## AddFireRectangle: [NOTE: x1, x2, y1, y2] ## */
    public void addFireRectangle(int x1, int x2, int y1, int y2) {
        for (int i = x1; i < x2; i += 8) {
            for (int j = y1; j < y2; j += 8) {
                fireBlocks.add(new FireBlock(i, j));
            }
        }
    }

    /* ## RemoveFireRectangle: [NOTE: x1, x2, y1, y2] ## */
    public void removeFireRectangle(int x1, int x2, int y1, int y2) {
        Rectangle rec = new Rectangle(x1, y1, Math.abs(x1 - x2), Math.abs(y1 - y2));
        for (int i = 0; i < fireBlocks.size();) {
            if (rec.intersects(fireBlocks.get(i).getBoundary()))
                fireBlocks.remove(i);
            else
                i++;
        }
    }

    /* ## IncreaseFlames: [Flames moves towards middle] ## */
    public void increaseFlames() {
        flameCount++;

        if (flameCount <= 7) {
            try {
                SoundEffect soundEffect = new SoundEffect("Sound/fireRing.wav", stopSoundEffects);
                soundEffect.play();
            } catch (Exception ex) {
                System.out.println("Soundtrack not found");
                ex.printStackTrace();
            }

            for (int i = (flameCount - 1) * 8; i < width - xOffset; i += 8) {
                fireBlocks.add(new FireBlock(i, (flameCount - 1) * 8));
                fireBlocks.add(new FireBlock(i, height - yOffset - (flameCount - 1) * 8));
            }
            for (int i = 0; i < height - yOffset; i += 8) {
                fireBlocks.add(new FireBlock((flameCount - 1) * 8, i));
                fireBlocks.add(new FireBlock(width - xOffset - (flameCount - 1) * 8, i));
            }
            if (flameCount >= 4) {
                flameCount++;
                for (int i = (flameCount - 1) * 8; i < width - xOffset; i += 8) {
                    fireBlocks.add(new FireBlock(i, (flameCount - 1) * 8));
                    fireBlocks.add(new FireBlock(i, height - yOffset - (flameCount - 1) * 8));
                }
                for (int i = 0; i < height - yOffset; i += 8) {
                    fireBlocks.add(new FireBlock((flameCount - 1) * 8, i));
                    fireBlocks.add(new FireBlock(width - xOffset - (flameCount - 1) * 8, i));
                }
            }
        }
        if (flameCount == 8) {
            xCenterOfNoDieZone = (width - xOffset) / 2;
            yCenterOfNoDieZone = (height - yOffset) / 2;

            Random rand = new Random();
            int max = 9;
            int min = 1;
            int randomNum = rand.nextInt((max - min) + 1) + min;

            availablePositions(randomNum);
            removeFireRectangle(xCenterOfNoDieZone - 45, xCenterOfNoDieZone + 45, yCenterOfNoDieZone - 45,
                    yCenterOfNoDieZone + 45);
            startTimer();
        }
    }

    /* ## StartTimer: [Move fire ring with certain update rate] ## */
    public void startTimer() {
        goalTimer = new Timer(850, (ActionListener) this);
        goalTimer.start();
    }

    /* ## ActionPerformed: [Called by StartTimer - every Tick] ## */
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        goalPosition(xGoal, yGoal);
        iteration++;
        if (iteration == 100) {
            noDieZoneRadius = 54;
            r = noDieZoneRadius;
        }
    }

    /* ## GoalPosition: [Move fire circle towards a 2D point] ## */
    public void goalPosition(int x, int y) {
        fireBlocks.clear();
        addFireRectangle(0, (width - xOffset), 0, (height - yOffset));
        if (x > xCenterOfNoDieZone && Math.abs(x - xCenterOfNoDieZone) >= 8) {
            xCenterOfNoDieZone += 8;
            if (y > yCenterOfNoDieZone && Math.abs(y - yCenterOfNoDieZone) >= 8) {
                yCenterOfNoDieZone += 8;
                removeFireRectangle(xCenterOfNoDieZone - r, xCenterOfNoDieZone + r, yCenterOfNoDieZone - r,
                        yCenterOfNoDieZone + r);
            } else if (y < yCenterOfNoDieZone && Math.abs(y - yCenterOfNoDieZone) >= 8) {
                yCenterOfNoDieZone -= 8;
                removeFireRectangle(xCenterOfNoDieZone - r, xCenterOfNoDieZone + r, yCenterOfNoDieZone - r,
                        yCenterOfNoDieZone + r);
            } else if (Math.abs(y - yCenterOfNoDieZone) < 8) {
                removeFireRectangle(xCenterOfNoDieZone - r, xCenterOfNoDieZone + r, yCenterOfNoDieZone - r,
                        yCenterOfNoDieZone + r);
            }
        } else if (x < xCenterOfNoDieZone && Math.abs(x - xCenterOfNoDieZone) >= 8) {
            xCenterOfNoDieZone -= 8;
            if (y > yCenterOfNoDieZone && Math.abs(y - yCenterOfNoDieZone) >= 8) {
                yCenterOfNoDieZone += 8;
                removeFireRectangle(xCenterOfNoDieZone - r, xCenterOfNoDieZone + r, yCenterOfNoDieZone - r,
                        yCenterOfNoDieZone + r);
            } else if (y < yCenterOfNoDieZone && Math.abs(y - yCenterOfNoDieZone) >= 8) {
                yCenterOfNoDieZone -= 8;
                removeFireRectangle(xCenterOfNoDieZone - r, xCenterOfNoDieZone + r, yCenterOfNoDieZone - r,
                        yCenterOfNoDieZone + r);
            } else if (Math.abs(y - yCenterOfNoDieZone) < 8) {
                removeFireRectangle(xCenterOfNoDieZone - r, xCenterOfNoDieZone + r, yCenterOfNoDieZone - r,
                        yCenterOfNoDieZone + r);
            }
        } else if (Math.abs(x - xCenterOfNoDieZone) < 8) {
            if (y > yCenterOfNoDieZone) {
                yCenterOfNoDieZone += 8;
                removeFireRectangle(xCenterOfNoDieZone - r, xCenterOfNoDieZone + r, yCenterOfNoDieZone - r,
                        yCenterOfNoDieZone + r);
            } else if (y < yCenterOfNoDieZone) {
                yCenterOfNoDieZone -= 8;
                removeFireRectangle(xCenterOfNoDieZone - r, xCenterOfNoDieZone + r, yCenterOfNoDieZone - r,
                        yCenterOfNoDieZone + r);
            }
        }
        if (Math.abs(xCenterOfNoDieZone - x) < 10 && Math.abs(yCenterOfNoDieZone - y) < 10) {
            goalTimer.stop();
            Random rand = new Random();
            int max = 9;
            int min = 1;
            int randomNum = rand.nextInt((max - min) + 1) + min;
            availablePositions(randomNum);
            goalTimer.start();
        }

    }

    /* ## AvailablePositions: [Define 9 positions where the fire ring can move] ## */
    public void availablePositions(int posIndex) {
        // 1 2 3
        // 4 5 6
        // 7 8 9
        switch (posIndex) {
            case 1:
                xGoal = (width - xOffset) / 4 - 8;
                yGoal = (height - yOffset) / 4;
                break;
            case 2:
                xGoal = (width - xOffset) / 2;
                yGoal = (height - yOffset) / 4;
                break;
            case 3:
                xGoal = 3 * (width - xOffset) / 4 + 16;
                yGoal = (height - yOffset) / 4;
                break;
            case 4:
                xGoal = (width - xOffset) / 4 - 8;
                yGoal = (height - yOffset) / 2;
                break;
            case 5:
                xGoal = (width - xOffset) / 2;
                yGoal = (height - yOffset) / 2;
                break;
            case 6:
                xGoal = 3 * (width - xOffset) / 4 + 16;
                yGoal = (height - yOffset) / 2;
                break;
            case 7:
                xGoal = (width - xOffset) / 4 - 8;
                yGoal = 3 * (height - yOffset) / 4 + 8;
                break;
            case 8:
                xGoal = (width - xOffset) / 2;
                yGoal = 3 * (height - yOffset) / 4 + 8;
                break;
            case 9:
                xGoal = 3 * (width - xOffset) / 4 + 16;
                yGoal = 3 * (height - yOffset) / 4 + 8;
                break;
        }
    }
}