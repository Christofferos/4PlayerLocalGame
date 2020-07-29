import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.Rectangle;

public class WaterFlood {
    ArrayList<WaterBlock> waterBlocks;
    ArrayList<Boolean> stopSoundEffects;
    int width;
    int height;
    int xOffset;
    int yOffset;
    int waterCount = 1;
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

    public WaterFlood(ArrayList<WaterBlock> waterBlocks, int width, int height, int xOffset, int yOffset,
            ArrayList<Boolean> stopSoundEffects) {
        this.waterBlocks = waterBlocks;
        this.width = width;
        this.height = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.stopSoundEffects = stopSoundEffects;
    }

    public void waterRectangle(int x1, int y1, int x2, int y2) {
        for (int i = x1; i < x2; i += 24) {
            for (int j = y1; j < y2; j += 24) {
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
                waterRectangle(xCenter - 48, yCenter - 48, xCenter + 48, yCenter + 48);
            } else if (waterCount == 3) {
                waterBlocks.clear();
                waterRectangle(xCenter - 96, yCenter - 96, xCenter + 96, yCenter + 96);
            } else if (waterCount == 4) {
                waterRectangle(24, yCenter - 24, xCenter * 2 - 24, yCenter + 24);
                waterRectangle(xCenter - 24, 24, xCenter + 24, yCenter * 2 - 24);
            } else {
                double max = 1;
                double min = 0.7;
                double r = 240;
                for (int k = 7; k > 0; k--) {
                    Random rand = new Random();
                    double doubleRand = r * Math.sqrt(rand.nextDouble() * (max - min) + min);
                    double theta = rand.nextDouble() * 2 * Math.PI;
                    int xPlacement = (int) (xCenter + doubleRand * Math.cos(theta));
                    int yPlacement = (int) (yCenter + doubleRand * Math.sin(theta));

                    if (waterCount == 5) {
                        max = 1.4;
                        min = 0.9;
                        waterRectangle(xPlacement, yPlacement, xPlacement + 48, yPlacement + 48);
                    } else if (waterCount == 6) {
                        waterRectangle(xPlacement, yPlacement, xPlacement + 72, yPlacement + 72);
                        r = 300;
                        min = 1.1;
                    }
                }
            }
        }
        if (waterCount == 10) {
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
                xTsunamiDir = 24;
                break;
            // Move wave left
            case 2:
                tsunamiPosX = width - xOffset;
                xNoDieZone = 0;
                yNoDieZone = 0;
                tsunamiPosY = height;
                xTsunamiDir = -24;
                break;
            // Move wave down
            case 3:
                tsunamiPosY = 0;
                yNoDieZone = height - yOffset;
                xNoDieZone = 0;
                tsunamiPosX = width;
                yTsunamiDir = 24;
                break;
            // Move wave up
            case 4:
                tsunamiPosY = height - yOffset;
                yNoDieZone = 0;
                xNoDieZone = 0;
                tsunamiPosX = width;
                yTsunamiDir = -24;
                break;
        }

        try {
            SoundEffect soundEffect = new SoundEffect("Sound/tsunami.wav", stopSoundEffects);
            soundEffect.play();
        } catch (Exception ex) {
            System.out.println("Soundtrack not found");
            ex.printStackTrace();
        }

    }

    public void moveTsunami() {
        waterBlocks.clear();
        waterRectangle(0, 0, (width - xOffset), (height - yOffset));

        if (directionTsunami <= 2) {
            tsunamiPosX += xTsunamiDir;

            if (directionTsunami == 1) {
                removeWaterRectangle(tsunamiPosX, xNoDieZone, yNoDieZone, tsunamiPosY);
                if (Math.abs(xNoDieZone - tsunamiPosX) < 60) {
                    xTsunamiDir = 0;

                    holdWaterLevel = new Timer(5000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            xTsunamiDir = -24;
                            returnTsunami = true;
                            ((Timer) e.getSource()).stop();
                        }
                    });
                    holdWaterLevel.start();
                }
            } else if (directionTsunami == 2) {
                removeWaterRectangle(xNoDieZone, tsunamiPosX, yNoDieZone, tsunamiPosY);
                if (Math.abs(xNoDieZone - tsunamiPosX) < 60) {
                    xTsunamiDir = 0;

                    holdWaterLevel = new Timer(5000, new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            xTsunamiDir = 24;
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
                if (Math.abs(yNoDieZone - tsunamiPosY) < 60) {
                    yTsunamiDir = 0;
                    holdWaterLevel = new Timer(5000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            yTsunamiDir = -24;
                            returnTsunami = true;
                            ((Timer) e.getSource()).stop();
                        }
                    });
                    holdWaterLevel.start();
                }
            } else if (directionTsunami == 4) {
                removeWaterRectangle(xNoDieZone, tsunamiPosX, yNoDieZone, tsunamiPosY);
                if (Math.abs(yNoDieZone - tsunamiPosY) < 60) {
                    yTsunamiDir = 0;
                    holdWaterLevel = new Timer(5000, new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            yTsunamiDir = 24;
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

        waterRectangle(xCenter - 96, yCenter - 96, xCenter + 96, yCenter + 96);
        waterRectangle(24, yCenter - 24, xCenter * 2 - 24, yCenter + 24);
        waterRectangle(xCenter - 24, 24, xCenter + 24, yCenter * 2 - 24);

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
        tsunamiTimer = new Timer(250, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveTsunami();
            }
        });
        tsunamiTimer.start();
    }

}