package ITEMS;

import LocalCalendar.*;
import Playerr.Point;
import java.util.List;


public class Fish extends Item implements EdibleItem {
    public enum FishType {
        COMMON, REGULAR, LEGENDARY
    }

    private FishType type;
    private List<Season> seasons;
    private List<TimeRange> timeRanges;
    private List<Weather> weathers;
    private List<Point> locations;

    public Fish(String name, FishType type, List<Season> seasons, List<TimeRange> timeRanges, List<Weather> weathers, List<Point> locations) {
        super(name, 0, 0, false);
        if (seasons == null || timeRanges == null || weathers == null || locations == null) {
            throw new IllegalArgumentException("Data ikan tidak boleh null");
        }
        this.type = type;
        this.seasons = seasons;
        this.timeRanges = timeRanges;
        this.weathers = weathers;
        this.locations = locations;

        int sellPrice = calculateSellPrice();
        if (sellPrice > 0) {
            super.setSellPrice(sellPrice);
        }
    }

    private int calculateSellPrice() {
        int seasonCount = seasons.size();
        int totalHours = 0;

        for (TimeRange timeRange : timeRanges) {
            totalHours += timeRange.getDurationInHours();
        }

        int weatherCount = weathers.size();
        int locationCount = locations.size();

        if (seasonCount == 0 || totalHours == 0 || weatherCount == 0 || locationCount == 0) {
            return 0;
        }

        double seasonFactor = 4.0 / seasonCount;
        double timeFactor = 24.0 / totalHours;
        double weatherFactor = 2.0 / weatherCount;
        double locationFactor = 4.0 * locationCount;

        int constant;
        switch (type) {
            case COMMON: constant = 10; break;
            case REGULAR: constant = 5; break;
            case LEGENDARY: constant = 25; break;
            default: constant = 1;
        }

        double price = seasonFactor * timeFactor * weatherFactor * locationFactor * constant;
        return (int) Math.round(price);
    }

    public static class TimeRange {
        private int startHour;
        private int endHour;

        public TimeRange(int startHour, int endHour) {
            if (startHour < 0 || startHour > 23 || endHour < 0 || endHour > 23) {
                throw new IllegalArgumentException("Jam harus di antara 0 dan 23.");
            }
            this.startHour = startHour;
            this.endHour = endHour;
        }

        public int getStartHour() {
            return startHour;
        }

        public int getEndHour() {
            return endHour;
        }

        public int getDurationInHours() {
            if (endHour >= startHour) {
                return endHour - startHour;
            } else {
                return (24 - startHour) + endHour;
            }
        }

        public boolean contains(int hour) {
            if (startHour <= endHour) {
                return hour >= startHour && hour <= endHour;
            } else {
                return hour >= startHour || hour <= endHour;
            }
        }
    }

    public FishType getFishType() {
        return type;
    }

    public int getEnergyValue() {
        return 1;
    }

    public String getCategory() {
        return "Fish";
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public List<TimeRange> getTimeRanges() {
        return timeRanges;
    }

    public List<Weather> getWeathers() {
        return weathers;
    }

    public List<Point> getLocations() {
        return locations;
    }

    public static void main(String[] args) {
        // Buat data dummy
        List<Season> seasons = List.of(Season.SPRING, Season.SUMMER);
        List<TimeRange> timeRanges = List.of(new TimeRange(6, 18)); // 12 jam
        List<Weather> weathers = List.of(Weather.SUNNY);
        List<Point> locations = List.of(new Point(5, 10), new Point(20, 30)); // 2 lokasi

        // Buat ikan REGULAR
        Fish regularFish = new Fish("Mackerel", FishType.REGULAR, seasons, timeRanges, weathers, locations);

        // Print informasi
        System.out.println("==== TEST FISH ====");
        System.out.println("Nama Ikan     : " + regularFish.getName());
        System.out.println("Kategori      : " + regularFish.getCategory());
        System.out.println("Tipe Ikan     : " + regularFish.getFishType());
        System.out.println("Energi        : " + regularFish.getEnergyValue());
        System.out.println("Harga Jual    : " + regularFish.getSellPrice());
    }
}
