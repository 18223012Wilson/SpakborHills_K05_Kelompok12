package model.actions;

import model.entitas.Player;
import model.calendar.GameCalendar;

public class Sleeping extends Action {
    private Player player;
    private GameCalendar calendar;

    public Sleeping(Player player, GameCalendar calendar) {
        super("Sleep", "Melewati waktu sampai pagi dan mengisi ulang energi.", null);
        this.player = player;
        this.calendar = calendar;
    }

    @Override
    public String execute() {
        calendar.performSleepAndAdvanceDay();
        return player.getName() + " decided to sleep. A new day ("+ calendar.getDay() + ") begins!";
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