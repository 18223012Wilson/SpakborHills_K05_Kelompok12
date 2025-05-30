package model.actions;

import model.entitas.NPC;
import model.entitas.Player;
import model.calendar.GameCalendar;
import ui.GiftingDialog;
import main.Main;

public class InitiateGiftingUIAction extends Action {
    private Player player;
    private GameCalendar calendar;
    private NPC npcTarget;
    private Main mainFrame;
    private ui.GameScreenPanel gameScreenPanel;


    public InitiateGiftingUIAction(Player player, GameCalendar calendar, NPC npcTarget, Main mainFrame, ui.GameScreenPanel gameScreenPanel) {
        super("Give Gift to " + npcTarget.getName(), "Open menu to give a gift to " + npcTarget.getName(), null);
        this.player = player;
        this.calendar = calendar;
        this.npcTarget = npcTarget;
        this.mainFrame = mainFrame;
        this.gameScreenPanel = gameScreenPanel;
    }

    @Override
    public String execute() {
        calendar.pauseTime();
        GiftingDialog giftingDialog = new GiftingDialog(mainFrame, player, calendar, npcTarget, gameScreenPanel);
        giftingDialog.setVisible(true);
        return "Opened gifting menu for " + npcTarget.getName() + ". Game time paused.";
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