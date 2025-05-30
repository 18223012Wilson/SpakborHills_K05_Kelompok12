package model.calendar;

public interface WeatherObserver {
    void onWeatherChange(Weather weather);
}