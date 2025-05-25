package Entitas;
import LocalCalendar.*;

public class Chatting extends Action{
    private Player player;
    private GameCalendar calendar;
    private NPC npc;
    public static int frequency;
    
    public Chatting(Player player, GameCalendar calendar, NPC npc){
        super("Chatting", "Berbicara dengan NPC", null);
        this.player = player;
        this.calendar = calendar;
        this.npc = npc;
    }

    public void execute(){
        // cek apakah di rumah NPC
        System.out.println("Melakukan aksi: " + name);
        int energy = player.getEnergy();
        if((energy-5) < -20){
            System.out.println("Energi tidak cukup!");
            return;
        }
        
        System.out.println(player.getName() + " : Hai " + npc.getName() + ", boleh ngobrol sebentar?");
        try {
            Thread.sleep(1000); 
        } catch (InterruptedException e) {
            e.printStackTrace(); 
        }
        System.out.println(npc.getName() + " : Tentu saja boleh!");
        try {
            Thread.sleep(1000); 
        } catch (InterruptedException e) {
            e.printStackTrace(); 
        }
        System.out.println("Kamu dan " + npc.getName() + " berbincang selama beberapa waktu.");
        System.out.println(npc.getName() + " terlihat senang.");

        player.setEnergy(energy-5);
        npc.setHeartPoints(npc.getHeartPoints() + 10);
        frequency++;
    }
}