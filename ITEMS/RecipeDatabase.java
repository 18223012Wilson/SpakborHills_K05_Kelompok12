package ITEMS;

import java.util.*;
import PlayernNPC.*;

public class RecipeDatabase {
    private static final Map<String, Recipe> recipes = new HashMap<>();

    static {
        addRecipe("recipe_1", "Fish n’ Chips", Map.of(
                "Any Fish", 2,
                "Wheat", 1,
                "Potato", 1
        ), false); // beli di store

        addRecipe("recipe_2", "Baguette", Map.of(
                "Wheat", 3
        ), true); // default

        addRecipe("recipe_3", "Sashimi", Map.of(
                "Salmon", 3
        ), false); // setelah memancing 10 ikan

        addRecipe("recipe_4", "Fugu", Map.of(
                "Pufferfish", 1
        ), false); // memancing pufferfish

        addRecipe("recipe_5", "Wine", Map.of(
                "Grape", 2
        ), true); // default

        addRecipe("recipe_6", "Pumpkin Pie", Map.of(
                "Egg", 1,
                "Wheat", 1,
                "Pumpkin", 1
        ), true); // default

        addRecipe("recipe_7", "Veggie Soup", Map.of(
                "Cauliflower", 1,
                "Parsnip", 1,
                "Potato", 1,
                "Tomato", 1
        ), false); // panen pertama

        addRecipe("recipe_8", "Fish Stew", Map.of(
                "Any Fish", 2,
                "Hot Pepper", 1,
                "Cauliflower", 2
        ), false); // unlock saat dapat Hot Pepper

        addRecipe("recipe_9", "Spakbor Salad", Map.of(
                "Melon", 1,
                "Cranberry", 1,
                "Blueberry", 1,
                "Tomato", 1
        ), true); // default

        addRecipe("recipe_10", "Fish Sandwich", Map.of(
                "Any Fish", 1,
                "Wheat", 2,
                "Tomato", 1,
                "Hot Pepper", 1
        ), false); // beli di store

        addRecipe("recipe_11", "The Legends of Spakbor", Map.of(
                "Legend Fish", 1,
                "Potato", 2,
                "Parsnip", 1,
                "Tomato", 1,
                "Eggplant", 1
        ), false); // dapat saat mancing Legend Fish
    }

    private static void addRecipe(String id, String name, Map<String, Integer> ingredients, boolean unlockedByDefault) {
        recipes.put(id, new Recipe(id, name, ingredients, unlockedByDefault));
    }

    public static Recipe getRecipeById(String id) {
        return recipes.get(id);
    }

    public static Collection<Recipe> getAllRecipes() {
        return recipes.values();
    }

    public static List<Recipe> getUnlockedRecipes(Player player) {
        List<Recipe> unlocked = new ArrayList<>();
        for (Recipe recipe : recipes.values()) {
            if (recipe.isUnlocked(player)) {
                unlocked.add(recipe);
            }
        }
        return unlocked;
    }
}
