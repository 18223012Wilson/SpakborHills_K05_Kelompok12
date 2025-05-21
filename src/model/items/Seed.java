package model.items;

import model.calendar.Season;

public class Seed extends Item {
    private Season plantingSeason;
    private int daysToHarvest;

    public Seed(String name, int buyPrice, Season plantingSeason, int daysToHarvest, boolean giftable) {
        super(name, buyPrice, buyPrice / 2, giftable);
        this.plantingSeason = plantingSeason;
        this.daysToHarvest = daysToHarvest;
    }

    public Season getPlantingSeason() {
        return plantingSeason;
    }

    public int getDaysToHarvest() {
        return daysToHarvest;
    }

    @Override
    public String getCategory() {
        return "Seeds";
    }
}