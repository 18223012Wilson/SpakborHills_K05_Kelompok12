package model.items;

import java.util.ArrayList;
import java.util.List;

public class Food extends Item implements EdibleItem {
    private int energyValue;

    public Food(String name, int buyPrice, int sellPrice, int energyValue, boolean giftable) {
        super(name, buyPrice, sellPrice, giftable);
        if (energyValue <= 0) {
            throw new IllegalArgumentException("Energy value must be positive.");
        }
        this.energyValue = energyValue;
    }

    @Override
    public int getEnergyValue() {
        return energyValue;
    }

    @Override
    public String getCategory() {
        return "Food";
    }

    @Override
    public String getImagePath() {
        return "/items/food/" + getName().toLowerCase()
                .replace(" ", "_")
                .replace("’", "")
                .replace("'", "")
                + ".png";
    }

    public static List<Food> getAllFoods() {
        List<Food> foods = new ArrayList<>();
        foods.add(new Food("Fish n’ Chips", 150, 135, 50, true));
        foods.add(new Food("Baguette", 100, 80, 25, true));
        foods.add(new Food("Sashimi", 300, 275, 70, true));
        foods.add(new Food("Fugu", 0, 135, 50, true));
        foods.add(new Food("Wine", 100, 90, 20, true));
        foods.add(new Food("Pumpkin Pie", 120, 100, 35, true));
        foods.add(new Food("Veggie Soup", 140, 120, 40, true));
        foods.add(new Food("Fish Stew", 280, 260, 70, true));
        foods.add(new Food("Spakbor Salad", 0, 250, 70, true));
        foods.add(new Food("Fish Sandwich", 200, 180, 50, true));
        foods.add(new Food("The Legends of Spakbor", 0, 2000, 100, true));
        foods.add(new Food("Cooked Pig's Head", 1000, 0, 100, true));
        return foods;
    }
}