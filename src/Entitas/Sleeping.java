package Entitas;
import LocalCalendar.*;

public class Sleeping extends Action{
    private Player player;
    private GameCalendar calendar;
    public Sleeping(Player player, GameCalendar calendar){
        super("Sleeping", "Melewati waktu sampai pagi dan mengisi ulang energi", null);
        this.player = player;
        this.calendar = calendar;
    }

    public void execute(){
        System.out.println("Melakukan aksi: " + name);
        int energy = player.getEnergy();
        if(energy <= 0){
            player.setEnergy(10);
        }else if(energy < (player.getMaxEnergy()/10)){
            player.setEnergy(energy + (player.getMaxEnergy()/2));
        }else{
            player.setEnergy(player.getMaxEnergy());
        } 
        player.prosesShippingBin();
        player.newDayReset();
    }
}