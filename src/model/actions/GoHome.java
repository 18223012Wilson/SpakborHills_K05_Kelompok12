package model.actions;

import model.entitas.Player;
import model.calendar.GameCalendar;
import main.Main;
import model.items.Point;
import model.map.FarmMap;


public class GoHome extends Action {
    private Player player;
    private GameCalendar calendar;
    private Main mainFrame;

    public GoHome(Player player, GameCalendar calendar, Main mainFrame) {
        super("Go Home", "Return to your farm.", null);
        this.player = player;
        this.calendar = calendar;
        this.mainFrame = mainFrame;
    }

    @Override
    public String execute() {
        player.setLocation("Farm");
        mainFrame.switchToFarmMap();

        Point farmEntryPont = new Point(mainFrame.getFarmMap().getHouseCol() + FarmMap.HOUSE_WIDTH / 2, mainFrame.getFarmMap().getHouseRow() + FarmMap.HOUSE_HEIGHT);
        if (!(farmEntryPont.getY() < FarmMap.MAP_SIZE && farmEntryPont.getX() < FarmMap.MAP_SIZE &&
                mainFrame.getFarmMap().getTileType(farmEntryPont.getY(), farmEntryPont.getX()) == '.')) {
            farmEntryPont = new Point(0, model.map.FarmMap.MAP_SIZE / 2);
        }
        player.moveTo(farmEntryPont);


        return "You have returned to your farm.";
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