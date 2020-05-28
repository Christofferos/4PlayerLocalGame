import java.io.Serializable;

import javax.swing.*;

public class PlayerMovement implements Serializable {
    private static final long serialVersionUID = 1L;
    Movement movement1;
    Movement movement2;
    Movement movement3;
    Movement movement4;

    public PlayerMovement() {
        movement1 = new Movement();
        movement2 = new Movement();
        movement3 = new Movement();
        movement4 = new Movement();
    }

    public YDirAction returnNewY(Movement movement, int d) {
        return new YDirAction(movement, d);
    }

    public XDirAction returnNewX(Movement movement, int d) {
        return new XDirAction(movement, d);
    }

    public class Movement implements Serializable {
        private static final long serialVersionUID = 1L;
        public int xStep;
        public int yStep;
    }

    public abstract class AbstractDirectionAction extends AbstractAction {
        private static final long serialVersionUID = 1L;
        private final Movement movement;
        private final int value;

        public AbstractDirectionAction(Movement movement, int value) {
            this.movement = movement;
            this.value = value;
        }

        public Movement getMovement() {
            return movement;
        }

        public int getValue() {
            return value;
        }
    }

    public class YDirAction extends AbstractDirectionAction {
        private static final long serialVersionUID = 1L;

        public YDirAction(Movement movement, int value) {
            super(movement, value);
        }

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            getMovement().yStep = getValue();
        }
    }

    public class XDirAction extends AbstractDirectionAction {
        private static final long serialVersionUID = 1L;

        public XDirAction(Movement movement, int value) {
            super(movement, value);
        }

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            getMovement().xStep = getValue();
        }
    }
}