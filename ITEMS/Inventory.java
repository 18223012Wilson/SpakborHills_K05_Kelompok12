package ITEMS;

import LocalCalendar.*;


import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private Map<Item, Integer> items;

    public Inventory() {
        this.items = new HashMap<>();
        inventori();
    }

    private void inventori() {
        addItem(new Seed("Parsnips Seeds", 20, Season.SPRING, 1, true), 15);
        addItem(new Equipment("Hoe"), 1);
        addItem(new Equipment("Watering Can"), 1);
        addItem(new Equipment("Pickaxe"), 1);
        addItem(new Equipment("Fishing Rod"), 1);
    }

    public void addItem(Item item, int quantity) {
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            Item existItem = entry.getKey();
            // kalo nama itemnya sama
            if (existItem.getName().equals(item.getName())) {
                items.put(existItem, entry.getValue() + quantity);
                return;  
            }
        }
        // kalo tidak ada item dengan nama yang sama (tambah item baru)
        items.put(item, quantity);
    }

    public void getItemList() {
        if (items.isEmpty()) {
            System.out.println("Inventory kosong.");
        } else {
            System.out.println("Isi Inventory:");
            for (Map.Entry<Item, Integer> entry : items.entrySet()) {
                Item item = entry.getKey();
                int quantity = entry.getValue();
                System.out.println(item.getName() + " - Jumlah: " + quantity);
            }
        }
    }
    
    public void removeItem(Item item, int quantity) {
        if (items.containsKey(item)) {
            int currentQuantity = items.get(item);
            if (currentQuantity >= quantity) {
                int newQuantity = currentQuantity - quantity;
                if (newQuantity > 0) {
                    items.put(item, newQuantity);
                } else {
                    items.remove(item);
            }
            } else {
                System.out.println("Jumlah item tidak cukup untuk dihapus!");
        }
        } else {
            System.out.println("Item tidak ada di inventory!");
        }
    }
}
