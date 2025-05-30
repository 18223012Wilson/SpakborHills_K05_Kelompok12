package model.items;

import java.util.ArrayList;
import java.util.List;

public class Misc extends Item {

    public Misc(String name, int buyPrice, int sellPrice, boolean giftable) {
        super(name, buyPrice, sellPrice, giftable);
    }

    @Override
    public String getCategory() {
        return "Misc";
    }

    @Override
    public String getImagePath() {
        return "/items/misc/" + getName().toLowerCase()
                .replace(" ", "_")
                .replace(":", "")
                .replace("’", "")
                .replace("'", "")
                + ".png";
    }

    private static final List<Misc> miscItems = new ArrayList<>();
    static {
        miscItems.add(new Misc("Coal", 100, 50, true));
        miscItems.add(new Misc("Firewood", 200, 100, true));
        miscItems.add(new Misc("Proposal Ring", 3000, 0, true));
        miscItems.add(new Misc("Recipe: Fish n’ Chips", 20, 0, false));
        miscItems.add(new Misc("Recipe: Fish Sandwich", 30, 0, false));
        miscItems.add(new Misc("Egg", 50, 25, true));
        // TRIBUTEEEEEE
        miscItems.add(new Misc("Red Bull Energy Drink", 50, 0, false));
    }

    public static List<Misc> getAllMiscItems() {
        return new ArrayList<>(miscItems);
    }

    public static Item findMiscItemByName(String name) {
        for (Item item : miscItems) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }

        if (name != null && name.startsWith("Recipe:")) {
            String actualRecipeName = name.substring("Recipe:".length()).trim();
            if (actualRecipeName.equalsIgnoreCase("Fish n’ Chips")) return new Misc(name, 20, 0, false);
            if (actualRecipeName.equalsIgnoreCase("Fish Sandwich")) return new Misc(name, 30, 0, false);
        }
        return null;
    }
}