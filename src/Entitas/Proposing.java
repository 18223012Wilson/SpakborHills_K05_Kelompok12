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
        // cek apakah di rumah NPC
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

            System.out.println(player.getName() + " : Hai " + npc.getName() + ", maukah kamu menjadi tunanganku?");
                try {
                    Thread.sleep(1000); 
                } catch (InterruptedException e) {
                    e.printStackTrace(); 
                }
                System.out.println(npc.getName() + " : Ya, aku mau! Aku nggak pernah sebahagia ini.");
                try {
                    Thread.sleep(1000); 
                } catch (InterruptedException e) {
                    e.printStackTrace(); 
                }
                System.out.println("Kalian menghabiskan beberapa waktu dalam suasana berbahagia.");
                System.out.println(npc.getName() + " sekarang menjadi fiance mu.");
        }else if(npc.getHeartPoints() < npc.getMaxHeartPoints()){
            player.setEnergy(energy-20);
            System.out.println(player.getName() + " : Hai " + npc.getName() + ", maukah kamu menjadi tunanganku?");
                try {
                    Thread.sleep(1000); 
                } catch (InterruptedException e) {
                    e.printStackTrace(); 
                }
                System.out.println(npc.getName() + " : Maaf, kamu orang yang baik, namun aku belum siap untuk itu.");
                try {
                    Thread.sleep(1000); 
                } catch (InterruptedException e) {
                    e.printStackTrace(); 
                }
                System.out.println("Kamu bersedih untuk beberapa saat.");
        }
    }
}