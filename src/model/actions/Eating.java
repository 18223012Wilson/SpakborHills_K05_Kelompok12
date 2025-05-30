package model.actions;

import model.entitas.Player;
import model.calendar.GameCalendar;
import model.items.Item;
import model.items.EdibleItem;
import model.items.Inventory;

public class Eating extends Action {
    private Player player;
    private GameCalendar calendar;
    private Item itemToEat;

    public Eating(Player player, GameCalendar calendar, Item itemToEat) {
        super("Eating " + itemToEat.getName(), "Memakan " + itemToEat.getName() + " untuk menambah energi.", itemToEat.getName());
        this.player = player;
        this.calendar = calendar;
        this.itemToEat = itemToEat;
    }

    @Override
    public String execute() {
        if (!(itemToEat instanceof EdibleItem)) {
            return itemToEat.getName() + " tidak bisa dimakan!";
        }

        EdibleItem edible = (EdibleItem) itemToEat;
        Inventory inventory = player.getInventory();

        if (inventory.getItemCountByName(itemToEat.getName()) == 0) {
            return itemToEat.getName() + " tidak tersedia di inventory!";
        }

        int energyGained = edible.getEnergyValue();
        player.setEnergy(Math.min(player.getEnergy() + energyGained, player.getMaxEnergy()));
        inventory.removeItemByName(itemToEat.getName(), 1);
        calendar.addTime(5);

        return player.getName() + " memakan " + itemToEat.getName() + ", +" + energyGained + " energi. Energi sekarang: " + player.getEnergy() + ".";
    }

    @Override
    public int getEnergyCost() {
        return 0;
    }

    @Override
    public int getTimeCost() {
        return 5;
    }
}