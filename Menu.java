import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.*;
import javax.sound.sampled.*;

public class Menu {
    public int height = 315;
    public int width = 275;
    public int windowHeight = 335;
    public int windowWidth = 275;
    JFrame frame;

    public static void main(String[] args) {
        new Menu();
    }

    public Menu() {
        try {
            SimpleAudioPlayer audioPlayer = new SimpleAudioPlayer();
            audioPlayer.play();
        } catch (Exception ex) {
            System.out.println("Soundtrack not found");
            ex.printStackTrace();
        }

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame = new JFrame("Menu");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.add(new JPanelObj());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public class SimpleAudioPlayer {
        // to store current position 
        Long currentFrame;
        Clip clip;
        // current status of clip 
        String status;
        AudioInputStream audioInputStream;
        String filePath = "Images/soundtrackOfficial.wav";

        // constructor to initialize streams and clip 
        public SimpleAudioPlayer() throws UnsupportedAudioFileException, IOException, LineUnavailableException { // create AudioInputStream object 
            audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
            // create clip reference 
            clip = AudioSystem.getClip();
            // open audioInputStream to the clip 
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }

        public void play() {
            //start the clip 
            clip.start();
            status = "play";
        }
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
            menuItems.add("2 Players");
            menuItems.add("3 Players");
            menuItems.add("4 Players");
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

            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "arrowDown");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "arrowUp");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enterDown");

            am.put("arrowDown", new MenuAction(1, ""));
            am.put("arrowUp", new MenuAction(-1, ""));
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
                    menuBounds.put(text, new Rectangle(x, y, width + 10, height + 10));
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

                if (startGameMsg.equals("startGame")) {
                    int numberOfPlayers = index + 2;
                    frame.dispose();
                    EventQueue.invokeLater(() -> {
                        App battle = new App(numberOfPlayers);
                        battle.setVisible(true);
                    });
                }

                if (index < 0) {
                    selectMenuItem = menuItems.get(0);
                }
                index += delta;
                if (index < 0) {
                    selectMenuItem = menuItems.get(menuItems.size() - 1);
                } else if (index >= menuItems.size()) {
                    selectMenuItem = menuItems.get(0);
                } else {
                    selectMenuItem = menuItems.get(index);
                }
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

            g2d.drawString("BATTLETRONICS", 55, 50);
            g2d.drawString(text, x, y);
        }

        protected void paintBackground(Graphics2D g2d, Rectangle bounds, Color background, Color foreground) {
            g2d.setColor(background);
            g2d.fill(bounds);
            g2d.setColor(foreground);
            g2d.draw(bounds);
        }

    }

}