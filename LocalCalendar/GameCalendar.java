/*
 * Tolong DIBACAAAAAAAA
 * 
 * JIka mau melakukan CHEAT supaya bisa tau apakah ada bug atau tidak
 * gunakan perintah "add <menit>" (misal "add 360").;
 * 
 * FUNGSI DISNI YANG BISA DIGUNAKAN: -pause time
 * -start time
 * -add time <menit>
 * -time skip <jam>
 * -show time
 * -exit
 * 
 * 
 */

package LocalCalendar;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import LocalCalendar.*;
import java.util.Scanner;




// Interface untuk Observer Pattern
interface WeatherObserver {
    void onWeatherChange(Weather weather);
}

public class GameCalendar {
    private LocalTime time;
    private int day;
    private Season season;
    private Weather weather;
    private List<WeatherObserver> observers;
    private Random random;
    private int rainyDaysThisSeason;
    private boolean isPaused;

    // Constructor
    public GameCalendar() {
        this.time = LocalTime.of(6, 0); // Mulai 06:00
        this.day = 1;
        this.season = Season.SPRING;
        this.weather = Weather.SUNNY;
        this.observers = new ArrayList<>();
        this.random = new Random();
        this.rainyDaysThisSeason = 0;
        this.isPaused = false;
    }

    // Update waktu: 1 detik nyata = 5 menit in-game
    public void updateTime() {
        if (isPaused) {
            return;
        }
        // Hitung menit total dari 00:00
        int currentMinutes = time.getHour() * 60 + time.getMinute();
        currentMinutes += 5; // 1 detik = 5 menit (untuk tes cheat, gunakan addTime)

        // Hitung hari dan sisa menit
        int daysPassed = currentMinutes / 1440; // 1 hari = 1440 menit
        int remainingMinutes = currentMinutes % 1440;

        // Update hari
        day += daysPassed;

        // Set waktu baru
        time = LocalTime.of(remainingMinutes / 60, remainingMinutes % 60);

        // Update musim/cuaca jika hari bertambah
        if (daysPassed > 0) {
            updateSeason();
            updateWeather();
        }
    }

    // Hentikan jalannya waktu
    public void pauseTime() {
        isPaused = true;
    }

    // Lanjutkan jalannya waktu
    public void startTime() {
        isPaused = false;
    }

    // Tambah waktu (dalam menit)
    public void addTime(int minutes) {
        if (minutes < 0) {
            throw new IllegalArgumentException("Menit tidak boleh negatif");
        }
        // Hitung menit total dari 00:00
        int currentMinutes = time.getHour() * 60 + time.getMinute();
        currentMinutes += minutes;

        // Hitung hari dan sisa menit
        int daysPassed = currentMinutes / 1440;
        int remainingMinutes = currentMinutes % 1440;

        // Update hari
        day += daysPassed;

        // Set waktu baru
        time = LocalTime.of(remainingMinutes / 60, remainingMinutes % 60);

        // Update musim/cuaca jika hari bertambah
        if (daysPassed > 0) {
            updateSeason();
            updateWeather();
        }
    }

    // Lompat ke jam tertentu di hari berikutnya
    public void timeSkip(int targetHour) {
        if (targetHour < 0 || targetHour > 23) {
            throw new IllegalArgumentException("Jam tujuan harus antara 0-23");
        }
        time = LocalTime.of(targetHour, 0);
        if (time.isBefore(LocalTime.of(6, 0))) {
            day++;
            updateSeason();
            updateWeather();
        }
    }

    // Tampilkan waktu, hari, fase, musim, cuaca
    public void showTime() {
        String phase = (time.isAfter(LocalTime.of(5, 59)) && time.isBefore(LocalTime.of(18, 0))) ? "Siang" : "Malam";
        System.out.printf("Hari %d, %02d:%02d, %s, %s, %s%n",
                day, time.getHour(), time.getMinute(), phase, season, weather);
    }

    // Update musim: ganti setiap 10 hari
    private void updateSeason() {
        if (day % 10 == 1 && day > 1) { // Hari 1 musim baru
            rainyDaysThisSeason = 0;
            switch (season) {
                case SPRING:
                    season = Season.SUMMER;
                    break;
                case SUMMER:
                    season = Season.FALL;
                    break;
                case FALL:
                    season = Season.WINTER;
                    break;
                case WINTER:
                    season = Season.SPRING;
                    break;
            }
        }
    }

    // Update cuaca: sekali per hari, minimal 2 hujan per musim
    private void updateWeather() {
        if (day % 10 >= 8 && rainyDaysThisSeason < 2) {
            weather = Weather.RAINY;
            rainyDaysThisSeason++;
        } else {
            weather = random.nextDouble() < 0.6 ? Weather.SUNNY : Weather.RAINY;
            if (weather == Weather.RAINY) {
                rainyDaysThisSeason++;
            }
        }
        notifyObservers();
    }

    // Tambah observer
    public void addObserver(WeatherObserver observer) {
        observers.add(observer);
    }

    // Notify observer saat cuaca berubah
    private void notifyObservers() {
        for (WeatherObserver observer : observers) {
            observer.onWeatherChange(weather);
        }
    }

    // Getter
    public LocalTime getTime() {
        return time;
    }

    public int getDay() {
        return day;
    }

    public Season getSeason() {
        return season;
    }

    public Weather getWeather() {
        return weather;
    }
    // // // Main untuk test apakah berhasil atau tidak & sudah bisa
    // //     public static void main(String[] args) {
    // //         GameCalendar calendar = new GameCalendar();
    // //         Scanner scanner = new Scanner(System.in);

    // //         // Thread untuk simulasi waktu (latar belakang)
    // //         Thread timeThread = new Thread(() -> {
    // //             try {
    // //                 while (!Thread.currentThread().isInterrupted()) {
    // //                     // Update waktu tanpa print
    // //                     calendar.updateTime();
    // //                     // Tunggu 1 detik nyata
    // //                     Thread.sleep(1000);
    // //                 }
    // //             } catch (InterruptedException e) {
    // //                 System.out.println("Simulasi waktu dihentikan.");
    // //             }
    // //         });

    // //         // Thread untuk membaca input
    // //         Thread inputThread = new Thread(() -> {
    // //             try {
    // //                 System.out.println("Masukkan perintah (show, pause, start, skip <jam>, add <menit>, exit):");
    // //                 while (!Thread.currentThread().isInterrupted()) {
    // //                     // Baca input
    // //                     System.out.print("Perintah: ");
    // //                     String input = scanner.nextLine().trim().toLowerCase();

    // //                     // Handle berbagai input
    // //                     try {
    // //                         if (input.equals("show")) {
    // //                             calendar.showTime();
    // //                         } else if (input.equals("pause")) {
    // //                             calendar.pauseTime();
    // //                             System.out.println("Waktu dijeda!");
    // //                             calendar.showTime();
    // //                         } else if (input.equals("start")) {
    // //                             calendar.startTime();
    // //                             System.out.println("Waktu dilanjutkan!");
    // //                             calendar.showTime();
    // //                         } else if (input.startsWith("skip ")) {
    // //                             int hour = Integer.parseInt(input.substring(5).trim());
    // //                             calendar.timeSkip(hour);
    // //                             System.out.println("Waktu dilompati ke " + hour + ":00!");
    // //                             calendar.showTime();
    // //                         } else if (input.startsWith("add ")) {
    // //                             int minutes = Integer.parseInt(input.substring(4).trim());
    // //                             calendar.addTime(minutes);
    // //                             System.out.println("Waktu ditambah " + minutes + " menit!");
    // //                             calendar.showTime();
    // //                         } else if (input.equals("exit")) {
    // //                             System.out.println("Menghentikan program...");
    // //                             timeThread.interrupt();
    // //                             Thread.currentThread().interrupt();
    // //                         } else {
    // //                             System.out.println("Perintah tidak valid. Gunakan: show, pause, start, skip <jam>, add <menit>, exit");
    // //                         }
    // //                     } catch ( IllegalArgumentException e) {
    // //                         System.out.println("Input tidak valid: " + e.getMessage());
    // //                     }
    // //                 }
    // //             } catch (Exception e) {
    // //                 System.out.println("Input thread dihentikan: " + e.getMessage());
    // //             } finally {
    // //                 scanner.close();
    // //             }
    // //         });

    // //         // Mulai kedua thread
    // //         timeThread.start();
    // //         inputThread.start();

    // //         // Tunggu thread selesai
    // //         try {
    // //             timeThread.join();
    // //             inputThread.join();
    // //         } catch (InterruptedException e) {
    // //             System.out.println("Program dihentikan.");
    // //         }
    // //     }
    // }
}