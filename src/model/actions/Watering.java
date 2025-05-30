package model.actions;

import model.entitas.Player;
import model.calendar.GameCalendar;
import model.map.FarmMap;
import model.map.PlantData;
import model.items.Point;

public class Watering extends Action {
    private Player player;
    private GameCalendar calendar;
    private FarmMap farmMap;
    private Point targetTile;

    public Watering(Player player, GameCalendar calendar, FarmMap farmMap, Point targetTile) {
        super("Watering", "Menyiram tanaman di soil", "Watering Can");
        this.player = player;
        this.calendar = calendar;
        this.farmMap = farmMap;
        this.targetTile = targetTile;
    }

    @Override
    public String execute() {
        if (player.getEnergy() < getEnergyCost() && player.getEnergy() > -20) {
        }
        else if (player.getEnergy() <= -20) {
            return "Not enough energy!";
        }
        if (player.getInventory().getItemCountByName(this.itemNeeded) == 0) {
            return "Missing item: " + this.itemNeeded;
        }

        if (farmMap.getTileType(targetTile.getY(), targetTile.getX()) != 'l') {
            return "Can only water planted soil.";
        }

        PlantData plant = farmMap.getPlantData(targetTile);
        if (plant == null) {
            return "No plant data found for this tile.";
        }
        if (plant.isWateredThisDayCycle() && calendar.getWeather() != model.calendar.Weather.RAINY) {
            return "This plant is already watered today.";
        }
        if (calendar.getWeather() == model.calendar.Weather.RAINY) {
            return "No need to water, it's raining!";
        }

        player.setEnergy(Math.max(-20, player.getEnergy() - getEnergyCost()));
        calendar.addTime(getTimeCost());
        plant.water(calendar.getDay());

        return "Watered plant at (" + targetTile.getX() + ", " + targetTile.getY() + "). Energy: " + player.getEnergy();
    }

    @Override
    public int getEnergyCost() {
        return 5;
    }

    @Override
    public int getTimeCost() {
        return 5;
    }
}