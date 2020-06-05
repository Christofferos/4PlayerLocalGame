import javax.swing.*;
import java.awt.event.*;
import java.util.List;

public class SetKeyBindings {
    public abstract class KeyAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        protected final Player player;
        protected final String keyName;

        public KeyAction(Player player, String keyName) {
            this.player = player;
            this.keyName = keyName;
        }
    }

    public class KeyPressedAction extends KeyAction {
        private static final long serialVersionUID = 1L;

        public KeyPressedAction(Player player, String keyName) {
            super(player, keyName);
        }

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            player.onKeyPressed(keyName);
        }
    }

    public class KeyReleasedAction extends KeyAction {
        private static final long serialVersionUID = 1L;

        public KeyReleasedAction(Player player, String keyName) {
            super(player, keyName);
        }

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            player.onKeyReleased(keyName);
        }
    }

    public SetKeyBindings(List<Player> players, InputMap im, ActionMap am) {
        Player player1 = players.get(0);
        Player player2 = players.get(1);
        Player player3 = players.get(2);
        Player player4 = players.get(3);

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "down-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "down-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "up-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "up-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "left-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "left-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "right-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "right-released");

        am.put("down-pressed", new KeyPressedAction(player1, "DOWN"));
        am.put("down-released", new KeyReleasedAction(player1, "DOWN"));
        am.put("up-pressed", new KeyPressedAction(player1, "UP"));
        am.put("up-released", new KeyReleasedAction(player1, "UP"));
        am.put("left-pressed", new KeyPressedAction(player1, "LEFT"));
        am.put("left-released", new KeyReleasedAction(player1, "LEFT"));
        am.put("right-pressed", new KeyPressedAction(player1, "RIGHT"));
        am.put("right-released", new KeyReleasedAction(player1, "RIGHT"));

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "s-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "s-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "w-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "w-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "a-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "a-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "d-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "d-released");

        am.put("s-pressed", new KeyPressedAction(player2, "DOWN"));
        am.put("s-released", new KeyReleasedAction(player2, "DOWN"));
        am.put("w-pressed", new KeyPressedAction(player2, "UP"));
        am.put("w-released", new KeyReleasedAction(player2, "UP"));
        am.put("a-pressed", new KeyPressedAction(player2, "LEFT"));
        am.put("a-released", new KeyReleasedAction(player2, "LEFT"));
        am.put("d-pressed", new KeyPressedAction(player2, "RIGHT"));
        am.put("d-released", new KeyReleasedAction(player2, "RIGHT"));

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD5, 0, false), "numpad5-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD5, 0, true), "numpad5-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD8, 0, false), "numpad8-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD8, 0, true), "numpad8-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4, 0, false), "numpad4-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4, 0, true), "numpad4-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD6, 0, false), "numpad6-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD6, 0, true), "numpad6-released");

        am.put("numpad5-pressed", new KeyPressedAction(player3, "DOWN"));
        am.put("numpad5-released", new KeyReleasedAction(player3, "DOWN"));
        am.put("numpad8-pressed", new KeyPressedAction(player3, "UP"));
        am.put("numpad8-released", new KeyReleasedAction(player3, "UP"));
        am.put("numpad4-pressed", new KeyPressedAction(player3, "LEFT"));
        am.put("numpad4-released", new KeyReleasedAction(player3, "LEFT"));
        am.put("numpad6-pressed", new KeyPressedAction(player3, "RIGHT"));
        am.put("numpad6-released", new KeyReleasedAction(player3, "RIGHT"));

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_J, 0, false), "j-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_J, 0, true), "j-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_U, 0, false), "u-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_U, 0, true), "u-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, 0, false), "h-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, 0, true), "h-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_K, 0, false), "k-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_K, 0, true), "k-released");

        am.put("j-pressed", new KeyPressedAction(player4, "DOWN"));
        am.put("j-released", new KeyReleasedAction(player4, "DOWN"));
        am.put("u-pressed", new KeyPressedAction(player4, "UP"));
        am.put("u-released", new KeyReleasedAction(player4, "UP"));
        am.put("h-pressed", new KeyPressedAction(player4, "LEFT"));
        am.put("h-released", new KeyReleasedAction(player4, "LEFT"));
        am.put("k-pressed", new KeyPressedAction(player4, "RIGHT"));
        am.put("k-released", new KeyReleasedAction(player4, "RIGHT"));
    }
}