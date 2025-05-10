package ITEMS;

public class Crop extends Item implements EdibleItem {
    private int harvestAmount; 

    public Crop(String name, int buyPrice, int sellPrice, int harvestAmount, int energyValue, boolean giftable) {
        super(name, buyPrice, sellPrice, giftable);
        if (harvestAmount <= 0) {
            throw new IllegalArgumentException("Harvest amount must be positive.");
        }
        this.harvestAmount = harvestAmount;
    }

    public int getHarvestAmount() {
        return harvestAmount;
    }

    public int getEnergyValue() {
        return 3; // setiap crop yang dimakan akan mengembalikan energi sebanyak 3
    }

    public String getCategory() {
        return "Crop";  
    }
}
