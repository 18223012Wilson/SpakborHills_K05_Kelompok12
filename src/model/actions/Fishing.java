package model.actions;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalTime;

import model.entitas.Player;
import model.calendar.GameCalendar;
import model.calendar.Season;
import model.calendar.Weather;
import model.items.Fish;
import model.items.FishDatabase;
import model.items.Inventory;
import model.items.Point;
import javax.swing.JOptionPane;

public class Fishing extends Action {
    private Player player;
    private GameCalendar calendar;
    private Point targetWaterTile;

    private int targetNumber;
    private int maxAttempts;
    private Fish selectedFish;
    private int minRange;
    private int maxRange;

    public Fishing(Player player, GameCalendar calendar, Point targetWaterTile) {
        super("Fishing", "Memancing ikan.", "Fishing Rod");
        if (player == null || calendar == null || targetWaterTile == null) {
            throw new IllegalArgumentException("Player, GameCalendar, or targetWaterTile cannot be null");
        }
        this.player = player;
        this.calendar = calendar;
        this.targetWaterTile = targetWaterTile;
    }

    private boolean initializeFish() {
        List<Fish> availableFish = new ArrayList<>();
        LocalTime currentTime = calendar.getTime();
        int currentHour = currentTime.getHour();
        Season currentSeason = calendar.getSeason();
        Weather currentWeather = calendar.getWeather();

        String playerLocation = player.getLocation();

        for (Fish fish : FishDatabase.getAllFish()) {
            if (!fish.getLocations().contains(playerLocation)) {
                continue;
            }
            boolean seasonMatch = fish.getSeasons().contains(currentSeason) || fish.getSeasons().size() == 4;
            if (!seasonMatch) {
            }

            boolean weatherMatch = fish.getWeathers().contains(currentWeather) || fish.getWeathers().size() == 2;
            if (!weatherMatch) {
            }

            boolean timeMatches = false;
            for (Fish.TimeRange timeRange : fish.getTimeRanges()) {
                if (timeRange.contains(currentHour)) {
                    timeMatches = true;
                    break;
                }
            }
            if (!timeMatches) {
                continue;
            }
            availableFish.add(fish);
        }

        if (availableFish.isEmpty()) {
            return false;
        }

        Random rand = new Random();
        selectedFish = availableFish.get(rand.nextInt(availableFish.size()));

        switch (selectedFish.getFishType()) {
            case COMMON:
                minRange = 1; maxRange = 10; maxAttempts = 10; break;
            case REGULAR:
                minRange = 1; maxRange = 100; maxAttempts = 10; break;
            case LEGENDARY:
                minRange = 1; maxRange = 500; maxAttempts = 7; break;
        }
        targetNumber = rand.nextInt(maxRange - minRange + 1) + minRange;
        return true;
    }

    @Override
    public String execute() {
        int currentEnergy = player.getEnergy();
        if (currentEnergy < getEnergyCost() && currentEnergy > -20 && (currentEnergy - getEnergyCost() < -20) ) {
            return "Not enough energy to fish. Will become too exhausted!";
        }
        else if (currentEnergy <= -20 && getEnergyCost() > 0) {
            return "Not enough energy (already at or below -20)!";
        }
        else if (currentEnergy < getEnergyCost() && getEnergyCost() > 0) {
            return "Not enough energy for Fishing!";
        }

        if (player.getInventory().getItemCountByName(this.itemNeeded) == 0) {
            return "Missing item: " + this.itemNeeded;
        }

        calendar.pauseTime();

        String initialMessage = "World time paused. Started fishing...\n";

        if (!initializeFish()) {
            player.decreaseEnergy(getEnergyCost());
            calendar.addTime(getTimeCost());
            calendar.startTime();
            return initialMessage + "No fish biting at " + player.getLocation() + " right now.";
        }

        initialMessage += "Trying to catch: " + selectedFish.getName() + " (Type: " + selectedFish.getFishType() + ")\n";
        player.decreaseEnergy(getEnergyCost());

        int attempts = 0;
        boolean caught = false;
        StringBuilder fishingLog = new StringBuilder(initialMessage);

        while (attempts < maxAttempts) {
            String guessStr = JOptionPane.showInputDialog(null,
                    "Guess the number (" + minRange + "-" + maxRange + "). Attempts left: " + (maxAttempts - attempts) + "\n" +
                            "Targeting: " + selectedFish.getName() + " (Cheat: " + targetNumber + ")",
                    "Fishing Minigame (" + player.getLocation() + ")", JOptionPane.QUESTION_MESSAGE);

            if (guessStr == null) { // Player cancelled
                fishingLog.append("Fishing cancelled by player.\n");
                break;
            }

            try {
                int guess = Integer.parseInt(guessStr);
                attempts++;
                if (guess == targetNumber) {
                    addFishToInventory(selectedFish);
                    player.onFishCaught(selectedFish);
                    fishingLog.append("Correct! Caught " + selectedFish.getName() + "!\n");
                    caught = true;
                    break;
                } else if (guess < targetNumber) {
                    fishingLog.append("Attempt " + attempts + ": " + guess + " - Too small.\n");
                } else {
                    fishingLog.append("Attempt " + attempts + ": " + guess + " - Too large.\n");
                }
                if (attempts >= maxAttempts && !caught) {
                    fishingLog.append("Out of attempts! The fish got away. The correct number was " + targetNumber + ".\n");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        calendar.addTime(getTimeCost());
        calendar.startTime();
        fishingLog.append("World time resumed. Fishing finished. Energy: ").append(player.getEnergy());
        return fishingLog.toString();
    }

    private void addFishToInventory(Fish fish) {
        Inventory inventory = player.getInventory();
        inventory.addItem(fish, 1);
    }

    @Override
    public int getEnergyCost() {
        return 5;
    }

    @Override
    public int getTimeCost() {
        return 15;
    }
}