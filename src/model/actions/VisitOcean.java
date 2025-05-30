package model.actions;

import model.entitas.Player;
import model.calendar.GameCalendar;
import main.Main;
import model.items.Point;
import model.map.OceanMap;

public class VisitOcean extends Action {
    private Player player;
    private GameCalendar calendar;
    private Main mainFrame;

    public VisitOcean(Player player, GameCalendar calendar, Main mainFrame) {
        super("Visit Ocean", "Travel to the Ocean.", null);
        this.player = player;
        this.calendar = calendar;
        this.mainFrame = mainFrame;
    }

    @Override
    public String execute() {
        int energyCost = getEnergyCost();
        if (player.getEnergy() - energyCost < -20 && player.getEnergy() > -20) {
            return "Too tired to visit the Ocean. Will become exhausted.";
        }
        if (player.getEnergy() < energyCost && energyCost > 0) {
            return "Not enough energy to visit the Ocean. Need: " + energyCost + ", Have: " + player.getEnergy();
        }

        player.decreaseEnergy(energyCost);
        calendar.addTime(getTimeCost());
        player.setLocation("Ocean");
        mainFrame.switchToOceanMap();

        Point entryPoint = new Point(OceanMap.MAP_SIZE / 2, 0);
        if (mainFrame.getOceanMap().getTileType(entryPoint.getY(), entryPoint.getX()) != '.') {
            entryPoint = new Point(1,1);
            if(mainFrame.getOceanMap().getTileType(entryPoint.getY(), entryPoint.getX()) != '.') {
                for(int r=0; r < 6; r++){
                    for(int c=0; c < OceanMap.MAP_SIZE; c++){
                        if(mainFrame.getOceanMap().getTileType(r,c) == '.'){
                            entryPoint = new Point(c,r);
                            break;
                        }
                    }
                    if(mainFrame.getOceanMap().getTileType(entryPoint.getY(), entryPoint.getX()) == '.') break;
                }
            }
        }
        player.moveTo(entryPoint);

        return "You have arrived at the Ocean. Energy: " + player.getEnergy();
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