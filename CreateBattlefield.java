import java.io.Serializable;
import java.util.ArrayList;

public class CreateBattlefield implements Serializable {
    private static final long serialVersionUID = 1L;
    ArrayList<Obstacle> obstacles;
    private int width;
    private int height;
    private int xOffset;
    private int yOffset;
    private int size;

    public CreateBattlefield(ArrayList<Obstacle> obstacles, int width, int height, int xOffset, int yOffset) {
        this.obstacles = obstacles;
        this.width = width;
        this.height = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        size = 8;
    }

    public void createBorder() {
        Obstacle obj = new Obstacle(-10, -10, false, size);
        for (int i = 0; i < width - xOffset; i += obj.getWidth()) {
            obstacles.add(new Obstacle(i, 0, false, size)); // Upper horisontal edge
            obstacles.add(new Obstacle(i, height - yOffset, false, size)); // Lower horisontal edge
        }
        for (int i = 0; i < height - yOffset; i += obj.getHeight()) {
            obstacles.add(new Obstacle(0, i, false, size)); // Left vertical edge
            obstacles.add(new Obstacle(width - xOffset, i, false, size)); // Right vertical edge
        }
    }

    public void createBattlefield() {
        obstacles.add(new Obstacle(8, 8, true, size));
        obstacles.add(new Obstacle(16, 8, true, size));
        obstacles.add(new Obstacle(8, 16, true, size));
        obstacles.add(new Obstacle(width - xOffset - 8, height - yOffset - 8, true, size));
        obstacles.add(new Obstacle(width - xOffset - 16, height - yOffset - 8, true, size));
        obstacles.add(new Obstacle(width - xOffset - 8, height - yOffset - 16, true, size));

        // Undre väggen
        for (int i = 32; i < width / 2 - xOffset + 16; i += 8) {
            obstacles.add(new Obstacle(i, height - 32 - yOffset, false, size));
            obstacles.add(new Obstacle(i + 8 * 12, height - 32 - yOffset, false, size));
        }
        obstacles.add(new Obstacle((width - xOffset) / 4, height - yOffset - 8, true, size));

        obstacles.add(new Obstacle(width / 2 - xOffset + 16 + 8 * 12 - 4, height - 40 - yOffset, false, size));
        obstacles.add(new Obstacle(width / 2 - xOffset + 16 + 8 * 12 - 4, height - 48 - yOffset, false, size));

        obstacles.add(new Obstacle(width / 2 - xOffset + 22, height - 32 - yOffset, true, size));

        // Allra vänstra väggen
        for (int i = 48; i < height / 2 - yOffset; i += 8) {
            obstacles.add(new Obstacle(32, i, false, size));
            obstacles.add(new Obstacle(32, i + height / 2 - yOffset - 16, false, size));
        }
        obstacles.add(new Obstacle(8, 3 * (height - yOffset) / 4 - 32, true, size));

        // Näst vänstra väggen
        obstacles.add(new Obstacle(72, 32, false, size));
        obstacles.add(new Obstacle(80, 32, false, size));

        obstacles.add(new Obstacle(64, 32, false, size));
        obstacles.add(new Obstacle(64, 44, true, size));
        obstacles.add(new Obstacle(64, 56, false, size));

        obstacles.add(new Obstacle(40, 48, false, size));
        for (int i = 64; i < height / 2 - yOffset + 16; i += 8) {
            obstacles.add(new Obstacle(64, i, false, size));
        }
        obstacles.add(new Obstacle(72, (height - yOffset) / 2 - 24, true, size));
        obstacles.add(new Obstacle(80, (height - yOffset) / 2 - 24, true, size));

        // Lodrätt vägg som sticker ut från övre kanten av spelplanen
        obstacles.add(new Obstacle(2 * (width - xOffset) / 3, 8, false, size));
        obstacles.add(new Obstacle(2 * (width - xOffset) / 3, 16, false, size));
        obstacles.add(new Obstacle(2 * (width - xOffset) / 3 + 8, 17, true, size));
        obstacles.add(new Obstacle(2 * (width - xOffset) / 3 + 16, 17, true, size));

        // Väggen närmast högra kanten
        for (int i = 48; i < height / 2 - yOffset; i += 8) {
            obstacles.add(new Obstacle(width - xOffset - 32, i, false, size));
            obstacles.add(new Obstacle(width - xOffset - 32, i + height / 2 - yOffset - 16, false, size));
        }

        obstacles.add(new Obstacle(width - xOffset - 45, height / 2 - yOffset + 5, true, size));

        // Väggen näst närmast högra kanten
        for (int i = 120; i < height / 2 - yOffset + 64; i += 8) {
            obstacles.add(new Obstacle(width - xOffset - 70, i, false, size));
        }
        obstacles.add(new Obstacle(width - xOffset - 78, (height - yOffset) / 2 + 16, true, size));
        obstacles.add(new Obstacle(width - xOffset - 86, (height - yOffset) / 2 + 16, true, size));

        obstacles.add(new Obstacle(width - xOffset - 16, 48, true, size));

        obstacles.add(new Obstacle(96, 16, true, size));
        obstacles.add(new Obstacle(48, 132, true, size));

        obstacles.add(new Obstacle(width - xOffset - 62, height - yOffset - 64, true, size));
        obstacles.add(new Obstacle(width - xOffset - 58, 110, true, size));
    }
}