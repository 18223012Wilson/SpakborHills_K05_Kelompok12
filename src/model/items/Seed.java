package model.items;

import model.calendar.Season;
import java.util.List;

public class Seed extends Item {
    private List<Season> plantingSeasons;
    private int daysToHarvest;
    private String cropYields;

    public Seed(String name, int buyPrice, List<Season> plantingSeasons, int daysToHarvest, String cropYields, boolean giftable) {
        super(name, buyPrice, buyPrice / 2, giftable);
        if (plantingSeasons == null || plantingSeasons.isEmpty()) {
            throw new IllegalArgumentException("Planting seasons cannot be null or empty.");
        }
        this.plantingSeasons = plantingSeasons;
        this.daysToHarvest = daysToHarvest;
        this.cropYields = cropYields;
    }

    public List<Season> getPlantingSeasons() {
        return plantingSeasons;
    }

    public int getDaysToHarvest() {
        return daysToHarvest;
    }

    public String getCropYields() {
        return cropYields;
    }

    @Override
    public String getCategory() {
        return "Seeds";
    }

    @Override
    public String getImagePath() {
        String baseName = getName().replace(" Seeds", "").toLowerCase().replace(" ", "_");
        return "/items/seeds/" + baseName + "_seeds.png";
    }
}