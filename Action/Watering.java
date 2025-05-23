package Action;

import LocalCalendar.GameCalendar;
import Playerr.Player;

public class Watering extends Action {
    private Player player;
    private GameCalendar calendar;

    public Watering(Player player, GameCalendar calendar) {
        super("Watering", "Bergerak ke soil yang telah ditanam dan melakukan watering", "Watering Can");
        if (player == null || calendar == null) {
            throw new IllegalArgumentException("Player atau GameCalendar tidak boleh null");
        }
        this.player = player;
        this.calendar = calendar;
    }

    @Override
    public void execute() {
        int currentEnergy = player.getEnergy();
        if (currentEnergy <= -20) {
            System.out.println("Energi terlalu rendah (-20 atau lebih), aksi tidak bisa dilakukan!");
            return;
        }
        int newEnergy = currentEnergy - getEnergyCost();
        if (newEnergy < -20) {
            newEnergy = -20; // Batasi energi minimal -20
        }
        player.setEnergy(newEnergy);
        calendar.addTime(getTimeCost());
    }

    @Override
    public int getEnergyCost() {
        return 5; // 5 energi per soil
    }

    @Override
    public int getTimeCost() {
        return 5; // 5 menit per soil
    }
}