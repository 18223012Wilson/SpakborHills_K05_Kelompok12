package ITEMS;

import java.util.ArrayList;
import java.util.List;

public class Equipment extends Item {
    public Equipment(String name) {
        super(name, 0, 0, false); // not buyable, sellable, or giftable
    }

    @Override
    public String getCategory() {
        return "Equipment";
    }

    private static final List<Equipment> defaultEquipments = new ArrayList<>();
    static {
        defaultEquipments.add(new Equipment("Hoe"));
        defaultEquipments.add(new Equipment("Watering Can"));
        defaultEquipments.add(new Equipment("Pickaxe"));
        defaultEquipments.add(new Equipment("Fishing Rod"));
    }

    public static List<Equipment> getDefaultEquipments() {
        return new ArrayList<>(defaultEquipments);
    }
}
