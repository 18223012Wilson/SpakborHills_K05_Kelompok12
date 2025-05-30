package model.actions;

import model.entitas.Player;
import model.calendar.GameCalendar;
import main.Main;
import model.items.Point;
import model.map.FarmMap;

public class LeaveStoreAction extends Action {
    private Player player;
    private GameCalendar calendar;
    private Main mainFrame;

    public LeaveStoreAction(Player player, GameCalendar calendar, Main mainFrame) {
        super("Leave Store", "Return to your farm from the store.", null);
        this.player = player;
        this.calendar = calendar;
        this.mainFrame = mainFrame;
    }

    @Override
    public String execute() {
        player.setLocation("Farm");
        mainFrame.switchToFarmMap();

        Point farmEntryPoint = new Point(mainFrame.getFarmMap().getHouseCol() + FarmMap.HOUSE_WIDTH / 2, mainFrame.getFarmMap().getHouseRow() + FarmMap.HOUSE_HEIGHT);
        if (!(farmEntryPoint.getY() < FarmMap.MAP_SIZE && farmEntryPoint.getX() < FarmMap.MAP_SIZE &&
                mainFrame.getFarmMap().getTileType(farmEntryPoint.getY(), farmEntryPoint.getX()) == '.')) {
            farmEntryPoint = new Point(0, model.map.FarmMap.MAP_SIZE / 2);
        }
        player.moveTo(farmEntryPoint);

        return "You have left the store and returned to your farm.";
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