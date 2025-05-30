package model.entitas;

import model.items.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Emily extends NPC {
    private static final String IMAGE_PATH = "/npcs/emily.png";
    private String[] loved = {"Seeds"};
    private String[] liked = {"Catfish", "Salmon", "Sardine"};
    private String[] hated = {"Coal", "Firewood"};

    private static List<Item> itemsForSale;

    public Emily() {
        super("Emily", "Store", IMAGE_PATH);
        setLovedItems(loved);
        setLikedItems(liked);
        setHatedItems(hated);
        initializeItemsForSale();
    }

    private static void initializeItemsForSale() {
        itemsForSale = new ArrayList<>();

        // Seeds
        itemsForSale.add(new Seed("Parsnip Seeds", 20, List.of(model.calendar.Season.SPRING), 1, "Parsnip", true));
        itemsForSale.add(new Seed("Cauliflower Seeds", 80, List.of(model.calendar.Season.SPRING), 5, "Cauliflower", true));
        itemsForSale.add(new Seed("Potato Seeds", 50, List.of(model.calendar.Season.SPRING), 3, "Potato", true));
        itemsForSale.add(new Seed("Wheat Seeds", 60, List.of(model.calendar.Season.SPRING, model.calendar.Season.FALL), 1, "Wheat", true));
        itemsForSale.add(new Seed("Blueberry Seeds", 80, List.of(model.calendar.Season.SUMMER), 7, "Blueberry", true));
        itemsForSale.add(new Seed("Tomato Seeds", 50, List.of(model.calendar.Season.SUMMER), 3, "Tomato", true));
        itemsForSale.add(new Seed("Pumpkin Seeds", 150, List.of(model.calendar.Season.FALL), 7, "Pumpkin", true));
        itemsForSale.add(new Seed("Grape Seeds", 60, List.of(model.calendar.Season.FALL), 3, "Grape", true));


        // Crops
        itemsForSale.add(Crop.fromString("Parsnip"));
        itemsForSale.add(Crop.fromString("Wheat"));

        // Food
        itemsForSale.add(new Food("Fish n’ Chips", 150, 135, 50, true));
        itemsForSale.add(new Food("Baguette", 100, 80, 25, true));
        itemsForSale.add(new Food("Wine", 100, 90, 20, true));
        itemsForSale.add(new Food("Pumpkin Pie", 120, 100, 35, true));

        // Misc
        itemsForSale.add(Misc.findMiscItemByName("Coal"));
        itemsForSale.add(Misc.findMiscItemByName("Firewood"));
        itemsForSale.add(Misc.findMiscItemByName("Proposal Ring"));

        // Recipes as Misc
        itemsForSale.add(new Misc("Recipe: Fish n’ Chips", 20, 0, false));
        itemsForSale.add(new Misc("Recipe: Fish Sandwich", 30, 0, false));

        // Add other recipes
        for (Recipe recipe : RecipeDatabase.getAllRecipes()) {
            if (recipe.getName().equals("Fish n’ Chips") || recipe.getName().equals("Fish Sandwich")) {
            }
        }
    }

    public static List<Item> getItemsForSaleStatic() {
        if (itemsForSale == null) {
            initializeItemsForSale();
        }
        return itemsForSale;
    }
}