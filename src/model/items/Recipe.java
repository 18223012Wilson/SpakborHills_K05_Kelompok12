package model.items;

import java.util.Map;
import model.entitas.Player;

public class Recipe {
    private String id;
    private String name;
    private Map<String, Integer> ingredients;
    private boolean unlockedByDefault;

    public Recipe(String id, String name, Map<String, Integer> ingredients, boolean unlockedByDefault) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.unlockedByDefault = unlockedByDefault;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<String, Integer> getIngredients() {
        return ingredients;
    }

    public boolean isUnlockedByDefault() {
        return unlockedByDefault;
    }

    public boolean isUnlocked(Player player) {
        return unlockedByDefault || player.hasUnlockedRecipe(id);
    }

    public int getMaxBatch(Inventory inv) {
        int maxBatch = Integer.MAX_VALUE;
        if (ingredients == null || ingredients.isEmpty()) return 0;

        for (Map.Entry<String, Integer> entry : ingredients.entrySet()) {
            String ingredientName = entry.getKey();
            int requiredQty = entry.getValue();
            if (requiredQty <= 0) continue;

            int playerQty;
            if (ingredientName.equalsIgnoreCase("Any Fish")) {
                playerQty = 0;
                for(Map.Entry<Item, Integer> invItem : inv.getAllItemsWithQuantities().entrySet()){
                    if(invItem.getKey() instanceof Fish){
                        playerQty += invItem.getValue();
                    }
                }
            } else {
                playerQty = inv.getItemCountByCategoryOrName(ingredientName);
            }
            int batchPossible = playerQty / requiredQty;
            maxBatch = Math.min(maxBatch, batchPossible);
        }
        return maxBatch == Integer.MAX_VALUE ? 0 : maxBatch;
    }
}