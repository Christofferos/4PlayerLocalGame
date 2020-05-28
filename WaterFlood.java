import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.Rectangle;

public class WaterFlood implements ActionListener {
    private static final long serialVersionUID = 1L;
    ArrayList<WaterBlock> waterBlocks;
    int width;
    int height;
    int xOffset;
    int yOffset;
    int waterCount = 1;
    int increaseCount = 1;
    Timer tsunamiTimer;
    int directionTsunami;
    int tsunamiPosX = 0;
    int tsunamiPosY = 0;
    int xNoDieZone;
    int yNoDieZone;
    int xTsunamiDir;
    int yTsunamiDir;
    boolean returnTsunami;
    Timer waitUnitlNextTsunami;
    Timer holdWaterLevel;

    public WaterFlood(ArrayList<WaterBlock> waterBlocks, int width, int height, int xOffset, int yOffset) {
        this.waterBlocks = waterBlocks;
        this.width = width;
        this.height = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public void waterRectangle(int x1, int y1, int x2, int y2) {
        for (int i = x1; i < x2; i += 8) {
            for (int j = y1; j < y2; j += 8) {
                waterBlocks.add(new WaterBlock(i, j));
            }
        }
    }

    // Top left corner to bottom right cornor.
    public void removeWaterRectangle(int x1, int x2, int y1, int y2) {
        Rectangle rec = new Rectangle(x1, y1, Math.abs(x1 - x2), Math.abs(y1 - y2));
        for (int i = 0; i < waterBlocks.size();) {
            if (rec.intersects(waterBlocks.get(i).getBoundary()))
                waterBlocks.remove(i);
            else
                i++;
        }
    }

    public void increaseWater() {
        waterCount++;
        int xCenter = (width - xOffset) / 2;
        int yCenter = (height - yOffset) / 2;
        if (waterCount <= 6) {
            if (waterCount == 2) {
                waterRectangle(xCenter - 16, yCenter - 16, xCenter + 16, yCenter + 16);
            } else if (waterCount == 3) {
                waterBlocks.clear();
                waterRectangle(xCenter - 32, yCenter - 32, xCenter + 32, yCenter + 32);
            } else if (waterCount == 4) {
                waterRectangle(8, yCenter - 8, xCenter * 2 - 8, yCenter + 8);
                waterRectangle(xCenter - 8, 8, xCenter + 8, yCenter * 2 - 8);
            } else {
                double max = 1;
                double min = 0.7;
                double r = 80;
                for (int k = 7; k > 0; k--) {
                    Random rand = new Random();
                    double doubleRand = r * Math.sqrt(rand.nextDouble() * (max - min) + min);
                    double theta = rand.nextDouble() * 2 * Math.PI;
                    int xPlacement = (int) (xCenter + doubleRand * Math.cos(theta));
                    int yPlacement = (int) (yCenter + doubleRand * Math.sin(theta));

                    if (waterCount == 5) {
                        max = 1.4;
                        min = 0.9;
                        waterRectangle(xPlacement, yPlacement, xPlacement + 16, yPlacement + 16);
                    } else if (waterCount == 6) {
                        waterRectangle(xPlacement, yPlacement, xPlacement + 24, yPlacement + 24);
                        r = 100;
                        min = 1.1;
                    }
                }
            }
        }
        if (waterCount == 7) {
            Random random = new Random();
            int maxI = 4;
            int minI = 1;
            directionTsunami = random.nextInt((maxI - minI) + 1) + minI;
            tsunami(directionTsunami);
            startTimer();
        }
    }

    public void tsunami(int directionTsunami) {
        switch (directionTsunami) {
            // Move wave right
            case 1:
                tsunamiPosX = 0;
                xNoDieZone = width - xOffset;
                yNoDieZone = 0;
                tsunamiPosY = height;
                xTsunamiDir = 8;
                break;
            // Move wave left
            case 2:
                tsunamiPosX = width - xOffset;
                xNoDieZone = 0;
                yNoDieZone = 0;
                tsunamiPosY = height;
                xTsunamiDir = -8;
                break;
            // Move wave down
            case 3:
                tsunamiPosY = 0;
                yNoDieZone = height - yOffset;
                xNoDieZone = 0;
                tsunamiPosX = width;
                yTsunamiDir = 8;
                break;
            // Move wave up
            case 4:
                tsunamiPosY = height - yOffset;
                yNoDieZone = 0;
                xNoDieZone = 0;
                tsunamiPosX = width;
                yTsunamiDir = -8;
                break;
        }
    }

    public void moveTsunami() {
        waterBlocks.clear();
        waterRectangle(0, 0, (width - xOffset), (height - yOffset));

        if (directionTsunami <= 2) {
            tsunamiPosX += xTsunamiDir;

            if (directionTsunami == 1) {
                removeWaterRectangle(tsunamiPosX, xNoDieZone, yNoDieZone, tsunamiPosY);
                if (Math.abs(xNoDieZone - tsunamiPosX) < 20) {
                    xTsunamiDir = 0;

                    holdWaterLevel = new Timer(5000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            xTsunamiDir = -8;
                            returnTsunami = true;
                            ((Timer) e.getSource()).stop();
                        }
                    });
                    holdWaterLevel.start();
                }
            } else if (directionTsunami == 2) {
                removeWaterRectangle(xNoDieZone, tsunamiPosX, yNoDieZone, tsunamiPosY);
                if (Math.abs(xNoDieZone - tsunamiPosX) < 20) {
                    xTsunamiDir = 0;

                    holdWaterLevel = new Timer(5000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            xTsunamiDir = 8;
                            returnTsunami = true;
                            ((Timer) e.getSource()).stop();
                        }
                    });
                    holdWaterLevel.start();
                }
            }

        } else if (directionTsunami > 2) {
            tsunamiPosY += yTsunamiDir;
            if (directionTsunami == 3) {
                removeWaterRectangle(xNoDieZone, tsunamiPosX, tsunamiPosY, yNoDieZone);
                if (Math.abs(yNoDieZone - tsunamiPosY) < 20) {
                    yTsunamiDir = 0;
                    holdWaterLevel = new Timer(5000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            yTsunamiDir = -8;
                            returnTsunami = true;
                            ((Timer) e.getSource()).stop();
                        }
                    });
                    holdWaterLevel.start();
                }
            } else if (directionTsunami == 4) {
                removeWaterRectangle(xNoDieZone, tsunamiPosX, yNoDieZone, tsunamiPosY);
                if (Math.abs(yNoDieZone - tsunamiPosY) < 20) {
                    yTsunamiDir = 0;
                    holdWaterLevel = new Timer(5000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            yTsunamiDir = 8;
                            returnTsunami = true;
                            ((Timer) e.getSource()).stop();
                        }
                    });
                    holdWaterLevel.start();
                }
            }
        }

        int xCenter = (width - xOffset) / 2;
        int yCenter = (height - yOffset) / 2;
        waterRectangle(xCenter - 32, yCenter - 32, xCenter + 32, yCenter + 32);
        waterRectangle(8, yCenter - 8, xCenter * 2 - 8, yCenter + 8);
        waterRectangle(xCenter - 8, 8, xCenter + 8, yCenter * 2 - 8);

        if (returnTsunami && (tsunamiPosX <= 0 || tsunamiPosX >= width - xOffset)
                && (tsunamiPosY <= 0 || tsunamiPosY >= height - yOffset)) {
            waitUnitlNextTsunami(returnTsunami);
            returnTsunami = false;
        }
    }

    public void waitUnitlNextTsunami(boolean returnTsunami) {
        if (returnTsunami) {
            waitUnitlNextTsunami = null;
            holdWaterLevel = null;

            waitUnitlNextTsunami = new Timer(5000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Random rand = new Random();
                    int max = 4;
                    int min = 1;
                    directionTsunami = rand.nextInt((max - min) + 1) + min;
                    tsunami(directionTsunami);
                    ((Timer) e.getSource()).stop();
                }
            });
            waitUnitlNextTsunami.start();
        }
    }

    public void startTimer() {
        tsunamiTimer = new Timer(250, (ActionListener) this);
        tsunamiTimer.start();
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        moveTsunami();
    }

}