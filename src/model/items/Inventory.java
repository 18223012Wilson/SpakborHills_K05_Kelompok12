package model.items;

import model.calendar.Season;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private Map<Item, Integer> items;

    public Inventory() {
        this.items = new HashMap<>();
        initializeDefaultInventory();
    }

    private void initializeDefaultInventory() {
        addItem(new Seed("Parsnips Seeds", 20, Season.SPRING, 1, true), 15);
        addItem(new Equipment("Hoe"), 1);
        addItem(new Equipment("Watering Can"), 1);
        addItem(new Equipment("Pickaxe"), 1);
        addItem(new Equipment("Fishing Rod"), 1);
    }

    public void addItem(Item item, int quantity) {
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            Item existItem = entry.getKey();
            if (existItem.getName().equals(item.getName())) {
                items.put(existItem, entry.getValue() + quantity);
                return;
            }
        }
        items.put(item, quantity);
    }

    public void getItemList() {
        if (items.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            System.out.println("Inventory Contents:");
            for (Map.Entry<Item, Integer> entry : items.entrySet()) {
                Item item = entry.getKey();
                int quantity = entry.getValue();
                System.out.println(item.getName() + " - Quantity: " + quantity);
            }
        }
    }

    public void removeItem(Item item, int quantity) {
        Item itemToRemove = null;
        for (Item invItem : items.keySet()) {
            if (invItem.getName().equals(item.getName())) {
                itemToRemove = invItem;
                break;
            }
        }

        if (itemToRemove != null && items.containsKey(itemToRemove)) {
            int currentQuantity = items.get(itemToRemove);
            if (currentQuantity >= quantity) {
                int newQuantity = currentQuantity - quantity;
                if (newQuantity > 0) {
                    items.put(itemToRemove, newQuantity);
                } else {
                    items.remove(itemToRemove);
                }
            } else {
                System.out.println("Not enough items to remove!");
            }
        } else {
            System.out.println("Item not found in inventory!");
        }
    }

    public int getItemCountByCategoryOrName(String nameOrCategory) {
        int count = 0;
        for (Item item : items.keySet()) {
            if (item.getName().equalsIgnoreCase(nameOrCategory) || item.getCategory().equalsIgnoreCase(nameOrCategory)) {
                count += items.get(item);
            }
        }
        return count;
    }

    public void removeItemsByCategoryOrName(String nameOrCategory, int quantity) {
        for (Item item : new ArrayList<>(items.keySet())) {
            if (quantity <= 0) break;

            if (item.getName().equalsIgnoreCase(nameOrCategory) || item.getCategory().equalsIgnoreCase(nameOrCategory)) {
                int available = items.get(item);
                int toRemove = Math.min(quantity, available);
                removeItem(item, toRemove);
                quantity -= toRemove;
            }
        }
    }
}