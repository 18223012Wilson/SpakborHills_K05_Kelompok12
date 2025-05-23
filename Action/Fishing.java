package Action;

import java.util.Random;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalTime;

import Playerr.Player;
import LocalCalendar.GameCalendar;
import LocalCalendar.Season;
import LocalCalendar.Weather;
import ITEMS.Fish;
import ITEMS.FishDatabase;
import ITEMS.Inventory;

public class Fishing extends Action {
    private Player player;
    private GameCalendar calendar;
    private int targetNumber;
    private int maxAttempts;
    private Fish selectedFish;
    private int minRange;
    private int maxRange;

    public Fishing(Player player, GameCalendar calendar) {
        super("Fishing", "Memancing di lokasi tertentu", "Fishing Rod");
        if (player == null || calendar == null) {
            throw new IllegalArgumentException("Player atau GameCalendar tidak boleh null");
        }
        this.player = player;
        this.calendar = calendar;
    }

    private boolean initializeFish() {
        List<Fish> availableFish = new ArrayList<>();
        LocalTime currentTime = calendar.getTime();
        int currentHour = currentTime.getHour();
        Season currentSeason = calendar.getSeason();
        Weather currentWeather = calendar.getWeather();
        String playerLocation = player.getLocation();

        // Ambil semua ikan dari FishDatabase
        for (Fish fish : FishDatabase.getAllFish()) {
            // Cek kecocokan musim
            if (!fish.getSeasons().contains(currentSeason)) {
                continue;
            }
            // Cek kecocokan cuaca
            if (!fish.getWeathers().contains(currentWeather)) {
                continue;
            }
            // Cek kecocokan lokasi
            if (!fish.getLocations().contains(playerLocation)) {
                continue;
            }
            // Cek kecocokan waktu
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
            // Ikan cocok, tambahkan ke daftar
            availableFish.add(fish);
        }

        // Jika tidak ada ikan yang cocok
        if (availableFish.isEmpty()) {
            return false;
        }

        // Cetak daftar ikan yang cocok untuk debugging
        System.out.println("Ikan yang tersedia untuk dipancing:");
        for (Fish fish : availableFish) {
            System.out.println("- " + fish.getName() + " (" + fish.getFishType() + ")");
        }

        // Pilih ikan secara acak
        Random rand = new Random();
        selectedFish = availableFish.get(rand.nextInt(availableFish.size()));

        // Tentukan parameter tebak angka berdasarkan rarity
        switch (selectedFish.getFishType()) {
            case COMMON:
                minRange = 1;
                maxRange = 10;
                maxAttempts = 10;
                break;
            case REGULAR:
                minRange = 1;
                maxRange = 100;
                maxAttempts = 10;
                break;
            case LEGENDARY:
                minRange = 1;
                maxRange = 500;
                maxAttempts = 7;
                break;
        }
        targetNumber = rand.nextInt(maxRange - minRange + 1) + minRange;
        return true;
    }

    public Fish.FishType execute(Scanner scanner) {
        int currentEnergy = player.getEnergy();
        if (currentEnergy <= -20 || currentEnergy < 5) {
            System.out.println("Energi terlalu rendah (kurang dari 5 atau -20 atau lebih), aksi tidak bisa dilakukan!");
            return null;
        }

        // Hentikan world time
        calendar.pauseTime();
        System.out.println("World time dihentikan. Mulai memancing...");

        // Inisialisasi ikan
        if (!initializeFish()) {
            System.out.println("Tidak ada ikan yang tersedia di lokasi ini saat ini!");
            calendar.addTime(15);
            calendar.startTime();
            return null;
        }

        System.out.println("Ikan yang dipancing: " + selectedFish.getName() + " (Tipe: " + selectedFish.getFishType() + ")");

        // Kurangi energi
        int newEnergy = currentEnergy - 5;
        player.setEnergy(newEnergy);

        // Tebak angka
        int attempts = 0;
        boolean caught = false;
        while (attempts < maxAttempts) {
            System.out.print("Tebak angka dari range " + minRange + " sampai " + maxRange + " (sisa " + (maxAttempts - attempts) + " percobaan): ");
            if (scanner.hasNextInt()) {
                int guess = scanner.nextInt();
                attempts++;
                if (guess == targetNumber) {
                    System.out.println("Selamat! Tebakan benar. Ikan " + selectedFish.getName() + " ditangkap!");
                    // Tambahkan ikan ke inventory
                    addFishToInventory(selectedFish);
                    caught = true;
                    break;
                } else if (guess < targetNumber) {
                    System.out.println("Terlalu kecil. Coba lagi!");
                } else {
                    System.out.println("Terlalu besar. Coba lagi!");
                }
                if (attempts == maxAttempts) {
                    System.out.println("Gagal! Jumlah percobaan habis. Angka yang benar: " + targetNumber);
                }
            } else {
                String input = scanner.next().toLowerCase();
                if (input.equals("show") || input.equals("tampilkan")) {
                    System.out.println("Cheat: Angka yang benar adalah " + targetNumber);
                } else {
                    System.out.println("Input tidak valid. Masukkan angka atau 'show'/'tampilkan' untuk cheat.");
                }
            }
            scanner.nextLine(); // Bersihkan buffer setelah input
        }

        // Tambah 15 menit
        calendar.addTime(15);
        System.out.println("World time dilanjutkan. Aksi Fishing selesai.");

        // Lanjutkan waktu setelah memancing
        calendar.startTime();

        // Kembalikan tipe ikan jika berhasil ditangkap, null jika gagal
        return caught ? selectedFish.getFishType() : null;
    }

    private void addFishToInventory(Fish fish) {
        Inventory inventory = player.getInventory();
        if (inventory == null) {
            System.out.println("Error: Inventory pemain belum diinisialisasi!");
            return;
        }
        inventory.addItem(fish, 1);
        System.out.println("Ikan " + fish.getName() + " ditambahkan ke inventory.");
    }

    @Override
    public int getEnergyCost() {
        return 5; // 5 energi
    }

    @Override
    public int getTimeCost() {
        return 15; // 15 menit
    }

    // public static void main(String[] args) {
    //     // Setup GameCalendar
    //     GameCalendar calendar = new GameCalendar();
    //     Scanner scanner = new Scanner(System.in);

    //     // Pause waktu sejak awal untuk mencegah perubahan selama inisialisasi
    //     calendar.pauseTime();

    //     // Thread untuk simulasi waktu (latar belakang)
    //     Thread timeThread = new Thread(() -> {
    //         try {
    //             while (!Thread.currentThread().isInterrupted()) {
    //                 calendar.updateTime();
    //                 Thread.sleep(1000); // 1 detik nyata = 5 menit in-game
    //             }
    //         } catch (InterruptedException e) {
    //             System.out.println("Simulasi waktu dihentikan.");
    //         }
    //     });
    //     timeThread.start();

    //     // Skenario untuk menguji semua ikan
    //     // Pemain 1: Mountain Lake, SPRING, RAINY, 10:00 (Bullhead, Carp, Chub, Largemouth Bass, Sturgeon, Legend)
    //     Player player1 = new Player("Asep", "Mountain Lake", "Male", "SpakborFarm");

    //     // Pemain 2: Forest River, FALL, SUNNY, 12:00 (Chub, Salmon)
    //     Player player2 = new Player("Budi", "Forest River", "Male", "RiverFarm");

    //     // Pemain 3: Ocean, SUMMER, SUNNY, 14:00 (Flounder, Halibut, Octopus, Pufferfish, Sardine, Super Cucumber, Crimsonfish)
    //     Player player3 = new Player("Cici", "Ocean", "Female", "OceanFarm");

    //     // Pemain 4: Pond, FALL, RAINY, 21:00 (Carp, Midnight Carp, Catfish, Angler)
    //     Player player4 = new Player("Dedi", "Pond", "Male", "PondFarm");

    //     // Pemain 5: Forest River, WINTER, SUNNY, 12:00 (Glacierfish)
    //     Player player5 = new Player("Eko", "Forest River", "Male", "WinterFarm");

    //     // Pemain 6: Forest River, SPRING, RAINY, 12:00 (Catfish)
    //     Player player6 = new Player("Fani", "Forest River", "Female", "SpringFarm");

    //     // Pemain 7: Forest River, SUMMER, SUNNY, 12:00 (Rainbow Trout)
    //     Player player7 = new Player("Gina", "Forest River", "Female", "SummerFarm");

    //     // Simulasi memancing untuk setiap pemain
    //     Player[] players = {player1, player2, player3, player4, player5, player6, player7};
    //     String[] playerNames = {"Asep", "Budi", "Cici", "Dedi", "Eko", "Fani", "Gina"};
    //     Object[][] playerConditions = {
    //         {Season.SPRING, Weather.RAINY, 10 * 60}, // Pemain 1
    //         {Season.FALL, Weather.SUNNY, 12 * 60},   // Pemain 2
    //         {Season.SUMMER, Weather.SUNNY, 14 * 60}, // Pemain 3
    //         {Season.FALL, Weather.RAINY, 21 * 60},   // Pemain 4
    //         {Season.WINTER, Weather.SUNNY, 12 * 60}, // Pemain 5
    //         {Season.SPRING, Weather.RAINY, 12 * 60}, // Pemain 6
    //         {Season.SUMMER, Weather.SUNNY, 12 * 60}  // Pemain 7
    //     };

    //     int currentPlayerIndex = 0;
    //     String action = "fishing"; // Mulai dengan fishing
    //     boolean newPlayer = true; // Flag untuk inisialisasi pemain

    //     while (true) {
    //         Player currentPlayer = players[currentPlayerIndex];

    //         // Cetak informasi pemain saat beralih ke pemain baru
    //         if (newPlayer) {
    //             System.out.println("\n=== Pemain: " + playerNames[currentPlayerIndex] + " ===");
    //             System.out.println("Lokasi: " + currentPlayer.getLocation());
    //             Season season = (Season) playerConditions[currentPlayerIndex][0];
    //             Weather weather = (Weather) playerConditions[currentPlayerIndex][1];
    //             int minutes = (int) playerConditions[currentPlayerIndex][2];
    //             System.out.println("Kondisi: " + season + ", " + weather + ", " + String.format("%02d:%02d", minutes / 60, minutes % 60));
    //             newPlayer = false;
    //         }

    //         if (action.equals("fishing")) {
    //             // Pause waktu dan atur ulang kondisi sebelum memancing
    //             calendar.pauseTime();
    //             Season season = (Season) playerConditions[currentPlayerIndex][0];
    //             Weather weather = (Weather) playerConditions[currentPlayerIndex][1];
    //             int minutes = (int) playerConditions[currentPlayerIndex][2];

    //             // Atur waktu, musim, cuaca menggunakan pemanggilan statis
    //             calendar.setTime(minutes);
    //             GameCalendar.setSeason(season); // Perbaikan: Gunakan pemanggilan statis
    //             GameCalendar.setWeather(weather); // Perbaikan: Gunakan pemanggilan statis

    //             // Tes memancing
    //             System.out.println("\n=== Tes Fishing ===");
    //             System.out.println("Energi awal: " + currentPlayer.getEnergy());
    //             System.out.print("Waktu awal: ");
    //             calendar.showTime();
    //             Fishing fishing = new Fishing(currentPlayer, calendar);
    //             Fish.FishType caughtFishType = fishing.execute(scanner);
    //             System.out.println("Energi setelah: " + currentPlayer.getEnergy());
    //             System.out.print("Waktu setelah: ");
    //             calendar.showTime();
    //             if (caughtFishType != null) {
    //                 System.out.println("Tipe ikan yang ditangkap: " + caughtFishType);
    //             }

    //             // Tampilkan inventory setelah memancing
    //             System.out.println("\n=== Inventory ===");
    //             currentPlayer.getInventory().getItemList();
    //         } else if (action.equals("inventory")) {
    //             System.out.println("\n=== Inventory ===");
    //             currentPlayer.getInventory().getItemList();
    //         } else if (action.equals("showtime")) {
    //             calendar.showTime();
    //         }

    //         // Lanjutkan waktu selama menunggu input
    //         calendar.startTime();
    //         System.out.println("Waktu berjalan normal selama menunggu keputusan...");
    //         System.out.print("\nPilih tindakan (fishing/inventory/showTime/[lain untuk ganti pemain atau keluar]): ");

    //         // Bersihkan buffer dan baca input
    //         String input = "";
    //         while (input.isEmpty()) {
    //             if (scanner.hasNextLine()) {
    //                 input = scanner.nextLine().trim().toLowerCase();
    //             }
    //         }
    //         action = input;

    //         if (action.equals("fishing") || action.equals("inventory") || action.equals("showtime")) {
    //             // Tetap dengan pemain saat ini
    //             if (action.equals("fishing")) {
    //                 calendar.pauseTime();
    //             }
    //         } else {
    //             // Ganti pemain atau keluar
    //             currentPlayerIndex++;
    //             if (currentPlayerIndex >= players.length) {
    //                 System.out.println("Program dihentikan. Terima kasih!");
    //                 timeThread.interrupt();
    //                 break;
    //             }
    //             action = "fishing"; // Mulai dengan fishing untuk pemain baru
    //             newPlayer = true; // Reset flag untuk pemain baru
    //             calendar.pauseTime();
    //         }
    //     }

    //     // Tutup scanner
    //     scanner.close();
    // }
}