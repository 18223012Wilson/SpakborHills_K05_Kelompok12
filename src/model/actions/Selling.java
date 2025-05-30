package model.actions;

import model.entitas.Player;
import model.calendar.GameCalendar;
import main.Main;

public class Selling extends Action {
    private Player player;
    private GameCalendar calendar;
    private Main mainFrame;

    public Selling(Player player, GameCalendar calendar, Main mainFrame) {
        super("Sell", "Sell items via Shipping Bin.", "");
        this.player = player;
        this.calendar = calendar;
        this.mainFrame = mainFrame;
    }

    @Override
    public String execute() {
        calendar.pauseTime();
        mainFrame.showShippingBinPanel();
        return "Opened Shipping Bin. Time is paused.";
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