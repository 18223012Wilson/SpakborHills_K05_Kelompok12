package Entitas;
import java.util.Map;

import ITEMS.Item;
import LocalCalendar.*;

public class Proposing extends Action{
    public static int time;
    private Player player;
    private GameCalendar calendar;
    private NPC npc;
    public Proposing(Player player, GameCalendar calendar, NPC npc){
        super("Proposing", "Melamar NPC untuk menjadi fiance", "Proposal Ring");
        this.player = player;
        this.calendar = calendar;
        this.npc = npc;
    }

    public void execute(){
        // kondisi di rumah NPC
        System.out.println("Melakukan aksi: " + name);

        Boolean found = false;
        for (Map.Entry<Item, Integer> entry : player.getInventory().getItems().entrySet()) {
            Item item = entry.getKey();
            if(getItemNeeded().equals(item.getName())){
                found = true;
            }
        }
        if(found == false){
            System.out.println("Kamu tidak memiliki " + getItemNeeded() + "!");
            return;
        }

        int energy = player.getEnergy();
        if((energy-20) < -20){
            System.out.println("Energi tidak cukup");
            return;
        }
        
        if(npc.getHeartPoints() == npc.getMaxHeartPoints()){
            player.setEnergy(energy-10);
            player.setPartner(npc);
            npc.setRelationshipStatus("fiance");
            Proposing.time = calendar.getDay();
            //+10mnt atau percakapan
        }else if(npc.getHeartPoints() < npc.getMaxHeartPoints()){
            player.setEnergy(energy-20);
            //+10mnt atau percakapan
        }
    }
}