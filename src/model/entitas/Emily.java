package model.entitas;

import model.calendar.Season;
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
        // spring
        itemsForSale.add(new Seed("Parsnip Seeds", 20, List.of(model.calendar.Season.SPRING), 1, "Parsnip", true));
        itemsForSale.add(new Seed("Cauliflower Seeds", 80, List.of(model.calendar.Season.SPRING), 5, "Cauliflower", true));
        itemsForSale.add(new Seed("Potato Seeds", 50, List.of(model.calendar.Season.SPRING), 3, "Potato", true));
        itemsForSale.add(new Seed("Wheat Seeds", 60, List.of(model.calendar.Season.SPRING, model.calendar.Season.FALL), 1, "Wheat", true));

        // summer
        itemsForSale.add(new Seed("Blueberry Seeds", 80, List.of(model.calendar.Season.SUMMER), 7, "Blueberry", true));
        itemsForSale.add(new Seed("Tomato Seeds", 50, List.of(model.calendar.Season.SUMMER), 3, "Tomato", true));
        itemsForSale.add(new Seed("Hot Pepper Seeds", 40, List.of(model.calendar.Season.SUMMER), 1, "Hot Pepper", true));
        itemsForSale.add(new Seed("Melon Seeds", 80, List.of(Season.SUMMER), 4, "Melon", true));

        // fall
        itemsForSale.add(new Seed("Pumpkin Seeds", 150, List.of(model.calendar.Season.FALL), 7, "Pumpkin", true));
        itemsForSale.add(new Seed("Grape Seeds", 60, List.of(model.calendar.Season.FALL), 3, "Grape", true));
        itemsForSale.add(new Seed("Cranberry Seeds", 100, List.of(model.calendar.Season.FALL), 2, "Cranberry", true));


        // Crops
        itemsForSale.add(Crop.fromString("Parsnip"));
        itemsForSale.add(Crop.fromString("Cauliflower"));
        itemsForSale.add(Crop.fromString("Wheat"));
        itemsForSale.add(Crop.fromString("Blueberry"));
        itemsForSale.add(Crop.fromString("Tomato"));
        itemsForSale.add(Crop.fromString("Pumpkin"));
        itemsForSale.add(Crop.fromString("Grape"));

        // Food
        itemsForSale.add(new Food("Fish n’ Chips", 150, 135, 50, true));
        itemsForSale.add(new Food("Baguette", 100, 80, 25, true));
        itemsForSale.add(new Food("Wine", 100, 90, 20, true));
        itemsForSale.add(new Food("Pumpkin Pie", 120, 100, 35, true));
        itemsForSale.add(new Food("Sashimi", 300, 275, 70, true));
        itemsForSale.add(new Food("Veggie Soup", 140, 120, 40, true));
        itemsForSale.add(new Food("Fish Stew", 280, 260, 70, true));
        itemsForSale.add(new Food("Fish Sandwich", 200, 180, 50, true));
        itemsForSale.add(new Food("Cooked Pig's Head", 1000, 0, 100, true));


        // Misc
        itemsForSale.add(Misc.findMiscItemByName("Coal"));
        itemsForSale.add(Misc.findMiscItemByName("Firewood"));
        itemsForSale.add(Misc.findMiscItemByName("Proposal Ring"));
        itemsForSale.add(Misc.findMiscItemByName("Egg"));


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
