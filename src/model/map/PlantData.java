package model.map;

import model.items.Seed;
import model.calendar.GameCalendar;
import model.calendar.Season;
import model.calendar.Weather;

public class PlantData {
    private Seed seed;
    private int plantedDay;
    private int lastWateredDay;
    private boolean wateredThisDayCycle;
    private int growthDays;
    private FarmMap farmMap;

    public PlantData(Seed seed, int plantedDay, FarmMap farmMap, GameCalendar calendar) {
        this.seed = seed;
        this.plantedDay = plantedDay;
        this.lastWateredDay = 0;
        this.wateredThisDayCycle = false;
        this.growthDays = 0;
        this.farmMap = farmMap;
    }

    public Seed getSeed() {
        return seed;
    }

    public boolean isReadyToHarvest() {
        return growthDays >= seed.getDaysToHarvest();
    }

    public void water(int currentDay) {
        this.wateredThisDayCycle = true;
        this.lastWateredDay = currentDay;
    }

    public boolean needsWateringToday(GameCalendar calendar) {
        if (calendar.getWeather() == Weather.RAINY) {
            return false;
        }
        return !wateredThisDayCycle;
    }

    public boolean wasWateredEffectively(int currentDay) {
        return (currentDay - lastWateredDay < 2) || wateredThisDayCycle;
    }

    public void dailyResetAndGrowth(GameCalendar calendar) {
        boolean effectivelyWateredForPreviousDay = (calendar.getWeather() == Weather.RAINY || this.wateredThisDayCycle);

        if (effectivelyWateredForPreviousDay) {
            if (growthDays < seed.getDaysToHarvest()) {
                growthDays++;
            }
        }
        this.wateredThisDayCycle = (calendar.getWeather() == Weather.RAINY);
        if (calendar.getWeather() == Weather.RAINY) {
            this.lastWateredDay = calendar.getDay();
        }
    }

    public boolean isWateredThisDayCycle() {
        return wateredThisDayCycle;
    }

    public int getGrowthDays() {
        return growthDays;
    }
}