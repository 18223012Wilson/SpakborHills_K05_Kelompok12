package Action;

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
    }

    // uang player masuk besoknya
}
