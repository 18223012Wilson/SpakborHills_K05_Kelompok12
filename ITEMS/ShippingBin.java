package ITEMS;

import PlayernNPC.*;
import java.util.*;

public class ShippingBin {
    private final Map<Item, Integer> bin;
    private final int MAX_UNIQUE_ITEMS = 16;
    private boolean hasSoldToday = false;

    public ShippingBin() {
        this.bin = new HashMap<>();
    }

    public boolean addItemToBin(Item item, int quantity, Inventory inventory) {
        if (hasSoldToday) {
            System.out.println("Kamu sudah menjual hari ini. Tunggu besok.");
            return false;
        }       

        if (!isSellable(item)) {
            System.out.println(item.getName() + " tidak bisa dijual.");
            return false;
        }

        if (bin.containsKey(item) || bin.size() < MAX_UNIQUE_ITEMS) {
            int inventoryQuantity = getInventoryQty(item, inventory);
            if (inventoryQuantity >= quantity) {
                bin.put(item, bin.getOrDefault(item, 0) + quantity);
                inventory.removeItem(item, quantity);
                System.out.println(quantity + " " + item.getName() + " ditambahkan ke Shipping Bin.");
                return true;
            } else {
                System.out.println("Jumlah item di inventory tidak mencukupi!");
            }
        } else {
            System.out.println("Shipping bin sudah penuh!");
        }
        return false;
    }

    private boolean isSellable(Item item) {
        String category = item.getCategory();
        return category.equals("Seed") || category.equals("Crops") || category.equals("Fish") || category.equals("Food") || category.equals("Misc");
    }

    // show isi shipping bin
    public void showShippingBin() {
        if (bin.isEmpty()) {
            System.out.println("Shipping Bin kosong.");
        } else {
            System.out.println("Isi Shipping Bin:");
            for (Map.Entry<Item, Integer> entry : bin.entrySet()) {
                Item item = entry.getKey();
                int quantity = entry.getValue();
                System.out.println(item.getName() + " - Jumlah: " + quantity + ", Harga jual per item: " + item.getSellPrice());
            }
            System.out.println("Total nilai jual: " + getTotalSellValue());
        }
    }

    private int getInventoryQty(Item item, Inventory inventory) {
        return inventory.getItemCountByCategoryOrName(item.getName());
    }

    public int getTotalSellValue() {
        int total = 0;
        for (Map.Entry<Item, Integer> entry : bin.entrySet()) {
            total += entry.getKey().getSellPrice() * entry.getValue();
        }
        return total;
    }

    public Map<Item, Integer> getBinItems() {
        return bin;
    }

    public void clearBin() {
        bin.clear();
    }

    public boolean canSellToday() {
        return !hasSoldToday;
    }

    public void markAsSold() {
        hasSoldToday = true;
    }

    public void resetDailySell() {
        hasSoldToday = false;
    }
}
