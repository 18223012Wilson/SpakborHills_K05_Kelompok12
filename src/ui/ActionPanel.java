package ui;

import model.actions.Action;
import model.entitas.Player;
import model.items.Point;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ActionPanel extends JPanel {
    private GameScreenPanel gameScreenPanel;
    private Player player;

    public ActionPanel(GameScreenPanel gameScreenPanel) {
        this.gameScreenPanel = gameScreenPanel;
        this.player = gameScreenPanel.getPlayer();
        setBackground(new Color(70, 70, 70));
        setPreferredSize(new Dimension(0, 80));
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setVisible(false);
    }

    public void displayActions(List<Action> actions, Point clickedTile) {
        removeAll();
        if (actions == null || actions.isEmpty()) {
            setVisible(false);
            revalidate();
            repaint();
            return;
        }

        for (Action action : actions) {
            JButton actionButton = new JButton(action.getName());
            actionButton.setFont(new Font("Arial", Font.PLAIN, 14));
            actionButton.setFocusable(false);
            actionButton.addActionListener(e -> {
                Window activeModal = null;
                Window owner = SwingUtilities.getWindowAncestor(this);
                if (owner != null) {
                    for (Window w : owner.getOwnedWindows()) {
                        if (w instanceof JDialog && ((JDialog)w).isModal() && w.isVisible() && w instanceof EndGameStatsDialog) {
                            activeModal = w;
                            break;
                        }
                    }
                }
                if (activeModal != null) {
                    gameScreenPanel.showMessage("Please close the milestone message first!");
                    return;
                }


                if (player.getEnergy() > -20 || action.getEnergyCost() <=0 ) {
                    String resultMessage = action.execute();
                    gameScreenPanel.showMessage(resultMessage);
                    if (gameScreenPanel.getMainFrame() != null) {
                        gameScreenPanel.getMainFrame().checkMilestones();
                    }
                    clearActions();
                    gameScreenPanel.requestFocusInWindow();
                    gameScreenPanel.repaint();
                } else {
                    gameScreenPanel.showMessage("Not enough energy to perform " + action.getName());
                }
            });
            add(actionButton);
        }
        setVisible(true);
        revalidate();
        repaint();
    }

    public void clearActions() {
        removeAll();
        setVisible(false);
        revalidate();
        repaint();
    }
}