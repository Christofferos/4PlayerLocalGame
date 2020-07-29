import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;

public class LightningStorm {
    private static final long serialVersionUID = 1L;

    ArrayList<LightningBlock> lightningBlocks;
    int width;
    int height;
    int xOffset;
    int yOffset;
    int xCenter;
    int yCenter;
    int lightningCount;
    Timer removeLightningTimer;

    enum LightningDir {
        UP, DOWN, LEFT, RIGHT
    }

    public LightningStorm(ArrayList<LightningBlock> lightningBlocks, int width, int height, int xOffset, int yOffset) {
        this.lightningBlocks = lightningBlocks;
        this.width = width;
        this.height = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        xCenter = (width - xOffset) / 2;
        yCenter = (height - yOffset) / 2;
        lightningCount = 1;

        /* Too much noise when playing constantly.
        try {
            SoundEffect soundEffect = new SoundEffect("Sound/thunder1.wav");
            soundEffect.play();
        } catch (Exception ex) {
            System.out.println("Soundtrack not found");
            ex.printStackTrace();
        }
        */
    }

    /* ## lightningRectangle: Practical when spawning lightning rectangles ## */
    public void lightningRectangle(int x1, int y1, int x2, int y2) {
        for (int i = x1; i < x2; i += 24) {
            for (int j = y1; j < y2; j += 24) {
                lightningBlocks.add(new LightningBlock(i, j));
            }
        }
    }

    /* ## removeLightningAtInterval: clears all lightnings from the screen [Called from startStorm] ## */
    public void removeLightningAtInterval(int frequency) {
        removeLightningTimer = new Timer(frequency, new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                lightningBlocks.clear();
                ((Timer) e.getSource()).stop();
            }

        });
        removeLightningTimer.start();
    }

    /* ## startStorm: Starts the lightning environment on the playing field [Three levels] ## */
    public void startStorm() {
        Random rand = new Random();
        double max = 1;
        double min = 0.1;

        double doubleRand = 80 * Math.sqrt(rand.nextDouble() * (max - min) + min);
        double theta = rand.nextDouble() * 2 * Math.PI;

        int xPlacement = (int) (xCenter + doubleRand * Math.cos(theta));
        int yPlacement = (int) (yCenter + doubleRand * Math.sin(theta));

        removeLightningAtInterval(500); // 750
        lightningCount++;

        // Spawnes one complete lightning spark
        if (lightningCount <= 18) {
            lightningRectangle(xPlacement, yPlacement, xPlacement + 24, yPlacement + 72);
            lightningRectangle(xPlacement, yPlacement, xPlacement + 72, yPlacement + 24);
            lightningRectangle(xPlacement + 48, yPlacement - 48, xPlacement + 72, yPlacement);
        }

        if (lightningCount >= 8 && lightningCount <= 18) {
            Timer spreadOutLightning = new Timer(150, new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    double doubleRand = 240 * Math.sqrt(rand.nextDouble() * (max - min) + min);
                    double theta = rand.nextDouble() * 2 * Math.PI;
                    int xPlacement = (int) (xCenter + doubleRand * Math.cos(theta));
                    int yPlacement = (int) (yCenter + doubleRand * Math.sin(theta));
                    lightningRectangle(xPlacement, yPlacement, xPlacement + 72, yPlacement + 24);
                    lightningRectangle(xPlacement + 24, yPlacement - 24, xPlacement + 48, yPlacement + 48);
                    ((Timer) e.getSource()).stop();
                }
            });
            spreadOutLightning.start();
        }

        if (lightningCount >= 14 && lightningCount <= 18) {
            double newMax = 2;
            double newMin = 0.7;
            Timer spreadOutLightning = new Timer(200, new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    double doubleRand = 240 * Math.sqrt(rand.nextDouble() * (newMax - newMin) + newMin);
                    double theta = rand.nextDouble() * 2 * Math.PI;
                    int xPlacement = (int) (xCenter + doubleRand * Math.cos(theta));
                    int yPlacement = (int) (yCenter + doubleRand * Math.sin(theta));
                    lightningRectangle(xPlacement, yPlacement, xPlacement + 72, yPlacement + 24);
                    lightningRectangle(xPlacement + 48, yPlacement - 24, xPlacement + 72, yPlacement + 24);
                    lightningRectangle(xPlacement, yPlacement + 24, xPlacement + 24, yPlacement + 48);
                    ((Timer) e.getSource()).stop();
                }
            });
            spreadOutLightning.start();
        }

        if (lightningCount % 2 == 0 && lightningCount >= 18) {
            double newMin = 0.3;
            doubleRand = 270 * Math.sqrt(rand.nextDouble() * (max - newMin) + min);
            theta = rand.nextDouble() * 2 * Math.PI;
            xPlacement = (int) (xCenter + doubleRand * Math.cos(theta));
            yPlacement = (int) (yCenter + doubleRand * Math.sin(theta));

            lightningRectangle(xPlacement, yPlacement, xPlacement + 24, yPlacement + 24);
            hugeLightningSpark(xPlacement, yPlacement, rand);
        }
    }

    /* ## hugeLightningSpark: Spawns 4 lightnings 'arms' [Called from startStorm] ## */
    public void hugeLightningSpark(int xPlacement, int yPlacement, Random rand) {
        int frequencyIsNotUsed = 1000;
        Timer delayLightningSpark = new Timer(frequencyIsNotUsed, new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {

                // Tree with initial direction >>> UP
                Timer spreadOutLightning = new Timer(10, new ActionListener() {

                    LightningDir currentDir = LightningDir.UP;
                    int treeNodeUpX = xPlacement;
                    int treeNodeUpY = yPlacement + 24;

                    LightningDir currentDir2 = LightningDir.DOWN;
                    int treeNodeDownX = xPlacement;
                    int treeNodeDownY = yPlacement;

                    LightningDir currentDir3 = LightningDir.LEFT;
                    int treeNodeLeftX = xPlacement - 24;
                    int treeNodeLeftY = yPlacement;

                    LightningDir currentDir4 = LightningDir.RIGHT;
                    int treeNodeRightX = xPlacement + 24;
                    int treeNodeRightY = yPlacement;

                    int i = 0;

                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent e) {

                        lightningRectangle(treeNodeUpX, treeNodeUpY, treeNodeUpX + 24, treeNodeUpY + 24);
                        double dir = rand.nextDouble();
                        // DOWN
                        if (dir < 0.05 && currentDir != LightningDir.UP && treeNodeUpY < height - 48 - yOffset) {
                            treeNodeUpY += 24;
                            currentDir = LightningDir.DOWN;
                        }
                        // LEFT
                        else if (dir >= 0.05 && dir < 0.30 && currentDir != LightningDir.RIGHT && treeNodeUpX > 48) {
                            treeNodeUpX -= 24;
                            currentDir = LightningDir.LEFT;
                        }
                        // UP
                        else if (dir >= 0.30 && dir < 0.75 && currentDir != LightningDir.DOWN && treeNodeUpY > 48) {
                            treeNodeUpY -= 24;
                            currentDir = LightningDir.UP;
                        }
                        // RIGHT
                        else if (dir >= 0.75 && dir < 1.0 && currentDir != LightningDir.LEFT
                                && treeNodeUpX < width - 48 - xOffset) {
                            treeNodeUpX += 24;
                            currentDir = LightningDir.RIGHT;
                        }

                        // Tree with initial direction >>> DOWN
                        lightningRectangle(treeNodeDownX, treeNodeDownY, treeNodeDownX + 24, treeNodeDownY + 24);
                        dir = rand.nextDouble();
                        // DOWN
                        if (dir < 0.45 && currentDir2 != LightningDir.UP && treeNodeDownY < height - 48 - yOffset) {
                            treeNodeDownY += 24;
                            currentDir2 = LightningDir.DOWN;
                        }
                        // LEFT
                        else if (dir >= 0.45 && dir < 0.70 && currentDir2 != LightningDir.RIGHT && treeNodeDownX > 48) {
                            treeNodeDownX -= 24;
                            currentDir2 = LightningDir.LEFT;
                        }
                        // UP
                        else if (dir >= 0.70 && dir < 0.75 && currentDir2 != LightningDir.DOWN && treeNodeDownY > 48) {
                            treeNodeDownY -= 24;
                            currentDir2 = LightningDir.UP;
                        }
                        // RIGHT
                        else if (dir >= 0.75 && dir < 1.0 && currentDir2 != LightningDir.LEFT
                                && treeNodeDownX < width - 48 - xOffset) {
                            treeNodeDownX += 24;
                            currentDir2 = LightningDir.RIGHT;
                        }

                        // Tree with initial direction >>> LEFT
                        lightningRectangle(treeNodeLeftX, treeNodeLeftY, treeNodeLeftX + 24, treeNodeLeftY + 24);
                        dir = rand.nextDouble();
                        // DOWN
                        if (dir < 0.20 && currentDir3 != LightningDir.UP && treeNodeLeftY < height - 48 - yOffset) {
                            treeNodeLeftY += 24;
                            currentDir3 = LightningDir.DOWN;
                        }
                        // LEFT
                        else if (dir >= 0.20 && dir < 0.75 && currentDir3 != LightningDir.RIGHT && treeNodeLeftX > 48) {
                            treeNodeLeftX -= 24;
                            currentDir3 = LightningDir.LEFT;
                        }
                        // UP
                        else if (dir >= 0.75 && dir < 0.95 && currentDir3 != LightningDir.DOWN && treeNodeLeftY > 48) {
                            treeNodeLeftY -= 24;
                            currentDir3 = LightningDir.UP;
                        }
                        // RIGHT
                        else if (dir >= 0.95 && dir < 1.0 && currentDir3 != LightningDir.LEFT
                                && treeNodeLeftX < width - 48 - xOffset) {
                            treeNodeLeftX += 24;
                            currentDir3 = LightningDir.RIGHT;
                        }

                        // Tree with initial direction >>> RIGHT
                        lightningRectangle(treeNodeRightX, treeNodeRightY, treeNodeRightX + 24, treeNodeRightY + 24);
                        dir = rand.nextDouble();
                        // DOWN
                        if (dir < 0.20 && currentDir4 != LightningDir.UP && treeNodeRightY < height - 48 - yOffset) {
                            treeNodeRightY += 24;
                            currentDir4 = LightningDir.DOWN;
                        }
                        // LEFT
                        else if (dir >= 0.20 && dir < 0.25 && currentDir4 != LightningDir.RIGHT
                                && treeNodeRightX > 48) {
                            treeNodeRightX -= 24;
                            currentDir4 = LightningDir.LEFT;
                        }
                        // UP
                        else if (dir >= 0.25 && dir < 0.45 && currentDir4 != LightningDir.DOWN && treeNodeRightY > 48) {
                            treeNodeRightY -= 24;
                            currentDir4 = LightningDir.UP;
                        }
                        // RIGHT
                        else if (dir >= 0.45 && dir < 1.0 && currentDir4 != LightningDir.LEFT
                                && treeNodeRightX < width - 48 - xOffset) {
                            treeNodeRightX += 24;
                            currentDir4 = LightningDir.RIGHT;
                        }

                        i++;
                        if (i == 10) // 20
                            ((Timer) e.getSource()).stop();
                    }
                });
                spreadOutLightning.start();

            }
        });
        delayLightningSpark.setInitialDelay(1000);
        delayLightningSpark.setRepeats(false);
        delayLightningSpark.start();
    }
}
