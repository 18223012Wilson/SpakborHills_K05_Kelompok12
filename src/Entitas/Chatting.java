package Entitas;
import LocalCalendar.*;

public class Chatting extends Action{
    private Player player;
    private GameCalendar calendar;
    private NPC npc;
    public Chatting(Player player, GameCalendar calendar, NPC npc){
        super("Chatting", "Berbicara dengan NPC", null);
        this.player = player;
        this.calendar = calendar;
        this.npc = npc;
    }

    public void execute(){
        // kondisi di rumah NPC
        System.out.println("Melakukan aksi: " + name);
        int energy = player.getEnergy();
        if((energy-5) < -20){
            System.out.println("Energi tidak cukup!");
            return;
        }
        player.setEnergy(energy-5);
        npc.setHeartPoints(npc.getHeartPoints() + 10);
        //+10mnt atau percakapan
    }
}