package model.actions;

import model.entitas.Player;
import model.calendar.GameCalendar;
import ui.CookingDialog;
import main.Main;

public class CookingAction extends Action {
    private Player player;
    private GameCalendar calendar;
    private Main mainFrame;

    public CookingAction(Player player, GameCalendar calendar, Main mainFrame) {
        super("Cook", "Memasak makanan di dapur.", null);
        this.player = player;
        this.calendar = calendar;
        this.mainFrame = mainFrame;
    }

    @Override
    public String execute() {
        if (!player.getLocation().equalsIgnoreCase("House") && !player.getLocation().equalsIgnoreCase("House Interior")) {
            return "You can only cook inside your house!";
        }
        int initialEnergyCost = 10;
        if (player.getEnergy() < initialEnergyCost && player.getEnergy() > -20 && (player.getEnergy() - initialEnergyCost < -20)) {
            return "Not enough energy to start cooking! Will go below -20.";
        }
        if (player.getEnergy() <= -20 && initialEnergyCost > 0) {
            return "Exhausted! Not enough energy to cook.";
        }
        if (player.getEnergy() < initialEnergyCost ) {
            return "Not enough energy to start cooking! Need " + initialEnergyCost + ", have " + player.getEnergy();
        }

        calendar.pauseTime();
        CookingDialog cookingDialog = new CookingDialog(mainFrame, player, calendar, mainFrame.getGameScreenPanel());
        cookingDialog.setVisible(true);

        return "Opened the cooking menu. Game time paused.";
    }

    @Override
    public int getEnergyCost() {
        return 0;
    }

    @Override
    public int getTimeCost() {
        return 0;
    }
}