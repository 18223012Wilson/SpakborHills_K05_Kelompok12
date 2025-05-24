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
        miscItems.add(new Misc("Coal", 50, 30, false));
        miscItems.add(new Misc("Firewood", 40, 20, false));
    }

    public static List<Misc> getAllMiscItems() {
        return new ArrayList<>(miscItems);
    }
}
