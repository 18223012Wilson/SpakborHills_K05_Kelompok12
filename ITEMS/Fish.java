package ITEMS;

import LocalCalendar.GameCalendar;
// import LocalCalendar.Season;
// import LocalCalendar.Weather;
import java.util.List;

public class Fish extends Item implements EdibleItem {
    public enum FishType {
        COMMON, REGULAR, LEGENDARY
    }

    private FishType type;
    private List<Season> seasons; // enum dari GameCalendar
    private List<TimeRange> timeRanges;
    private List<Weather> weathers; // enum dari GameCalendar
    private List<Point> locations;  // location dlm Point

    public Fish(String name, FishType type, List<Season> seasons, List<TimeRange> timeRanges, List<Weather> weathers, List<Point> locations) {
        super(name, 0, 0, false); // buyPrice = 0 karena tidak bisa dibeli, sellPrice dihitung
        this.type = type;
        this.seasons = seasons;
        this.timeRanges = timeRanges;
        this.weathers = weathers;
        this.locations = locations;

        // set sellPrice jika valid
        int sellPrice = calculateSellPrice();
        if (sellPrice > 0) {
        super.setSellPrice(calculateSellPrice()); 
        }
    }

    // hitung harga jual 
    private int calculateSellPrice() {
        int seasonCount = seasons.size();
        int totalHours = 0;
        
        // hitung total jam dari semua TimeRange
        for (TimeRange timeRange : timeRanges) {
            totalHours += timeRange.getDurationInHours();
        }
        
        int weatherCount = weathers.size();
        int locationCount = locations.size();
    
        if (seasonCount == 0 || totalHours == 0 || weatherCount == 0 || locationCount == 0) {
            return 0; // if ada data kosong
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
    
        // sell price
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
    
        // cek if jam tertentu ada dalam rentang waktu (termasuk kasus melewati tengah malam)
        public boolean contains(int hour) {
            if (startHour <= endHour) {
                return hour >= startHour && hour <= endHour;
            } else {
                // misal: jam 22 - 2 pagi
                return hour >= startHour || hour <= endHour;
            }
        }
    }
    
    public FishType getFishType() {
        return type;
    }
    
    // nilai energi ikan = 1
    public int getEnergyValue() {
        return 1;  
    }

    public String getCategory() {
        return "Fish";
    }
}


// TEST FISH
// public class TestFish {
//     public static void main(String[] args) {
//         // Buat data dummy
//         List<Season> seasons = Arrays.asList(Season.SPRING, Season.SUMMER);
//         List<Fish.TimeRange> timeRanges = Arrays.asList(new Fish.TimeRange(6, 18)); // 12 jam
//         List<Weather> weathers = Arrays.asList(Weather.SUNNY);
//         List<Point> locations = Arrays.asList(new Point(5, 10), new Point(20, 30)); // 2 lokasi

//         // Buat ikan REGULAR
//         Fish regularFish = new Fish("Mackerel", Fish.FishType.REGULAR, seasons, timeRanges, weathers, locations);

//         // Print informasi
//         System.out.println("==== TEST FISH ====");
//         System.out.println("Nama Ikan     : " + regularFish.getName());
//         System.out.println("Kategori      : " + regularFish.getCategory());
//         System.out.println("Tipe Ikan     : " + regularFish.getFishType());
//         System.out.println("Energi        : " + regularFish.getEnergyValue());
//         System.out.println("Harga Jual    : " + regularFish.getSellPrice()); // akan pakai rumus dari calculateSellPrice()
//     }
// }
