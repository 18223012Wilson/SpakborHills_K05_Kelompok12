package ITEMS;

import LocalCalendar.GameCalendar;
// import LocalCalendar.Season; //enum Season

public class Seed extends Item {
    private Season season;  
    private int daysToHarvest;

    public Seed(String name, int buyPrice, Season season, int daysToHarvest, boolean giftable) {
        super(name, buyPrice, buyPrice / 2, giftable);
        if (daysToHarvest <= 0) {
            throw new IllegalArgumentException("Days to harvest harus positif!");
        }
        this.season = season;
        this.daysToHarvest = daysToHarvest;
    }

    public Season getSeason() {
        return season;
    }

    public int getDaysToHarvest() {
        return daysToHarvest;
    }

    public boolean canBePlanted(Season currentSeason) {
        return this.season == currentSeason;
    }

    @Override
    public String getCategory() {
        return "Seed";
    }
}
