import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuInstructions {
    public int height = 315;
    public int width = 275;
    public int windowHeight = 335;
    public int windowWidth = 275;
    JFrame frame;

    public MenuInstructions() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame = new JFrame("Instructions");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.add(new JPanelObj());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public class JPanelObj extends JPanel {
        private static final long serialVersionUID = 1L;
        private List<String> menuItems;
        private String selectMenuItem;
        private String focusedItem;
        private MenuItemPainter painter;
        private Map<String, Rectangle> menuBounds;

        public JPanelObj() {
            setBackground(Color.BLACK);

            painter = new SimpleMenuItemPainter();
            menuItems = new ArrayList<>(25);
            menuItems.add("Go Back");
            selectMenuItem = menuItems.get(0);

            MouseAdapter ma = new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    String newItem = null;
                    for (String text : menuItems) {
                        Rectangle bounds = menuBounds.get(text);
                        if (bounds.contains(e.getPoint())) {
                            newItem = text;
                            break;
                        }
                    }
                    if (newItem != null && !newItem.equals(selectMenuItem)) {
                        selectMenuItem = newItem;
                        repaint();
                    }
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    focusedItem = null;
                    for (String text : menuItems) {
                        Rectangle bounds = menuBounds.get(text);
                        if (bounds.contains(e.getPoint())) {
                            focusedItem = text;
                            repaint();
                            break;
                        }
                    }
                }

            };

            addMouseListener(ma);
            addMouseMotionListener(ma);

            InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
            ActionMap am = getActionMap();

            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enterDown");
            am.put("enterDown", new MenuAction(0, "startGame"));

        }

        @Override
        public void invalidate() {
            menuBounds = null;
            super.invalidate();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(width - 15, height - 15);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            if (menuBounds == null) {
                menuBounds = new HashMap<>(menuItems.size());
                int width = 100;
                int height = 30;
                for (String text : menuItems) {
                    Dimension dim = painter.getPreferredSize(g2d, text);
                    width = Math.max(width, dim.width);
                    height = Math.max(height, dim.height);
                }

                int x = (getWidth() - (width + 10)) / 2;

                int totalHeight = (height + 10) * menuItems.size();
                totalHeight += 5 * (menuItems.size() - 1);

                int y = (getHeight() - totalHeight) / 2;

                for (String text : menuItems) {
                    menuBounds.put(text, new Rectangle(x, y + 110, width + 10, height + 10));
                    y += height + 10 + 5;
                }

            }
            for (String text : menuItems) {
                Rectangle bounds = menuBounds.get(text);
                boolean isSelected = text.equals(selectMenuItem);
                boolean isFocused = text.equals(focusedItem);
                painter.paint(g2d, text, bounds, isSelected, isFocused);

            }
            g2d.dispose();
        }

        public class MenuAction extends AbstractAction {

            private final int delta;
            private final String startGameMsg;

            public MenuAction(int delta, String startGameMsg) {
                this.delta = delta;
                this.startGameMsg = startGameMsg;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                int index = menuItems.indexOf(selectMenuItem);

                if (startGameMsg.equals("startGame") && index == 0) {
                    frame.dispose();
                    new Menu(false);
                }
                selectMenuItem = menuItems.get(0);
                repaint();
            }

        }

    }

    public interface MenuItemPainter {
        public void paint(Graphics2D g2d, String text, Rectangle bounds, boolean isSelected, boolean isFocused);

        public Dimension getPreferredSize(Graphics2D g2d, String text);

    }

    public class SimpleMenuItemPainter implements MenuItemPainter {

        public Dimension getPreferredSize(Graphics2D g2d, String text) {
            return g2d.getFontMetrics().getStringBounds(text, g2d).getBounds().getSize();
        }

        @Override
        public void paint(Graphics2D g2d, String text, Rectangle bounds, boolean isSelected, boolean isFocused) {
            Font f = new Font("Nunito", Font.BOLD, 18);
            g2d.setFont(f);
            FontMetrics fm = g2d.getFontMetrics();
            if (isSelected) {
                paintBackground(g2d, bounds, Color.DARK_GRAY, Color.BLACK);
            } else if (isFocused) {
                paintBackground(g2d, bounds, Color.DARK_GRAY, Color.BLACK);
            } else {
                paintBackground(g2d, bounds, Color.BLACK, Color.BLACK);
            }
            int x = bounds.x + ((bounds.width - fm.stringWidth(text)) / 2);
            int y = bounds.y + ((bounds.height - fm.getHeight()) / 2) + fm.getAscent();
            g2d.setColor(isSelected ? Color.WHITE : Color.LIGHT_GRAY);

            f = new Font("Nunito", Font.BOLD, 22);
            g2d.setFont(f);
            g2d.drawString("BATTLETRONICS", 45, 50);
            g2d.drawString(text, x, y);

            f = new Font("Nunito", Font.PLAIN, 14);
            g2d.setFont(f);

            Image p1 = new ImageIcon("Images/player1.png").getImage();
            g2d.drawImage(p1, 10, 70, null);
            g2d.drawString("P1 Controls: Arrow Keys, Del, End.", 25, 80);

            Image p2 = new ImageIcon("Images/player2.png").getImage();
            g2d.drawImage(p2, 10, 90, null);
            g2d.drawString("P2 Controls: W, A, S, D, 1, Q.", 25, 100);

            Image p3 = new ImageIcon("Images/player3.png").getImage();
            g2d.drawImage(p3, 10, 110, null);
            g2d.drawString("P3 Controls: U, H, J, K, '.', '-'.", 25, 120);

            Image p4 = new ImageIcon("Images/player4.png").getImage();
            g2d.drawImage(p4, 10, 130, null);
            g2d.drawString("P4 Controls: NumKeys, Div, Mult.", 25, 140);

            f = new Font("Nunito", Font.PLAIN, 12);
            g2d.setFont(f);
            Image i1 = new ImageIcon("Images/health.png").getImage();
            g2d.drawImage(i1, 15, 160, null);
            g2d.drawString(": health", 25, 168);

            Image i2 = new ImageIcon("Images/mine.png").getImage();
            g2d.drawImage(i2, 90, 160, null);
            g2d.drawString(": mine", 105, 168);

            Image i3 = new ImageIcon("Images/minigun.png").getImage();
            g2d.drawImage(i3, 155, 160, null);
            g2d.drawString(": machineGun", 180, 168);

            // Ny rad
            Image i4 = new ImageIcon("Images/strengthBoost.png").getImage();
            g2d.drawImage(i4, 13, 177, null);
            g2d.drawString(": maxHP", 25, 188);

            Image i5 = new ImageIcon("Images/fireBoost3.png").getImage();
            g2d.drawImage(i5, 93, 180, null);
            g2d.drawString(": firerate", 105, 188);

            Image i6 = new ImageIcon("Images/movementBoost2.png").getImage();
            g2d.drawImage(i6, 160, 180, null);
            g2d.drawString(": pickUpBoost", 175, 188);

            // Ny rad
            Image i7 = new ImageIcon("Images/rocket.png").getImage();
            g2d.drawImage(i7, 15, 200, null);
            g2d.drawString(": rocket", 25, 208);

            Image i8 = new ImageIcon("Images/sniper.png").getImage();
            g2d.drawImage(i8, 90, 200, null);
            g2d.drawString(": sniper", 110, 208);

        }

        protected void paintBackground(Graphics2D g2d, Rectangle bounds, Color background, Color foreground) {
            g2d.setColor(background);
            g2d.fill(bounds);
            g2d.setColor(foreground);
            g2d.draw(bounds);
        }

    }

}