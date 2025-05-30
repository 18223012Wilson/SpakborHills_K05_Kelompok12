package model.actions;

import model.entitas.Player;
import model.calendar.GameCalendar;
import model.calendar.Season;
import model.map.FarmMap;
import model.map.PlantData;
import model.items.Point;
import model.items.Seed;
import model.items.Item;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;


public class Planting extends Action {
    private Player player;
    private GameCalendar calendar;
    private FarmMap farmMap;
    private Point targetTile;

    public Planting(Player player, GameCalendar calendar, FarmMap farmMap, Point targetTile) {
        super("Planting", "Menanam seed di soil", "Seed (any)");
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

        List<Seed> availableSeeds = player.getInventory().getItemsByCategory("Seeds", Seed.class);
        if (availableSeeds.isEmpty()) {
            return "No seeds in inventory.";
        }

        String[] seedNames = availableSeeds.stream().map(Item::getName).distinct().toArray(String[]::new);
        if (seedNames.length == 0) return "No distinct seeds found.";

        String chosenSeedName = (String) JOptionPane.showInputDialog(
                null, "Choose seed to plant:", "Planting",
                JOptionPane.QUESTION_MESSAGE, null, seedNames, seedNames[0]);

        if (chosenSeedName == null) {
            return "Planting cancelled.";
        }

        Seed seedToPlant = null;
        for (Seed s : availableSeeds) {
            if (s.getName().equals(chosenSeedName)) {
                seedToPlant = s;
                break;
            }
        }
        if (seedToPlant == null) return "Error selecting seed.";


        boolean canPlantInCurrentSeason = false;
        for (Season s : seedToPlant.getPlantingSeasons()) {
            if (s == calendar.getSeason()) {
                canPlantInCurrentSeason = true;
                break;
            }
        }
        if (!canPlantInCurrentSeason) {
            return seedToPlant.getName() + " cannot be planted in " + calendar.getSeason() + ".";
        }

        if (farmMap.getTileType(targetTile.getY(), targetTile.getX()) != 't') {
            return "Can only plant on tilled soil.";
        }

        player.getInventory().removeItemByName(seedToPlant.getName(), 1);
        player.setEnergy(Math.max(-20, player.getEnergy() - getEnergyCost()));
        calendar.addTime(getTimeCost());
        farmMap.setTileType(targetTile.getY(), targetTile.getX(), 'l');
        farmMap.addPlantData(targetTile, new PlantData(seedToPlant, calendar.getDay(), farmMap, calendar));

        return "Planted " + seedToPlant.getName() + " at (" + targetTile.getX() + ", " + targetTile.getY() + "). Energy: " + player.getEnergy();
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