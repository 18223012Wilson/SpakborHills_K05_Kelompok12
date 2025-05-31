package model.actions;

import model.entitas.Player;
import model.calendar.GameCalendar;
import model.map.FarmMap;
import model.map.PlantData;
import model.items.Point;
import model.items.Crop;

public class Harvesting extends Action {
    private Player player;
    private GameCalendar calendar;
    private FarmMap farmMap;
    private Point targetTile;

    public Harvesting(Player player, GameCalendar calendar, FarmMap farmMap, Point targetTile) {
        super("Harvesting", "Memanen tanaman yang siap panen.", "");
        this.player = player;
        this.calendar = calendar;
        this.farmMap = farmMap;
        this.targetTile = targetTile;
    }

    @Override
    public String execute() {
        if (player.getEnergy() < getEnergyCost() && player.getEnergy() > -20 && getEnergyCost() > 0) {
        }
        else if (player.getEnergy() <= -20 && getEnergyCost() > 0) {
            return "Not enough energy (already at critical)!";
        }
        else if (player.getEnergy() < getEnergyCost() && getEnergyCost() > 0) {
            return "Not enough energy for harvesting!";
        }

        if (farmMap.getTileType(targetTile.getY(), targetTile.getX()) != 'l') {
            return "Can only harvest from planted soil ('l').";
        }

        PlantData plant = farmMap.getPlantData(targetTile);
        if (plant == null) {
            return "No plant data at this tile.";
        }

        if (!plant.isReadyToHarvest()) {
            return "Plant not ready for harvest. Growth: " + plant.getGrowthDays() + "/" + plant.getSeed().getDaysToHarvest() + " days.";
        }

        player.decreaseEnergy(getEnergyCost());
        calendar.addTime(getTimeCost());

        String cropName = plant.getSeed().getCropYields();
        Crop harvestedCrop = Crop.fromString(cropName);
        int yieldAmount = Crop.getYieldAmount(cropName);
        player.getInventory().addItem(harvestedCrop, yieldAmount);

        farmMap.removePlantData(targetTile);
        farmMap.setTileType(targetTile.getY(), targetTile.getX(), 't');

        player.setHasHarvested(true);

        return "Harvested " + yieldAmount + " " + harvestedCrop.getName() + ". Energy: " + player.getEnergy();
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