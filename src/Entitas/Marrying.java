package Entitas;
import java.util.Map;

import ITEMS.Item;
import LocalCalendar.*;

public class Marrying extends Action{
    private Player player;
    private GameCalendar calendar;
    private NPC npc;
    public Marrying(Player player, GameCalendar calendar, NPC npc){
        super("Marrying", "Menikahi NPC yang telah menjadi fiance", "Proposal Ring");
        this.player = player;
        this.calendar = calendar;
        this.npc = npc;
    }

    public void execute(){
        // kondisi di rumah NPC
        System.out.println("Melakukan aksi: " + name);
        int energy = player.getEnergy();
        
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

        if(npc.getRelationshipStatus().equals("fiance")){
            if((energy-80) < -20){
                System.out.println("Energi tidak cukup");
                return;
            }
            if(Proposing.time < calendar.getDay()){
                player.setEnergy(energy-80);
                calendar.timeSkip(22);
                npc.setRelationshipStatus("spouse");
                //player dikembalikan ke rumah
            }else{
                System.out.println("Terlalu cepat untuk menikah");
            }
        }
    }
}