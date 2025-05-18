package Action;

import ITEMS.*;
import Player.*;
import LocalCalendar.*;

public class Eating {
    private Player player;
    private GameCalendar calendar;

    public Eating(Player player, GameCalendar calendar) {
        this.player = player;
        this.calendar = calendar;
    }

    public void eat(Item item) {
        // cek apakah item edible
        if (!(item instanceof EdibleItem)) {
            System.out.println("Item tidak bisa dimakan!");
            return;
        }

        EdibleItem edible = (EdibleItem) item;

        // cek apakah item ada di inventory
        Inventory inventory = player.getInventory();
        int quantity = inventory.getItemCountByCategoryOrName(item.getName());

        if (quantity == 0) {
            System.out.println("Item tidak tersedia di inventory!");
            return;
        }

        // tambah energi sesuai jenis item
        int energyGained = edible.getEnergyValue();
        player.setEnergy(Math.min(player.getEnergy() + energyGained, player.getMaxEnergy()));

        // kurangi item dari inventory
        inventory.removeItem(item, 1);

        // tambah waktu 5 menit -> belum ada fungsinya 
        // calendar.addTime(5);

        System.out.println(player.getName() + " memakan " + item.getName() + " dan mendapatkan " + energyGained + " energi.");
        System.out.println("Energi sekarang: " + player.getEnergy());
        System.out.println("Waktu sekarang:");
        calendar.showTime();
    }
}
