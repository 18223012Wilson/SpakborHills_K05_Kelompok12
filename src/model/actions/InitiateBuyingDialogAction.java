package model.actions;

import model.entitas.Player;
import model.entitas.Emily;
import model.calendar.GameCalendar;
import ui.BuyingDialog;
import main.Main;

public class InitiateBuyingDialogAction extends Action {
    private Player player;
    private GameCalendar calendar;
    private Emily emily;
    private Main mainFrame;
    private ui.GameScreenPanel gameScreenPanel;

    public InitiateBuyingDialogAction(Player player, GameCalendar calendar, Emily emily, Main mainFrame, ui.GameScreenPanel gameScreenPanel) {
        super("Buy Items", "Open the store menu to buy items from " + emily.getName(), null);
        this.player = player;
        this.calendar = calendar;
        this.emily = emily;
        this.mainFrame = mainFrame;
        this.gameScreenPanel = gameScreenPanel;
    }

    @Override
    public String execute() {
        calendar.pauseTime();
        BuyingDialog buyingDialog = new BuyingDialog(mainFrame, player, calendar, emily, gameScreenPanel);
        buyingDialog.setVisible(true);
        return "Opened store menu with " + emily.getName() + ". Game time paused.";
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