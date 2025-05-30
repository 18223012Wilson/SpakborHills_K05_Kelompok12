package model.actions;

import model.entitas.NPC;
import model.entitas.Player;
import model.calendar.GameCalendar;

public class Chatting extends Action {
    private Player player;
    private GameCalendar calendar;
    private NPC npc;
    private static int frequency = 0;

    public Chatting(Player player, GameCalendar calendar, NPC npc) {
        super("Chat with " + npc.getName(), "Berbicara dengan " + npc.getName(), null);
        this.player = player;
        this.calendar = calendar;
        this.npc = npc;
    }

    @Override
    public String execute() {
        int energyCost = getEnergyCost();
        if (player.getEnergy() - energyCost < -20 && player.getEnergy() > -20) {
            return "Energi tidak cukup untuk berbicara dengan " + npc.getName() + ". Akan menjadi terlalu lelah.";
        }
        if (player.getEnergy() < energyCost && energyCost >0 ) {
            return "Energi tidak cukup untuk berbicara dengan " + npc.getName() + ". Butuh: " + energyCost + ", Punya: " + player.getEnergy();
        }


        StringBuilder dialogue = new StringBuilder();
        dialogue.append("You started a conversation with ").append(npc.getName()).append(".\n");
        dialogue.append(player.getName()).append(": Hi ").append(npc.getName()).append(", can we talk for a bit?\n");
        dialogue.append(npc.getName()).append(": Of course!\n");
        dialogue.append("You and ").append(npc.getName()).append(" talked for a while.\n");
        dialogue.append(npc.getName()).append(" seems happy.\n");

        player.decreaseEnergy(energyCost);
        calendar.addTime(getTimeCost());
        npc.setHeartPoints(npc.getHeartPoints() + 10);
        Chatting.incrementFrequency();

        dialogue.append(npc.getName()).append(" now has ").append(npc.getHeartPoints()).append(" heart points.\n");
        dialogue.append("Remaining energy: ").append(player.getEnergy());

        return dialogue.toString();
    }

    @Override
    public int getEnergyCost() {
        return 10;
    }

    @Override
    public int getTimeCost() {
        return 10;
    }

    public static void incrementFrequency() {
        frequency++;
    }

    public static int getFrequency() {
        return frequency;
    }
}