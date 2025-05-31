package model.actions;

import model.entitas.Player;
import model.calendar.GameCalendar;
import main.Main;

public class Visiting extends Action {
    private Player player;
    private GameCalendar calendar;
    private Main mainFrame;
    private static int frequency = 0;

    public Visiting(Player player, GameCalendar calendar, Main mainFrame) {
        super("Visit NPCs", "Travel to the NPC area.", null);
        this.player = player;
        this.calendar = calendar;
        this.mainFrame = mainFrame;
    }

    @Override
    public String execute() {
        int energyCost = getEnergyCost();
        if (player.getEnergy() - energyCost < -20 && player.getEnergy() > -20) {
            return "Too tired to visit the NPC area. Will become exhausted.";
        }

        player.decreaseEnergy(energyCost);
        calendar.addTime(getTimeCost());
        player.setLocation("NPCMap");
        mainFrame.switchToNPCMap();
        Visiting.incrementFrequency();

        int npcMapEntryX = model.map.NPCMap.MAP_SIZE / 2;
        int npcMapEntryY = model.map.NPCMap.MAP_SIZE - 1;
        player.moveTo(new model.items.Point(npcMapEntryX, npcMapEntryY));


        return "You have traveled to the NPC area. Energy: " + player.getEnergy();
    }

    @Override
    public int getEnergyCost() {
        return 10;
    }

    @Override
    public int getTimeCost() {
        return 15;
    }

    public static void incrementFrequency() {
        frequency++;
    }

    public static int getFrequency() {
        return frequency;
    }
}