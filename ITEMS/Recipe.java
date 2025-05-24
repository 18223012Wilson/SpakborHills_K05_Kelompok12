package ITEMS;

import java.util.Map;
import PlayernNPC.*;

public class Recipe {
    private String id;
    private String name; // ini juga jadi nama hasil makanan
    private Map<String, Integer> ingredients;
    private boolean unlockCondition;

    public Recipe(String id, String name, Map<String, Integer> ingredients, boolean unlockCondition) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.unlockCondition = unlockCondition;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name; // nama resep = nama hasil masakan
    }

    public Map<String, Integer> getIngredients() {
        return ingredients;
    }

    public Boolean getUnlockCondition() {
        return unlockCondition;
    }

    // berapa banyak makanan bisa dimasak
    public int getMaxBatch(Inventory inv) {
        int maxBatch = Integer.MAX_VALUE;

        for (Map.Entry<String, Integer> entry : ingredients.entrySet()) {
            String ingredientName = entry.getKey();
            int requiredQty = entry.getValue();

            int playerQty = inv.getItemCountByCategoryOrName(ingredientName); // Ini perlu kamu buat di Inventory
            int batchPossible = playerQty / requiredQty;
            maxBatch = Math.min(maxBatch, batchPossible);
        }
        return maxBatch;
    }

    // buat cek apakah kondisi player sesuai dengan ketentuan recipe
    public boolean isUnlocked(Player player) { 
        return unlockCondition || player.hasUnlockedRecipe(id);
    }

}
