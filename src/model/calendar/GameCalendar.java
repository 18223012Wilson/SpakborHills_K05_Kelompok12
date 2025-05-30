package model.calendar;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.map.FarmMap;
import model.entitas.Player;
import model.items.Point;
import main.Main;


public class GameCalendar implements Runnable {
    private LocalTime time;
    private int day;
    private Season season;
    private Weather weather;
    private List<WeatherObserver> observers;
    private Random random;
    private int rainyDaysThisSeason;
    private boolean isPaused;
    private volatile boolean running = true;
    private FarmMap farmMap;
    private Player player;
    private Main mainFrame;

    public GameCalendar(FarmMap farmMap, Player player) {
        this.time = LocalTime.of(6, 0);
        this.day = 1;
        this.season = Season.SPRING;
        this.observers = new ArrayList<>();
        this.random = new Random();
        this.rainyDaysThisSeason = 0;
        this.isPaused = false;
        this.farmMap = farmMap;
        this.player = player;
        updateWeather();
    }

    private void advanceDay() {
        day++;
        Player.incrementTotalDaysPlayed();

        String revenueMessage = player.collectShippingBinRevenue();
        if (revenueMessage != null && !revenueMessage.isEmpty()) {
            if (mainFrame != null && mainFrame.getGameScreenPanel() != null) {
                mainFrame.getGameScreenPanel().showMessage(revenueMessage);
            }
            else {
                System.out.println(revenueMessage);
            }
        }

        updateSeason();
        updateWeather();
        if (farmMap != null) {
            farmMap.dailyPlantUpdate(this);
        }
    }

    private void handlePassOut(String passOutReason) {
        player.sleep();
        advanceDay();
        this.time = LocalTime.of(6, 0);

        if (mainFrame != null) {
            mainFrame.switchToFarmMap();
            FarmMap currentFarmMap = mainFrame.getFarmMap();

            Point playerSpawnPoint = new Point(currentFarmMap.getHouseCol() + FarmMap.HOUSE_WIDTH / 2, currentFarmMap.getHouseRow() + FarmMap.HOUSE_HEIGHT);

            if (playerSpawnPoint.getY() >= FarmMap.MAP_SIZE || playerSpawnPoint.getX() >= FarmMap.MAP_SIZE ||
                    currentFarmMap.getTileType(playerSpawnPoint.getY(), playerSpawnPoint.getX()) != '.') {
                int doorCol = currentFarmMap.getHouseCol() + 2;
                int doorRowBelow = currentFarmMap.getHouseRow() + FarmMap.HOUSE_HEIGHT;
                if (doorRowBelow < FarmMap.MAP_SIZE && currentFarmMap.getTileType(doorRowBelow, doorCol) == '.') {
                    playerSpawnPoint = new Point(doorCol, doorRowBelow);
                }
                else {
                    playerSpawnPoint = new Point(1,1);
                    if(currentFarmMap.getTileType(1,1) != '.') currentFarmMap.setTileType(1,1,'.');
                }
            }
            player.moveTo(playerSpawnPoint);
            player.setLocation("Farm");

            if (mainFrame.getGameScreenPanel() != null) {
                mainFrame.getGameScreenPanel().showMessage(passOutReason);
                mainFrame.getGameScreenPanel().repaint();
            }
            else {
                System.out.println(passOutReason);
            }
        }
    }


    public void updateTime() {
        if (isPaused) {
            return;
        }
        LocalTime previousTime = this.time;
        this.time = this.time.plusMinutes(5);

        if (this.time.getHour() == 2 && this.time.getMinute() == 0 &&
                previousTime.isBefore(LocalTime.of(2,0)) ) {
            handlePassOut("Waduh, saking asyiknya kerja, kamu ketiduran di jalan! Untung ada yang nolongin pulang. Bangun-bangun udah jam 6 pagi di depan rumah.");
        }
    }

    public void performSleepAndAdvanceDay() {
        player.sleep();
        advanceDay();
        this.time = LocalTime.of(6,0);

        if (mainFrame != null) {
            mainFrame.switchToFarmMap();
            FarmMap currentFarmMap = mainFrame.getFarmMap();
            Point playerSpawnPoint = new Point(currentFarmMap.getHouseCol() + FarmMap.HOUSE_WIDTH / 2, currentFarmMap.getHouseRow() + FarmMap.HOUSE_HEIGHT);

            if (playerSpawnPoint.getY() >= FarmMap.MAP_SIZE || playerSpawnPoint.getX() >= FarmMap.MAP_SIZE ||
                    currentFarmMap.getTileType(playerSpawnPoint.getY(), playerSpawnPoint.getX()) != '.') {
                int doorCol = currentFarmMap.getHouseCol() + 2;
                int doorRowBelow = currentFarmMap.getHouseRow() + FarmMap.HOUSE_HEIGHT;
                if (doorRowBelow < FarmMap.MAP_SIZE && currentFarmMap.getTileType(doorRowBelow, doorCol) == '.') {
                    playerSpawnPoint = new Point(doorCol, doorRowBelow);
                }
                else {
                    playerSpawnPoint = new Point(1,1);
                    if(currentFarmMap.getTileType(1,1) != '.') currentFarmMap.setTileType(1,1,'.');
                }
            }
            player.moveTo(playerSpawnPoint);
            player.setLocation("Farm");

            if (mainFrame.getGameScreenPanel() != null) {
                mainFrame.getGameScreenPanel().showMessage("Istirahat yang nyenyak! Hari baru ("+ this.day + ") telah dimulai pukul 06:00.");
                mainFrame.getGameScreenPanel().repaint();
            }
        }
    }

    public void pauseTime() {
        isPaused = true;
    }

    public void startTime() {
        isPaused = false;
    }

    public void addTime(int minutes) {
        if (minutes <= 0) return;

        LocalTime timeBeforeAdd = this.time;
        this.time = this.time.plusMinutes(minutes);
        boolean passedOutThisAction = false;

        if (timeBeforeAdd.isBefore(LocalTime.of(2,0))) {
            if (this.time.equals(LocalTime.of(2,0)) ||
                    (this.time.isAfter(LocalTime.of(2,0)) && !timeBeforeAdd.isAfter(LocalTime.of(2,0)) && !this.time.isBefore(timeBeforeAdd) ) ||
                    (this.time.isAfter(LocalTime.of(2,0)) && this.time.isBefore(LocalTime.of(6,0)) && this.time.isBefore(timeBeforeAdd) )
            ) {
                if(this.time.getHour() >= 2 && this.time.getHour() < 6) {
                    handlePassOut("Kamu terlalu larut bekerja dan akhirnya ketiduran! Besok pagi kamu bangun di rumah.");
                    passedOutThisAction = true;
                }
            }
        }

        if (!passedOutThisAction && this.time.getHour() >= 2 && this.time.getHour() < 6) {
            handlePassOut("Malam semakin larut dan kamu akhirnya tumbang karena kelelahan. Kamu bangun di rumah keesokan paginya.");
        }
    }


    public void setMainFrame(Main mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void timeSkip(int targetHour, int targetMinute) {
        if (targetHour < 0 || targetHour > 23 || targetMinute < 0 || targetMinute > 59) {
            throw new IllegalArgumentException("Invalid target time for skip.");
        }
        if (isPaused) return;

        LocalTime targetTime = LocalTime.of(targetHour, targetMinute);
        boolean dayAdvancedDueToSkipLogic = false;

        if (targetTime.isBefore(this.time) || targetTime.equals(this.time)) {
            player.sleep();
            advanceDay();
            dayAdvancedDueToSkipLogic = true;
        }
        this.time = targetTime;

        boolean passedOutAfterSkip = false;
        if (this.time.getHour() >= 2 && this.time.getHour() < 6) {
            String passOutMsg = "Kamu mencoba melewati waktu hingga sangat larut dan akhirnya pingsan. Kamu bangun di rumah keesokan paginya pukul 06:00.";
            if (dayAdvancedDueToSkipLogic) {
                this.time = LocalTime.of(6,0);
                if (mainFrame != null && mainFrame.getGameScreenPanel() != null) {
                    mainFrame.getGameScreenPanel().showMessage(passOutMsg);
                }
            }
            else {
                handlePassOut(passOutMsg);
            }
            passedOutAfterSkip = true;
        }

        if (!passedOutAfterSkip && mainFrame != null && mainFrame.getGameScreenPanel() != null) {
            mainFrame.getGameScreenPanel().showMessage("Waktu telah berlalu hingga " + String.format("%02d:%02d", this.time.getHour(), this.time.getMinute()));
        }
    }

    public String getFormattedTimeInfo() {
        String phase = (time.isAfter(LocalTime.of(5, 59)) && time.isBefore(LocalTime.of(18, 0))) ? "Siang" : "Malam";
        return String.format("Hari %d, %02d:%02d (%s) - Musim: %s, Cuaca: %s",
                day, time.getHour(), time.getMinute(), phase, season.toString(), weather.toString());
    }

    private void updateSeason() {
        if (day > 1 && (day - 1) % 10 == 0) {
            rainyDaysThisSeason = 0;
            Season oldSeason = season;
            switch (season) {
                case SPRING: season = Season.SUMMER; break;
                case SUMMER: season = Season.FALL; break;
                case FALL:   season = Season.WINTER; break;
                case WINTER: season = Season.SPRING; break;
            }
        }
    }

    private void updateWeather() {
        int daysLeftInSeason = 10 - ((day - 1) % 10);
        int rainyDaysNeeded = 2 - rainyDaysThisSeason;

        if (rainyDaysNeeded > 0 && daysLeftInSeason <= rainyDaysNeeded) {
            weather = Weather.RAINY;
        } else {
            weather = random.nextDouble() < 0.70 ? Weather.SUNNY : Weather.RAINY;
        }

        if (weather == Weather.RAINY) {
            rainyDaysThisSeason++;
        }
        notifyObservers();
    }

    public void addObserver(WeatherObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        for (WeatherObserver observer : observers) {
            observer.onWeatherChange(weather);
        }
    }

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
    public boolean isPaused() {
        return isPaused;
    }
    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            if (!isPaused) {
                updateTime();
                if (mainFrame != null && mainFrame.getStatusBarPanel() != null) {
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                running = false;
            }
        }
    }
}