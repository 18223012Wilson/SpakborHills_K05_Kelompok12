package ITEMS;

import java.util.Map;

public class Recipe {
    private String id;
    private String name; // ini juga jadi nama hasil makanan
    private Map<String, Integer> ingredients;
    private String unlockCondition;

    public Recipe(String id, String name, Map<String, Integer> ingredients, String unlockCondition) {
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

    public String getUnlockCondition() {
        return unlockCondition;
    }

    // tambahin fungsi ini di Player (buat cek apakah kondisi player sesuai dengan ketentuan recipe)
    public boolean isUnlocked(Player player) { 
        return player.hasUnlockedRecipe(this.id);
    }
}
