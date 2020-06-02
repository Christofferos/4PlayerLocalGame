import javax.swing.*;
import java.awt.event.*;

public class SetKeyBindings {
    public SetKeyBindings(InputMap im, ActionMap am, PlayerMovement playerMovement) {
        /* Player 1 */
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "down-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "down-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "up-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "up-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "left-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "left-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "right-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "right-released");

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "s-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "s-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "w-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "w-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "a-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "a-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "d-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "d-released");

        /* Player 2 */
        am.put("down-pressed", playerMovement.returnNewY(playerMovement.movement1, 1));
        am.put("down-released", playerMovement.returnNewY(playerMovement.movement1, 0));
        am.put("up-pressed", playerMovement.returnNewY(playerMovement.movement1, -1));
        am.put("up-released", playerMovement.returnNewY(playerMovement.movement1, 0));
        am.put("left-pressed", playerMovement.returnNewX(playerMovement.movement1, -1));
        am.put("left-released", playerMovement.returnNewX(playerMovement.movement1, 0));
        am.put("right-pressed", playerMovement.returnNewX(playerMovement.movement1, 1));
        am.put("right-released", playerMovement.returnNewX(playerMovement.movement1, 0));

        am.put("s-pressed", playerMovement.returnNewY(playerMovement.movement2, 1));
        am.put("s-released", playerMovement.returnNewY(playerMovement.movement2, 0));
        am.put("w-pressed", playerMovement.returnNewY(playerMovement.movement2, -1));
        am.put("w-released", playerMovement.returnNewY(playerMovement.movement2, 0));
        am.put("a-pressed", playerMovement.returnNewX(playerMovement.movement2, -1));
        am.put("a-released", playerMovement.returnNewX(playerMovement.movement2, 0));
        am.put("d-pressed", playerMovement.returnNewX(playerMovement.movement2, 1));
        am.put("d-released", playerMovement.returnNewX(playerMovement.movement2, 0));

        /* Player 3 */
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD5, 0, false), "numpad5-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD5, 0, true), "numpad5-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD8, 0, false), "numpad8-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD8, 0, true), "numpad8-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4, 0, false), "numpad4-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4, 0, true), "numpad4-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD6, 0, false), "numpad6-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD6, 0, true), "numpad6-released");

        am.put("numpad5-pressed", playerMovement.returnNewY(playerMovement.movement3, 1));
        am.put("numpad5-released", playerMovement.returnNewY(playerMovement.movement3, 0));
        am.put("numpad8-pressed", playerMovement.returnNewY(playerMovement.movement3, -1));
        am.put("numpad8-released", playerMovement.returnNewY(playerMovement.movement3, 0));
        am.put("numpad4-pressed", playerMovement.returnNewX(playerMovement.movement3, -1));
        am.put("numpad4-released", playerMovement.returnNewX(playerMovement.movement3, 0));
        am.put("numpad6-pressed", playerMovement.returnNewX(playerMovement.movement3, 1));
        am.put("numpad6-released", playerMovement.returnNewX(playerMovement.movement3, 0));

        /* Player 4 */
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_J, 0, false), "j-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_J, 0, true), "j-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_U, 0, false), "u-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_U, 0, true), "u-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, 0, false), "h-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, 0, true), "h-released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_K, 0, false), "k-pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_K, 0, true), "k-released");

        am.put("j-pressed", playerMovement.returnNewY(playerMovement.movement4, 1));
        am.put("j-released", playerMovement.returnNewY(playerMovement.movement4, 0));
        am.put("u-pressed", playerMovement.returnNewY(playerMovement.movement4, -1));
        am.put("u-released", playerMovement.returnNewY(playerMovement.movement4, 0));
        am.put("h-pressed", playerMovement.returnNewX(playerMovement.movement4, -1));
        am.put("h-released", playerMovement.returnNewX(playerMovement.movement4, 0));
        am.put("k-pressed", playerMovement.returnNewX(playerMovement.movement4, 1));
        am.put("k-released", playerMovement.returnNewX(playerMovement.movement4, 0));

    }
}