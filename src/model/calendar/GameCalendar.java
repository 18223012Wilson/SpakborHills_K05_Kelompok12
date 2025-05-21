package model.calendar;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public GameCalendar() {
        this.time = LocalTime.of(23, 0);
        this.day = 1;
        this.season = Season.SPRING;
        this.weather = Weather.SUNNY;
        this.observers = new ArrayList<>();
        this.random = new Random();
        this.rainyDaysThisSeason = 0;
        this.isPaused = false;
    }

    public void updateTime() {
        if (isPaused) {
            return;
        }
        int currentMinutes = time.getHour() * 60 + time.getMinute();
        currentMinutes += 5;

        int daysPassed = currentMinutes / 1440;
        int remainingMinutes = currentMinutes % 1440;

        day += daysPassed;
        time = LocalTime.of(remainingMinutes / 60, remainingMinutes % 60);

        if (daysPassed > 0) {
            updateSeason();
            updateWeather();
        }
    }

    public void pauseTime() {
        isPaused = true;
    }

    public void startTime() {
        isPaused = false;
    }

    public void addTime(int minutes) {
        if (minutes < 0) {
            throw new IllegalArgumentException("Minutes cannot be negative");
        }
        int currentMinutes = time.getHour() * 60 + time.getMinute();
        currentMinutes += minutes;

        int daysPassed = currentMinutes / 1440;
        int remainingMinutes = currentMinutes % 1440;

        day += daysPassed;
        time = LocalTime.of(remainingMinutes / 60, remainingMinutes % 60);

        if (daysPassed > 0) {
            updateSeason();
            updateWeather();
        }
    }

    public void timeSkip(int targetHour) {
        if (targetHour < 0 || targetHour > 23) {
            throw new IllegalArgumentException("Target hour must be between 0-23");
        }
        time = LocalTime.of(targetHour, 0);
        if (time.isBefore(LocalTime.of(6, 0))) {
            day++;
            updateSeason();
            updateWeather();
        }
    }

    public String getFormattedTimeInfo() {
        String phase = (time.isAfter(LocalTime.of(5, 59)) && time.isBefore(LocalTime.of(18, 0))) ? "Day" : "Night";
        return String.format("Day %d, %02d:%02d, %s, %s, %s",
                day, time.getHour(), time.getMinute(), phase, season, weather);
    }


    private void updateSeason() {
        if (day % 10 == 1 && day > 1) {
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