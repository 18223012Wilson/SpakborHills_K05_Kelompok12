package model.items;

import model.calendar.Season;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {
    private Map<Item, Integer> items;

    public Inventory() {
        this.items = new HashMap<>();
        initializeDefaultInventory();
    }

    private void initializeDefaultInventory() {
        // Default tools
        addItem(new Equipment("Hoe"), 1);
        addItem(new Equipment("Watering Can"), 1);
        addItem(new Equipment("Pickaxe"), 1);
        addItem(new Equipment("Fishing Rod"), 1);

        // Default seeds
        addItem(new Seed("Parsnip Seeds", 20, List.of(Season.SPRING), 1, "Parsnip", true), 5);
//        addItem(new Seed("Wheat Seeds", 60, List.of(Season.SPRING, Season.FALL), 1, "Wheat", true), 5);
//        addItem(new Seed("Cauliflower Seeds", 80, List.of(Season.SPRING), 5, "Cauliflower", true), 3);
//        addItem(new Seed("Potato Seeds", 50, List.of(Season.SPRING), 3, "Potato", true), 5);
//        addItem(new Seed("Grape Seeds", 60, List.of(Season.FALL), 3, "Grape", true), 5);
//        addItem(new Seed("Pumpkin Seeds", 150, List.of(Season.FALL), 7, "Pumpkin", true), 3);


//        // Items for Cooking Testing
//        addItem(Crop.fromString("Wheat"), 10);
//        addItem(Crop.fromString("Potato"), 5);
//        addItem(Crop.fromString("Grape"), 10);
//        addItem(Crop.fromString("Parsnip"), 3);
//        addItem(Crop.fromString("Cauliflower"), 3);
//        addItem(Crop.fromString("Tomato"), 3);
//        addItem(Crop.fromString("Pumpkin"), 3);
//
//        addItem(new Fish("Bullhead", Fish.FishType.COMMON, List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER), List.of(new Fish.TimeRange(0,23)), List.of(model.calendar.Weather.SUNNY, model.calendar.Weather.RAINY), List.of("Pond")), 5);
//        addItem(new Fish("Carp", Fish.FishType.COMMON, List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER), List.of(new Fish.TimeRange(0,23)), List.of(model.calendar.Weather.SUNNY, model.calendar.Weather.RAINY), List.of("Pond")), 5);
//        addItem(new Fish("Salmon", Fish.FishType.REGULAR, List.of(Season.FALL), List.of(new Fish.TimeRange(6, 18)), List.of(model.calendar.Weather.SUNNY, model.calendar.Weather.RAINY), List.of("Forest River")), 3);


        if (Misc.findMiscItemByName("Firewood") != null) {
            addItem(Misc.findMiscItemByName("Firewood"), 10);
        }
        else {
            addItem(new Misc("Firewood", 200, 100, true), 10);
        }
        if (Misc.findMiscItemByName("Coal") != null) {
            addItem(Misc.findMiscItemByName("Coal"), 10);
        }
        else {
            addItem(new Misc("Coal", 100, 50, true), 10);
        }

        if (Misc.findMiscItemByName("Egg") != null) {
            addItem(Misc.findMiscItemByName("Egg"), 5);
        }
        else {
            addItem(new Misc("Egg", 50, 25, true), 5);
        }
        addItem(Misc.findMiscItemByName("Red Bull Energy Drink"), 99);
    }

    public void addItem(Item item, int quantity) {
        if (item == null || quantity <= 0) return;
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            Item existingItem = entry.getKey();
            if (existingItem.getName().equals(item.getName()) && existingItem.getClass().equals(item.getClass())) {
                items.put(existingItem, entry.getValue() + quantity);
                return;
            }
        }
        items.put(item, quantity);
    }

    public int getItemCountByName(String itemName) {
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            if (entry.getKey().getName().equalsIgnoreCase(itemName)) {
                return entry.getValue();
            }
        }
        return 0;
    }

    public void removeItemByName(String itemName, int quantity) {
        Item itemToRemoveKey = null;
        for (Item item : items.keySet()) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                itemToRemoveKey = item;
                break;
            }
        }

        if (itemToRemoveKey != null) {
            int currentQuantity = items.get(itemToRemoveKey);
            if (quantity >= currentQuantity) {
                items.remove(itemToRemoveKey);
            } else {
                items.put(itemToRemoveKey, currentQuantity - quantity);
            }
        }
    }

    public Item getItemByName(String itemName) {
        for (Item item : items.keySet()) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    public <T extends Item> List<T> getItemsByCategory(String category, Class<T> type) {
        List<T> categoryItems = new ArrayList<>();
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            Item item = entry.getKey();
            if (item.getCategory().equalsIgnoreCase(category) && type.isInstance(item)) {
                if (entry.getValue() > 0) {
                    categoryItems.add(type.cast(item)); // Add the item itself
                }
            }
        }
        return categoryItems;
    }


    public void getItemList() {
        if (items.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            StringBuilder sb = new StringBuilder("Inventory Contents:\n");
            for (Map.Entry<Item, Integer> entry : items.entrySet()) {
                Item item = entry.getKey();
                int quantity = entry.getValue();
                sb.append(item.getName() + " (" + item.getCategory() + ") - Quantity: " + quantity + "\n");
            }
            System.out.println(sb.toString());
        }
    }

    public int getItemCountByCategoryOrName(String nameOrCategory) {
        int count = 0;
        if (nameOrCategory.equalsIgnoreCase("Any Fish")) {
            for (Map.Entry<Item, Integer> entry : items.entrySet()) {
                if (entry.getKey() instanceof Fish) {
                    count += entry.getValue();
                }
            }
            return count;
        }

        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            Item item = entry.getKey();
            if (item.getName().equalsIgnoreCase(nameOrCategory) || item.getCategory().equalsIgnoreCase(nameOrCategory)) {
                count += entry.getValue();
            }
        }
        return count;
    }

    public void removeItemsByCategoryOrName(String nameOrCategory, int quantity) {
        if (quantity <=0) return;

        if (nameOrCategory.equalsIgnoreCase("Any Fish")) {
            int qtyToRemove = quantity;
            List<Item> fishToRemove = new ArrayList<>();
            for (Item item : new ArrayList<>(items.keySet())) {
                if (qtyToRemove <= 0) break;
                if (item instanceof Fish) {
                    int available = items.get(item);
                    int toRemoveFromStack = Math.min(qtyToRemove, available);

                    if (available - toRemoveFromStack <= 0) {
                        fishToRemove.add(item);
                    } else {
                        items.put(item, available - toRemoveFromStack);
                    }
                    qtyToRemove -= toRemoveFromStack;
                }
            }
            for(Item fish : fishToRemove) items.remove(fish);
            return;
        }

        // For specific item name or other categories
        List<Item> itemsToModify = new ArrayList<>(items.keySet());
        for (Item item : itemsToModify) {
            if (quantity <= 0) break;
            if (item.getName().equalsIgnoreCase(nameOrCategory) || item.getCategory().equalsIgnoreCase(nameOrCategory)) {
                int available = items.get(item);
                int toRemove = Math.min(quantity, available);
                removeItemByName(item.getName(), toRemove);
                quantity -= toRemove;
            }
        }
    }

    public Map<Item, Integer> getAllItemsWithQuantities() {
        return Collections.unmodifiableMap(new HashMap<>(items));
    }
}