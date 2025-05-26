package ITEMS;

import java.util.ArrayList;
import java.util.List;

public class Misc extends Item {
    public Misc(String name, int buyPrice, int sellPrice, boolean giftable) {
        super(name, buyPrice, sellPrice, giftable);
        
        if (sellPrice >= buyPrice) {
            throw new IllegalArgumentException("Harga jual harus lebih murah dari harga beli.");
        }
    }

    public String getCategory() {
        return "Misc";
    }

    // harga' nya buat sendiri
    private static final List<Misc> miscItems = new ArrayList<>();
    static {
        miscItems.add(new Misc("Coal", 100, 50, true));
        miscItems.add(new Misc("Firewood", 200, 100, true));
        miscItems.add(new Misc("Proposal Ring", 3000, 0, true));
    }

    public static List<Misc> getAllMiscItems() {
        return new ArrayList<>(miscItems);
    }
}
