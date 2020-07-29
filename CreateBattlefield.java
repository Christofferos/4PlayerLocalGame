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
        size = 24;
    }

    /* AddRectangleArea: [Practical way of placing multiple obstacles] */
    public void addRectangleArea(int x1, int y1, int x2, int y2, boolean movable) {
        for (int i = x1; i < x2; i += 25) {
            for (int j = y1; j < y2; j += 25) {
                obstacles.add(new Obstacle(i, j, movable, size));
            }
        }
    }

    /* CreateBorder: [4 edges around the battlefield] */
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

    /* CreateBattlefield: [Placing obstacles on battlefield. Sorry for the mess here..] */
    public void createBattlefield() {
        obstacles.add(new Obstacle(24, 24, true, size));
        obstacles.add(new Obstacle(width - xOffset - 24, height - yOffset - 24, true, size));

        obstacles.add(new Obstacle(width - xOffset - 24, 24, true, size));
        obstacles.add(new Obstacle(24, height - yOffset - 24, true, size));

        obstacles.add(new Obstacle(width - xOffset - 48, height - yOffset - 144, true, size));

        // LOWER WALL
        for (int i = 96; i < width / 2 - xOffset + 12; i += 24) {
            obstacles.add(new Obstacle(i, height - 96 - yOffset, false, size));
            obstacles.add(new Obstacle(i + 24 * 14, height - 96 - yOffset, false, size));
        }
        
        obstacles.add(new Obstacle((width - xOffset) / 4, height - yOffset - 48, true, size));

        obstacles.add(new Obstacle(width / 2 - xOffset + 38 + 24 * 12, height - 120 - yOffset, false, size));
        obstacles.add(new Obstacle(width / 2 - xOffset + 38 + 24 * 12, height - 144 - yOffset, false, size));

        obstacles.add(new Obstacle(3 * (width - xOffset) / 4 + 78, 3 * (height - yOffset) / 4 + 58, true, size));

        obstacles.add(new Obstacle(3 * (width - xOffset) / 4, height - yOffset - 48, true, size));

        // LEFTMOST WALL
        for (int i = 144; i < height / 2 - yOffset - 48; i += 24) {
            obstacles.add(new Obstacle(96, i, false, size));
            obstacles.add(new Obstacle(96, i + height / 2 - yOffset - 72, false, size));
        }
        
        obstacles.add(new Obstacle(48, 3 * (height - yOffset) / 4 -96, true, size));

        
        obstacles.add(new Obstacle(120, 36, true, size));
        obstacles.add(new Obstacle(120, 100, true, size));

        

        obstacles.add(new Obstacle(48, 144, true, size));

        
        obstacles.add(new Obstacle((width - xOffset) / 4 - 101, 3 * (height - yOffset) / 4 + 56, true, size));

        
        obstacles.add(new Obstacle((width - xOffset) / 4 - 36, 3 * (height - yOffset) / 4 + 6, true, size));
        obstacles.add(new Obstacle((width - xOffset) / 4 - 16, 3 * (height - yOffset) / 4 + 30, true, size));

        // SECOND LEFT WALL
        obstacles.add(new Obstacle(216, 96, false, size));
        obstacles.add(new Obstacle(240, 96, false, size));

        obstacles.add(new Obstacle(192, 96, false, size));
        obstacles.add(new Obstacle(192, 132, true, size));
        obstacles.add(new Obstacle(192, 168, false, size));


        obstacles.add(new Obstacle(120, 144, false, size));

        
        for (int i = 192; i < height / 2 - yOffset + 48; i += 24) {
            obstacles.add(new Obstacle(192, i, false, size));
        }

        
        obstacles.add(new Obstacle(246, (height - yOffset) / 2 - 72, true, size));
        obstacles.add(new Obstacle(246, (height - yOffset) / 2 - 96, true, size));

        // VERTICAL WALL FROM TOP EDGE
        obstacles.add(new Obstacle(2 * (width - xOffset) / 3, 24, false, size));
        obstacles.add(new Obstacle(2 * (width - xOffset) / 3, 48, false, size));
        obstacles.add(new Obstacle(2 * (width - xOffset) / 3 + 27, 51, true, size));
        obstacles.add(new Obstacle(2 * (width - xOffset) / 3 + 51, 51, true, size));

        addRectangleArea((width - xOffset) / 2 + 18, 51, (width - xOffset) / 2 + 90, 75, true);
        addRectangleArea((width - xOffset) / 2 + 240, 102, (width - xOffset) / 2 + 282, 120, true);

        obstacles.add(new Obstacle((width - xOffset) / 2 + 213, 72, true, size));

        // RIGHTMOST WALL
        for (int i = 144; i < height / 2 - yOffset - 48; i += 24) {
            obstacles.add(new Obstacle(width - xOffset - 96, i, false, size));
            obstacles.add(new Obstacle(width - xOffset - 96, i + height / 2 - yOffset - 48, false, size));
        }

        obstacles.add(new Obstacle(width - xOffset - 130, height / 2 - yOffset - 50, true, size));

        addRectangleArea(width - xOffset - 258, 3 * (height - yOffset) / 4 + 50, width - xOffset - 204,
                3 * (height - yOffset) / 4 + 68, true);

        // SECOND RIGHT WALL
        for (int i = 360; i < height / 2 - yOffset + 192; i += 24) {
            obstacles.add(new Obstacle(width - xOffset - 210, i, false, size));
        }
        
        obstacles.add(new Obstacle(width - xOffset - 264, (height - yOffset) / 2 + 48, true, size));
        obstacles.add(new Obstacle(width - xOffset - 264, (height - yOffset) / 2 + 72, true, size));

        obstacles.add(new Obstacle(width - xOffset - 48, 144, true, size));

        
        obstacles.add(new Obstacle(288, 48, true, size));
        obstacles.add(new Obstacle(144, 450, true, size));

        obstacles.add(new Obstacle(width - xOffset - 210, height - yOffset - 192, true, size));
        obstacles.add(new Obstacle(width - xOffset - 174, 330, true, size)); 
    }
}