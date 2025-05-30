package model.items;

public class Crop extends Item implements EdibleItem {
    private int energyValue;

    public Crop(String name, int buyPrice, int sellPrice, int energyValue, boolean giftable) {
        super(name, buyPrice, sellPrice, giftable);
        this.energyValue = energyValue;
    }

    @Override
    public String getCategory() {
        return "Crops";
    }

    @Override
    public int getEnergyValue() {
        return energyValue;
    }

    @Override
    public String getImagePath() {
        return "/items/crops/" + getName().toLowerCase().replace(" ", "_") + ".png";
    }

    public static Crop fromString(String cropName) {
        switch (cropName) {
            case "Parsnip":
                return new Crop("Parsnip", 50, 35, 3, true);
            case "Cauliflower":
                return new Crop("Cauliflower", 200, 150, 3, true);
            case "Potato":
                return new Crop("Potato", 0, 80, 3, true);
            case "Wheat":
                return new Crop("Wheat", 50, 30, 3, true);
            case "Blueberry":
                return new Crop("Blueberry", 150, 40, 3, true);
            case "Tomato":
                return new Crop("Tomato", 90, 60, 3, true);
            case "Hot Pepper":
                return new Crop("Hot Pepper", 0, 40, 3, true);
            case "Melon":
                return new Crop("Melon", 0, 250, 3, true);
            case "Cranberry":
                return new Crop("Cranberry", 0, 25, 3, true);
            case "Pumpkin":
                return new Crop("Pumpkin", 300, 250, 3, true);
            case "Grape":
                return new Crop("Grape", 100, 10, 3, true);
            default:
                return new Crop(cropName, 0, 0, 3, true);
        }
    }
    public static int getYieldAmount(String cropName) {
        switch (cropName) {
            case "Parsnip": return 1;
            case "Cauliflower": return 1;
            case "Potato": return 1;
            case "Wheat": return 3;
            case "Blueberry": return 3;
            case "Tomato": return 1;
            case "Hot Pepper": return 1;
            case "Melon": return 1;
            case "Cranberry": return 10;
            case "Pumpkin": return 1;
            case "Grape": return 20;
            default: return 1;
        }
    }
}