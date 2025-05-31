package model.actions;

import model.entitas.Player;
import model.calendar.GameCalendar;
import main.Main;
import model.items.Point;
import model.map.StoreMap;

public class VisitStoreAction extends Action {
    private Player player;
    private GameCalendar calendar;
    private Main mainFrame;

    public VisitStoreAction(Player player, GameCalendar calendar, Main mainFrame) {
        super("Visit Store", "Travel to Emily's Store.", null);
        this.player = player;
        this.calendar = calendar;
        this.mainFrame = mainFrame;
    }

    @Override
    public String execute() {
        int energyCost = getEnergyCost();
        if (player.getEnergy() - energyCost < -20 && player.getEnergy() > -20) {
            return "Too tired to visit the store. Will become exhausted.";
        }

        player.decreaseEnergy(energyCost);
        calendar.addTime(getTimeCost());
        player.setLocation("Store");
        mainFrame.switchToStoreMap();

        Point storeEntryPoint = new Point(StoreMap.MAP_SIZE / 2, StoreMap.MAP_SIZE - 1);
        if (mainFrame.getStoreMap().getTileType(storeEntryPoint.getY(), storeEntryPoint.getX()) == 'W') {
            storeEntryPoint.setY(storeEntryPoint.getY() -1 );
        }
        player.moveTo(storeEntryPoint);


        return "You have arrived at Emily's Store. Energy: " + player.getEnergy();
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