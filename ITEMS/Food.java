package ITEMS;

public class Food extends Item implements EdibleItem {
    private int energyValue;

    public Food(String name, int buyPrice, int sellPrice, int energyValue, boolean giftable) {
        super(name, buyPrice, sellPrice, giftable);
        if (energyValue <= 0) {
            throw new IllegalArgumentException("Energy value must be positive.");
        }
        this.energyValue = energyValue;
    }

    public int getEnergyValue() {
        return energyValue;
    }

    public String getCategory() {
        return "Food";
    }
}
