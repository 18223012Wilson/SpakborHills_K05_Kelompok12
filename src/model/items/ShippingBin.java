package model.items;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public class ShippingBin {
    public static final int MAX_UNIQUE_ITEMS = 16;

    private Map<Item, Integer> stagedItems;
    private Map<Item, Integer> itemsConfirmedForSale;
    private int goldToBeCollectedNextDay;

    public ShippingBin() {
        this.stagedItems = new HashMap<>();
        this.itemsConfirmedForSale = new HashMap<>();
        this.goldToBeCollectedNextDay = 0;
    }

    public boolean stageItem(Item item, int quantity) {
        if (item == null || quantity <= 0) {
            return false;
        }
        stagedItems.put(item, stagedItems.getOrDefault(item, 0) + quantity);
        return true;
    }

    public void unstageItem(Item item, int quantity) {
        if (item == null || quantity <= 0 || !stagedItems.containsKey(item)) {
            return;
        }
        int currentQuantity = stagedItems.get(item);
        if (quantity >= currentQuantity) {
            stagedItems.remove(item);
        }
        else {
            stagedItems.put(item, currentQuantity - quantity);
        }
    }

    public Map<Item, Integer> getStagedItems() {
        return Collections.unmodifiableMap(stagedItems);
    }

    public void clearStagedItems() {
        stagedItems.clear();
    }

    public boolean confirmStagedItemsForSale() {
        if (itemsConfirmedForSale.size() + stagedItems.keySet().stream().filter(item -> !itemsConfirmedForSale.containsKey(item)).count() > MAX_UNIQUE_ITEMS) {
            return false;
        }
        for (Map.Entry<Item, Integer> entry : stagedItems.entrySet()) {
            addItemToConfirmedSales(entry.getKey(), entry.getValue());
        }
        stagedItems.clear();
        return true;
    }

    public Map<Item, Integer> getItemsConfirmedForSale() {
        return Collections.unmodifiableMap(new HashMap<>(itemsConfirmedForSale));
    }

    public void addItemToConfirmedSales(Item item, int quantity) {
        if (item == null || quantity <= 0) return;
        this.itemsConfirmedForSale.put(item, this.itemsConfirmedForSale.getOrDefault(item, 0) + quantity);
    }

    private int calculateSaleValue() {
        int totalGold = 0;
        for (Map.Entry<Item, Integer> entry : itemsConfirmedForSale.entrySet()) {
            totalGold += entry.getKey().getSellPrice() * entry.getValue();
        }
        return totalGold;
    }

    public void processEndOfDaySales() {
        this.goldToBeCollectedNextDay = calculateSaleValue();
        this.itemsConfirmedForSale.clear();
    }

    public int collectPendingGold() {
        int gold = this.goldToBeCollectedNextDay;
        this.goldToBeCollectedNextDay = 0;
        return gold;
    }

    public int getPotentialGoldFromStagedItems() {
        int totalGold = 0;
        for (Map.Entry<Item, Integer> entry : stagedItems.entrySet()) {
            totalGold += entry.getKey().getSellPrice() * entry.getValue();
        }
        return totalGold;
    }
}