package ITEMS;

import java.util.Objects; // buat inventory

public abstract class Item {
    private String name;
    private int buyPrice;
    private int sellPrice;
    private boolean giftable;

    public Item(String name, int buyPrice, int sellPrice, boolean giftable) {
        if (sellPrice < 0) {
            throw new IllegalArgumentException("Item tidak dijual!");
        }
        if (buyPrice < 0) {
            throw new IllegalArgumentException("Item tidak dapat dibeli!");
        }
        this.name = name;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.giftable = giftable;
    }

    public String getName() {
        return name;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public boolean isGiftable() {
        return giftable;
    }

    // setter untuk sellPrice supaya bisa diubah secara dinamis
    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }

    public abstract String getCategory();
    // seeds/crop/fish/equipment/misc

    // buat cek inventory
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Item item = (Item) obj;
        return name.equals(item.name);
    }

    // buat cek inventory
    public int hashCode() {
        return Objects.hash(name);
    }
}
