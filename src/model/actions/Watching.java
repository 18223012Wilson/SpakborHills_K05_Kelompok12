package model.actions;

import model.entitas.Player;
import model.calendar.GameCalendar;

public class Watching extends Action {
    private Player player;
    private GameCalendar calendar;

    public Watching(Player player, GameCalendar calendar) {
        super("Watch TV", "Menonton televisi untuk melihat ramalan cuaca.", null);
        this.player = player;
        this.calendar = calendar;
    }

    @Override
    public String execute() {
        if (player.getEnergy() < getEnergyCost() && player.getEnergy() > -20 && (player.getEnergy() - getEnergyCost() < -20)) {
            return "Not enough energy to watch TV! Will go below -20.";
        }
        if (player.getEnergy() <= -20 && getEnergyCost() > 0) {
            return "Exhausted! Not enough energy to watch TV.";
        }


        player.decreaseEnergy(getEnergyCost());
        calendar.addTime(getTimeCost());

        String weatherForecast = "Today's weather: " + calendar.getWeather().toString() + ".";
        return player.getName() + " watched TV. " + weatherForecast + " Energy: " + player.getEnergy();
    }

    @Override
    public int getEnergyCost() {
        return 5;
    }

    @Override
    public int getTimeCost() {
        return 15;
    }
}