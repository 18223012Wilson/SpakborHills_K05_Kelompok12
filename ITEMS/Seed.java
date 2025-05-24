package ITEMS;

import LocalCalendar.*;
import java.util.List;
import java.util.ArrayList;

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

    // Static method untuk return semua seeds yang tersedia
    public static List<Seed> getAllSeeds() {
        List<Seed> seeds = new ArrayList<>();

        // spring
        seeds.add(new Seed("Parsnip Seeds", 20, Season.SPRING, 1, false));
        seeds.add(new Seed("Cauliflower Seeds", 80, Season.SPRING, 5, false));
        seeds.add(new Seed("Potato Seeds", 50, Season.SPRING, 3, false));
        seeds.add(new Seed("Wheat Seeds", 60, Season.SPRING, 1, false));

        // summer
        seeds.add(new Seed("Blueberry Seeds", 80, Season.SUMMER, 7, false));
        seeds.add(new Seed("Tomato Seeds", 50, Season.SUMMER, 3, false));
        seeds.add(new Seed("Hot Pepper Seeds", 40, Season.SUMMER, 1, false));
        seeds.add(new Seed("Melon Seeds", 80, Season.SUMMER, 4, false));

        // fall
        seeds.add(new Seed("Cranberry Seeds", 100, Season.FALL, 2, false));
        seeds.add(new Seed("Pumpkin Seeds", 150, Season.FALL, 7, false));
        seeds.add(new Seed("Wheat Seeds", 60, Season.FALL, 1, false));
        seeds.add(new Seed("Grape Seeds", 60, Season.FALL, 3, false));

        // winter: tidak ada seed

        return seeds;
    }
}
