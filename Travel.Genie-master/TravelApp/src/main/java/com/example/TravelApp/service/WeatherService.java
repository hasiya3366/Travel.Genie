package com.example.TravelApp.service;

import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class WeatherService {

    public WeatherSummary getWeatherForDestination(String destinationName, String countryName) {
        if (destinationName == null || destinationName.isBlank()) {
            return getFallbackWeather("Nearby destination", "TravelApp");
        }

        String normalizedName = destinationName.trim();
        String normalizedCountry = countryName == null || countryName.isBlank() ? "Worldwide" : countryName.trim();

        return buildWeatherSummary(normalizedName, normalizedCountry);
    }

    public WeatherSummary getFallbackWeather(String destinationName, String countryName) {
        return buildWeatherSummary(destinationName, countryName);
    }

    private WeatherSummary buildWeatherSummary(String destinationName, String countryName) {
        String normalizedName = destinationName == null || destinationName.isBlank() ? "Nearby destination" : destinationName.trim();
        String normalizedCountry = countryName == null || countryName.isBlank() ? "Worldwide" : countryName.trim();

        String condition = pickCondition(normalizedName.toLowerCase(Locale.ROOT));
        String temperature = pickTemperature(normalizedName.toLowerCase(Locale.ROOT));
        String summary = pickSummary(condition);
        String icon = pickIcon(condition);

        return new WeatherSummary(normalizedName, normalizedCountry, temperature, condition, summary, icon);
    }

    private String pickCondition(String normalizedName) {
        if (normalizedName.contains("sri") || normalizedName.contains("sigiriya") || normalizedName.contains("kandy") || normalizedName.contains("galle")) {
            return "Sunny";
        }
        if (normalizedName.contains("maldives") || normalizedName.contains("bali") || normalizedName.contains("paris") || normalizedName.contains("switzerland")) {
            return "Clear";
        }
        if (normalizedName.contains("switzerland") || normalizedName.contains("alps") || normalizedName.contains("himalaya") || normalizedName.contains("new york")) {
            return "Cloudy";
        }
        if (normalizedName.contains("amazon") || normalizedName.contains("rain") || normalizedName.contains("forest")) {
            return "Rainy";
        }
        return "Pleasant";
    }

    private String pickTemperature(String normalizedName) {
        if (normalizedName.contains("sri") || normalizedName.contains("sigiriya") || normalizedName.contains("kandy") || normalizedName.contains("galle")) {
            return "31°C";
        }
        if (normalizedName.contains("maldives") || normalizedName.contains("bali")) {
            return "29°C";
        }
        if (normalizedName.contains("switzerland") || normalizedName.contains("alps") || normalizedName.contains("paris")) {
            return "18°C";
        }
        if (normalizedName.contains("amazon") || normalizedName.contains("rain") || normalizedName.contains("forest")) {
            return "24°C";
        }
        return "26°C";
    }

    private String pickSummary(String condition) {
        return switch (condition) {
            case "Sunny" -> "Bright skies and warm temperatures make this a perfect day for outdoor exploration.";
            case "Clear" -> "Expect crisp air and excellent visibility for sightseeing and beach time.";
            case "Cloudy" -> "Cooler air with scattered clouds makes it comfortable for city tours.";
            case "Rainy" -> "Light showers may affect outdoor plans, so keep a light jacket handy.";
            default -> "Mild conditions are ideal for relaxed travel plans and walking tours.";
        };
    }

    private String pickIcon(String condition) {
        return switch (condition) {
            case "Sunny" -> "☀️";
            case "Clear" -> "🌤️";
            case "Cloudy" -> "☁️";
            case "Rainy" -> "🌧️";
            default -> "🌦️";
        };
    }
}
