package model.actions;

import model.entitas.Player;
import model.calendar.GameCalendar;
import main.Main;
import model.items.Point;
import model.map.MountainLakeMap;

public class VisitMountainLakeAction extends Action {
    private Player player;
    private GameCalendar calendar;
    private Main mainFrame;

    public VisitMountainLakeAction(Player player, GameCalendar calendar, Main mainFrame) {
        super("Visit Mountain Lake", "Travel to the Mountain Lake.", null);
        this.player = player;
        this.calendar = calendar;
        this.mainFrame = mainFrame;
    }

    @Override
    public String execute() {
        int energyCost = getEnergyCost();
        if (player.getEnergy() - energyCost < -20 && player.getEnergy() > -20) {
            return "Too tired to visit the Mountain Lake. Will become exhausted.";
        }
        if (player.getEnergy() < energyCost && energyCost > 0) {
            return "Not enough energy to visit the Mountain Lake. Need: " + energyCost + ", Have: " + player.getEnergy();
        }

        player.decreaseEnergy(energyCost);
        calendar.addTime(getTimeCost());
        player.setLocation("Mountain Lake");
        mainFrame.switchToMountainLakeMap();

        Point entryPoint = new Point(MountainLakeMap.MAP_SIZE / 2, MountainLakeMap.MAP_SIZE - 1);
        if (mainFrame.getMountainLakeMap().getTileType(entryPoint.getY(), entryPoint.getX()) != '.') {
            boolean found = false;
            for(int i = -1; i <= 1; i++){
                for(int j = -1; j <=1; j++){
                    if(i==0 && j==0) continue;
                    Point newEntry = new Point(entryPoint.getX()+j, entryPoint.getY()+i);
                    if(newEntry.getX() >=0 && newEntry.getX() < MountainLakeMap.MAP_SIZE &&
                            newEntry.getY() >=0 && newEntry.getY() < MountainLakeMap.MAP_SIZE &&
                            mainFrame.getMountainLakeMap().getTileType(newEntry.getY(), newEntry.getX()) == '.'){
                        entryPoint = newEntry;
                        found = true;
                        break;
                    }
                }
                if(found) break;
            }
            if(!found) entryPoint = new Point(1,1);
        }
        player.moveTo(entryPoint);


        return "You have arrived at the Mountain Lake. Energy: " + player.getEnergy();
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