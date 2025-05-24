package Action;

import ITEMS.*;
import PlayernNPC.*;
import java.util.*;
import LocalCalendar.GameCalendar;

public class Cooking {
    public static boolean cook(Player player, String recipeId, Map<String, Integer> providedIngredients, String fuelType, int servings, GameCalendar calendar) {
        Recipe recipe = RecipeDatabase.getRecipeById(recipeId);
        if (recipe == null) {
            System.out.println("Resep tidak ditemukan!");
            return false;
        }

        // 1. cek lokasi
        if (!player.getLocation().equalsIgnoreCase("House")) {
            System.out.println("Kamu hanya bisa memasak di dalam rumah!");
            return false;
        }

        // 2. cek apakah resep sudah di-unlock
        if (!recipe.isUnlocked(player)) {
            System.out.println("Resep belum di-unlock!");
            return false;
        }

        // 3. cek energi
        if (player.getEnergy() < 10) {
            System.out.println("Energi tidak cukup untuk memasak!");
            return false;
        }

        // 4. cek bahan-bahan
        if (!hasRequiredIngredients(player, recipe, providedIngredients)) {
            System.out.println("Bahan tidak mencukupi.");
            return false;
        }

        // 5. cek bahan bakar
        int requiredFuel = getRequiredFuel(fuelType, servings);
        if (player.getInventory().getItemCountByCategoryOrName(fuelType) < requiredFuel) {
            System.out.println("Bahan bakar tidak cukup.");
            return false;
        }

        Food cookedFood = findFoodByName(recipe.getName());
            if (cookedFood == null) {
                System.out.println("Makanan hasil masakan tidak ditemukan!");
            return false;
        }

        // 6. proses memasak
        player.setEnergy(player.getEnergy() - 10);
        calendar.addTime(60);

        consumeIngredients(player, providedIngredients);
        player.getInventory().removeItemsByCategoryOrName(fuelType, requiredFuel);

        // buat food baru sesuai resep, jumlah sebanyak servings
        player.getInventory().addItem(cookedFood, servings);

        System.out.println("Kamu berhasil memasak " + recipe.getName() + " sebanyak " + servings + "!");
        return true;
    }

    private static boolean hasRequiredIngredients(Player player, Recipe recipe, Map<String, Integer> providedIngredients) {
        Map<String, Integer> required = recipe.getIngredients();

        for (String ingredient : required.keySet()) {
            int requiredAmount = required.get(ingredient);
            int providedAmount = providedIngredients.getOrDefault(ingredient, 0);


            int available = player.getInventory().getItemCountByCategoryOrName(ingredient);
            if (providedAmount < requiredAmount || available < requiredAmount) {
                return false;
            }
        }
        return true;
    }

    private static void consumeIngredients(Player player, Map<String, Integer> ingredients) {
        for (Map.Entry<String, Integer> entry : ingredients.entrySet()) {
            player.getInventory().removeItemsByCategoryOrName(entry.getKey(), entry.getValue());
        }
    }

    private static int getRequiredFuel(String fuelType, int servings) {
        return switch (fuelType.toLowerCase()) {
            case "firewood" -> servings;                        // 1 fuel = 1 porsi
            case "coal" -> (int) Math.ceil(servings / 2.0);     // 1 fuel = 2 porsi
            default -> Integer.MAX_VALUE;                       // fuel tidak valid
        };
    }

    private static Food findFoodByName(String name) {
    for (Food food : Food.getAllFoods()) {
        if (food.getName().equalsIgnoreCase(name)) {
            return food;
        }
    }
    return null;
}
}
