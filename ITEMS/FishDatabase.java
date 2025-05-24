package ITEMS;

import LocalCalendar.*;
import java.util.List;
import java.util.ArrayList;

public class FishDatabase {
    public static List<Fish> getAllFish() {
        List<Fish> fishList = new ArrayList<>();

        // COMMON FISH 
        fishList.add(new Fish("Bullhead", Fish.FishType.COMMON,
                List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER), // Any season
                List.of(new Fish.TimeRange(0, 23)), // Any time
                List.of(Weather.SUNNY, Weather.RAINY), // Any weather
                List.of("Mountain Lake")));

        fishList.add(new Fish("Carp", Fish.FishType.COMMON,
                List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER),
                List.of(new Fish.TimeRange(0, 23)),
                List.of(Weather.SUNNY, Weather.RAINY),
                List.of("Mountain Lake", "Pond")));

        fishList.add(new Fish("Chub", Fish.FishType.COMMON,
                List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER),
                List.of(new Fish.TimeRange(0, 23)),
                List.of(Weather.SUNNY, Weather.RAINY),
                List.of("Forest River", "Mountain Lake")));

        // REGULAR FISH 
        fishList.add(new Fish("Largemouth Bass", Fish.FishType.REGULAR,
                List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER), 
                List.of(new Fish.TimeRange(6, 18)),
                List.of(Weather.SUNNY, Weather.RAINY),
                List.of("Mountain Lake")));

        fishList.add(new Fish("Rainbow Trout", Fish.FishType.REGULAR,
                List.of(Season.SUMMER),
                List.of(new Fish.TimeRange(6, 18)),
                List.of(Weather.SUNNY),
                List.of("Forest River", "Mountain Lake")));

        fishList.add(new Fish("Sturgeon", Fish.FishType.REGULAR,
                List.of(Season.SUMMER, Season.WINTER),
                List.of(new Fish.TimeRange(6, 18)),
                List.of(Weather.SUNNY, Weather.RAINY),
                List.of("Mountain Lake")));

        fishList.add(new Fish("Midnight Carp", Fish.FishType.REGULAR,
                List.of(Season.WINTER, Season.FALL),
                List.of(new Fish.TimeRange(20, 23), new Fish.TimeRange(0, 2)),
                List.of(Weather.SUNNY, Weather.RAINY),
                List.of("Mountain Lake", "Pond")));

        fishList.add(new Fish("Flounder", Fish.FishType.REGULAR,
                List.of(Season.SPRING, Season.SUMMER),
                List.of(new Fish.TimeRange(6, 22)),
                List.of(Weather.SUNNY, Weather.RAINY),
                List.of("Ocean")));

        fishList.add(new Fish("Halibut", Fish.FishType.REGULAR,
                List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER), // Any season
                List.of(new Fish.TimeRange(6, 11), new Fish.TimeRange(19, 23), new Fish.TimeRange(0, 2)),
                List.of(Weather.SUNNY, Weather.RAINY),
                List.of("Ocean")));

        fishList.add(new Fish("Octopus", Fish.FishType.REGULAR,
                List.of(Season.SUMMER),
                List.of(new Fish.TimeRange(6, 22)),
                List.of(Weather.SUNNY, Weather.RAINY),
                List.of("Ocean")));

        fishList.add(new Fish("Pufferfish", Fish.FishType.REGULAR,
                List.of(Season.SUMMER),
                List.of(new Fish.TimeRange(0, 16)),
                List.of(Weather.SUNNY),
                List.of("Ocean")));

        fishList.add(new Fish("Sardine", Fish.FishType.REGULAR,
                List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER), // Any season
                List.of(new Fish.TimeRange(6, 18)),
                List.of(Weather.SUNNY, Weather.RAINY),
                List.of("Ocean")));

        fishList.add(new Fish("Super Cucumber", Fish.FishType.REGULAR,
                List.of(Season.SUMMER, Season.FALL, Season.WINTER),
                List.of(new Fish.TimeRange(18, 23), new Fish.TimeRange(0, 2)),
                List.of(Weather.SUNNY, Weather.RAINY),
                List.of("Ocean")));

        fishList.add(new Fish("Catfish", Fish.FishType.REGULAR,
                List.of(Season.SPRING, Season.SUMMER, Season.FALL),
                List.of(new Fish.TimeRange(6, 22)),
                List.of(Weather.RAINY),
                List.of("Forest River", "Pond")));

        fishList.add(new Fish("Salmon", Fish.FishType.REGULAR,
                List.of(Season.FALL),
                List.of(new Fish.TimeRange(6, 18)),
                List.of(Weather.SUNNY, Weather.RAINY),
                List.of("Forest River")));

        // LEGENDARY FISH
        fishList.add(new Fish("Angler", Fish.FishType.LEGENDARY,
                List.of(Season.FALL),
                List.of(new Fish.TimeRange(8, 20)),
                List.of(Weather.SUNNY, Weather.RAINY),
                List.of("Pond")));

        fishList.add(new Fish("Crimsonfish", Fish.FishType.LEGENDARY,
                List.of(Season.SUMMER),
                List.of(new Fish.TimeRange(8, 20)),
                List.of(Weather.SUNNY, Weather.RAINY),
                List.of("Ocean")));

        fishList.add(new Fish("Glacierfish", Fish.FishType.LEGENDARY,
                List.of(Season.WINTER),
                List.of(new Fish.TimeRange(8, 20)),
                List.of(Weather.SUNNY, Weather.RAINY),
                List.of("Forest River")));

        fishList.add(new Fish("Legend", Fish.FishType.LEGENDARY,
                List.of(Season.SPRING),
                List.of(new Fish.TimeRange(8, 20)),
                List.of(Weather.RAINY),
                List.of("Mountain Lake")));

        return fishList;
    }
}
