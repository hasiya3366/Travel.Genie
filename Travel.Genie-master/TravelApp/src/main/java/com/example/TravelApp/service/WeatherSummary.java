package com.example.TravelApp.service;

public class WeatherSummary {
    private final String locationName;
    private final String countryName;
    private final String temperature;
    private final String condition;
    private final String summary;
    private final String icon;

    public WeatherSummary(String locationName, String countryName, String temperature, String condition, String summary, String icon) {
        this.locationName = locationName;
        this.countryName = countryName;
        this.temperature = temperature;
        this.condition = condition;
        this.summary = summary;
        this.icon = icon;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getCondition() {
        return condition;
    }

    public String getSummary() {
        return summary;
    }

    public String getIcon() {
        return icon;
    }
}
