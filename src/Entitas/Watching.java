package Entitas;
import LocalCalendar.*;

public class Watching extends Action{
    private Player player;
    private GameCalendar calendar;
    public Watching(Player player, GameCalendar calendar){
        super("Watching", "Menonton televisi", null);
        this.player = player;
        this.calendar = calendar;
    }

    public void execute(){
        System.out.println("Melakukan aksi: " + name);
        int energy = player.getEnergy();
        if((energy-5) < -20){
            System.out.println("Energi tidak cukup!");
            return;
        }
        System.out.println("Cuaca hari ini " + calendar.getWeather());
        player.setEnergy(energy-5);
        calendar.addTime(15);
    }
}