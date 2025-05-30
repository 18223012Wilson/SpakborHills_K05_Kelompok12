package model.actions;

import model.entitas.Player;
import model.calendar.GameCalendar;
import main.Main;
import model.items.Point;
import model.map.ForestRiverMap;

public class VisitForestRiverAction extends Action {
    private Player player;
    private GameCalendar calendar;
    private Main mainFrame;

    public VisitForestRiverAction(Player player, GameCalendar calendar, Main mainFrame) {
        super("Visit Forest River", "Travel to the Forest River.", null);
        this.player = player;
        this.calendar = calendar;
        this.mainFrame = mainFrame;
    }

    @Override
    public String execute() {
        int energyCost = getEnergyCost();
        if (player.getEnergy() - energyCost < -20 && player.getEnergy() > -20) {
            return "Too tired to visit the Forest River. Will become exhausted.";
        }
        if (player.getEnergy() < energyCost && energyCost > 0) {
            return "Not enough energy to visit the Forest River. Need: " + energyCost + ", Have: " + player.getEnergy();
        }

        player.decreaseEnergy(energyCost);
        calendar.addTime(getTimeCost());
        player.setLocation("Forest River");
        mainFrame.switchToForestRiverMap();

        Point entryPoint = new Point((ForestRiverMap.MAP_SIZE / 2) + 2, ForestRiverMap.MAP_SIZE - 1);
        if (mainFrame.getForestRiverMap().getTileType(entryPoint.getY(), entryPoint.getX()) == 'R') {
            entryPoint.setY(entryPoint.getY() -1 );
        }
        player.moveTo(entryPoint);

        return "You have arrived at the Forest River. Energy: " + player.getEnergy();
    }

    @Override
    public int getEnergyCost() {
        return 10;
    }

    @Override
    public int getTimeCost() {
        return 15;
    }
}