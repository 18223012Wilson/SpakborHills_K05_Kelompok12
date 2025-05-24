package ITEMS;

import java.util.ArrayList;
import java.util.List;

public class Crop extends Item implements EdibleItem {
    private int harvestAmount;

    public Crop(String name, int buyPrice, int sellPrice, int harvestAmount, boolean giftable) {
        super(name, buyPrice, sellPrice, giftable);
        if (harvestAmount <= 0) {
            throw new IllegalArgumentException("Harvest amount must be positive.");
        }
        this.harvestAmount = harvestAmount;
    }

    public int getHarvestAmount() {
        return harvestAmount;
    }

    @Override
    public int getEnergyValue() {
        return 3;
    }

    @Override
    public String getCategory() {
        return "Crop";
    }

    public static List<Crop> getAllCrops() {
        List<Crop> crops = new ArrayList<>();
        crops.add(new Crop("Parsnip", 50, 35, 1, true));
        crops.add(new Crop("Cauliflower", 200, 150, 1, true));
        crops.add(new Crop("Potato", 0, 80, 1, true));
        crops.add(new Crop("Wheat", 50, 30, 3, true));
        crops.add(new Crop("Blueberry", 150, 40, 3, true));
        crops.add(new Crop("Tomato", 90, 60, 1, true));
        crops.add(new Crop("Hot Pepper", 0, 40, 1, true));
        crops.add(new Crop("Melon", 0, 250, 1, true));
        crops.add(new Crop("Cranberry", 0, 25, 10, true));
        crops.add(new Crop("Pumpkin", 300, 250, 1, true));
        crops.add(new Crop("Grape", 100, 10, 20, true));
        return crops;
    }
}
