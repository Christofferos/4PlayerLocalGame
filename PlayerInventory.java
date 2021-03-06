import java.awt.event.ActionListener;

import javax.swing.Timer;

public class PlayerInventory {
    boolean p1InventoryAction;
    boolean p2InventoryAction;
    boolean p3InventoryAction;
    boolean p4InventoryAction;
    Timer p1InventoryTimer;
    Timer p2InventoryTimer;
    Timer p3InventoryTimer;
    Timer p4InventoryTimer;

    Player1 player1;
    Player2 player2;
    Player3 player3;
    Player4 player4;
    CollisionDetection collisionDetection;

    public PlayerInventory(Player1 player1, Player2 player2, Player3 player3, Player4 player4,
            CollisionDetection collisionDetection) {
        this.player1 = player1;
        this.player2 = player2;
        this.player3 = player3;
        this.player4 = player4;
        this.collisionDetection = collisionDetection;
    }

    public void playerInventoryAction(int player) {
        switch (player) {
            case 1:
                if (p1InventoryAction == false) {
                    if (player1.pickUpOrDrop && player1.inventory != player1.inventoryMaxCap
                            && collisionDetection.collision(1, false)) {
                        if (collisionDetection.collision(1, true)) {
                            player1.inventory++;
                            p1InventoryAction = true;
                            initializeInvetoryTimer(1);
                        }
                    } else if (player1.pickUpOrDrop && player1.inventory != 0) {
                        if (collisionDetection.spaceToDropObstacle(1)) {
                            player1.inventory--;
                            p1InventoryAction = true;
                            initializeInvetoryTimer(1);
                        }
                    }
                }
                break;
            case 2:
                if (p2InventoryAction == false) {
                    if (player2.pickUpOrDrop && player2.inventory != player2.inventoryMaxCap
                            && collisionDetection.collision(2, false)) {
                        if (collisionDetection.collision(2, true)) {
                            player2.inventory++;
                            p2InventoryAction = true;
                            initializeInvetoryTimer(2);
                        }
                    } else if (player2.pickUpOrDrop && player2.inventory != 0) {
                        if (collisionDetection.spaceToDropObstacle(2)) {
                            player2.inventory--;
                            p2InventoryAction = true;
                            initializeInvetoryTimer(2);
                        }
                    }
                }
                break;
            case 3:
                if (p3InventoryAction == false) {
                    if (player3.pickUpOrDrop && player3.inventory != player3.inventoryMaxCap
                            && collisionDetection.collision(3, false)) {
                        if (collisionDetection.collision(3, true)) {
                            player3.inventory++;
                            p3InventoryAction = true;
                            initializeInvetoryTimer(3);
                        }
                    } else if (player3.pickUpOrDrop && player3.inventory != 0) {
                        if (collisionDetection.spaceToDropObstacle(3)) {
                            player3.inventory--;
                            p3InventoryAction = true;
                            initializeInvetoryTimer(3);
                        }
                    }
                }
                break;
            case 4:
                if (p4InventoryAction == false) {
                    if (player4.pickUpOrDrop && player4.inventory != player4.inventoryMaxCap
                            && collisionDetection.collision(4, false)) {
                        if (collisionDetection.collision(4, true)) {
                            player4.inventory++;
                            p4InventoryAction = true;
                            initializeInvetoryTimer(4);
                        }
                    } else if (player4.pickUpOrDrop && player4.inventory != 0) {
                        if (collisionDetection.spaceToDropObstacle(4)) {
                            player4.inventory--;
                            p4InventoryAction = true;
                            initializeInvetoryTimer(4);
                        }
                    }
                }
                break;
        }
    }

    public void initializeInvetoryTimer(int player) {
        if (player == 1) {
            p1InventoryTimer = new Timer(player1.inventoryFrequency, new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    p1InventoryAction = false;
                    setDelay(1);
                    ((Timer) e.getSource()).stop();
                }
            });
            p1InventoryTimer.start();
        } else if (player == 2) {
            p2InventoryTimer = new Timer(player2.inventoryFrequency, new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    p2InventoryAction = false;
                    setDelay(2);
                    ((Timer) e.getSource()).stop();
                }
            });
            p2InventoryTimer.start();
        } else if (player == 3) {
            p3InventoryTimer = new Timer(player3.inventoryFrequency, new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    p3InventoryAction = false;
                    setDelay(3);
                    ((Timer) e.getSource()).stop();
                }
            });
            p3InventoryTimer.start();
        } else if (player == 4) {
            p4InventoryTimer = new Timer(player4.inventoryFrequency, new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    p4InventoryAction = false;
                    setDelay(4);
                    ((Timer) e.getSource()).stop();
                }
            });
            p4InventoryTimer.start();
        }
    }

    public void setDelay(int player) {
        if (player == 1)
            p1InventoryTimer.setDelay(player1.inventoryFrequency);
        else if (player == 2)
            p2InventoryTimer.setDelay(player2.inventoryFrequency);
        else if (player == 3)
            p3InventoryTimer.setDelay(player3.inventoryFrequency);
        else if (player == 4)
            p4InventoryTimer.setDelay(player4.inventoryFrequency);
    }
}