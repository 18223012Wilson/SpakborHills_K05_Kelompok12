package Action;

import java.util.Map;

import ITEMS.*;
import LocalCalendar.GameCalendar;
import PlayernNPC.*;

public class Selling extends Action {
    private GameCalendar calendar;
    private ShippingBin shippingBin;

    public Selling(GameCalendar calendar, ShippingBin shippingBin) {
        super("Selling", "Menjual item melalui Shipping Bin", null);
        this.calendar = calendar;
        this.shippingBin = shippingBin;
    }

    public void sellItem(Player player, Item item, int quantity) {
        if (!shippingBin.canSellToday()) {
            System.out.println("Kamu sudah melakukan penjualan hari ini.");
            return;
        }

        calendar.pauseTime(); // waktu berhenti saat jualan
        boolean success = shippingBin.addItemToBin(item, quantity, player.getInventory());
        if (success) {
            System.out.println("Item berhasil dimasukkan ke Shipping Bin.");
        }

        calendar.addTime(15); // nambah waktu 15 menit setelah jualan
        calendar.startTime(); // lanjut waktu lagi
    }

    // dipanggil saat malam ketika tidur
    public void processNightSell(Player player) {
        Map<Item, Integer> binItems = shippingBin.getBinItems();
        if (binItems.isEmpty()) {
            System.out.println("Tidak ada item untuk dijual malam ini.");
            return;
        }

        int totalGold = 0;
        System.out.println("=== Ringkasan Penjualan Hari Ini ===");
        for (Map.Entry<Item, Integer> entry : binItems.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            int price = item.getSellPrice();
            int subtotal = price * quantity;

            System.out.println("- " + item.getName() + " x" + quantity + " @ " + price + "g = " + subtotal + "g");
            totalGold += subtotal;
        }

        player.setGold(player.getGold() + totalGold);
        System.out.println("Total pendapatan hari ini: " + totalGold + "g");
        shippingBin.clearBin();         
        shippingBin.markAsSold();       
    }
}
