package com.example.TravelApp.controller;

import com.example.TravelApp.model.Destination;
import com.example.TravelApp.service.DestinationService;
import com.example.TravelApp.service.WeatherService; // 🎯 1. WeatherService එක Import කළා
import com.example.TravelApp.service.WeatherSummary;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class DestinationController {

    private final DestinationService destinationService;
    private final WeatherService weatherService; // 🎯 2. WeatherService එක Inject කරන්න Field එකක් හැදුවා

    // 🎯 3. Constructor එක ඇතුළට WeatherService එකත් ඇතුළත් කළා
    public DestinationController(DestinationService destinationService, WeatherService weatherService) {
        this.destinationService = destinationService;
        this.weatherService = weatherService;
    }

    // 🗺️ ඔයාගේ පරණ සියලුම Destinations පෙන්වන සහ Filter කරන මෙතඩ් එක (එලෙසම පවතී)
    @GetMapping("/destinations")
    public String destinations(Model model,
                               @RequestParam(required = false) String country,
                               @RequestParam(required = false) String category,
                               @RequestParam(required = false) Double minPrice,
                               @RequestParam(required = false) Double maxPrice) {
        List<Destination> destinations;

        if (country != null && !country.isBlank()) {
            destinations = destinationService.findByCountry(country);
        } else if (category != null && !category.isBlank()) {
            destinations = destinationService.findByCategory(category);
        } else if (minPrice != null && maxPrice != null) {
            destinations = destinationService.findByPriceRange(minPrice, maxPrice);
        } else {
            destinations = destinationService.findAll();
        }

        model.addAttribute("destinations", destinations);
        return "destinations";
    }

    // 🎯 4. තනි ලොකේෂන් එකක විස්තර සහ WEATHER එක පෙන්වන අලුත්ම මෙතඩ් එක
    @GetMapping("/destination/{id}")
    public String viewDestination(@PathVariable Long id, Model model) {
        Optional<Destination> optionalDestination = destinationService.findById(id);

        if (optionalDestination.isEmpty()) {
            return "redirect:/destinations"; // ලොකේෂන් එක නැත්නම් පරණ ලිස්ට් එකට හරවනවා
        }
        Destination destination = optionalDestination.get();

        // 🌤️ ඩේටාබේස් එකෙන් එන ලොකේෂන් නම අනුව Weather විස්තර ටික අපේ සර්විස් එකෙන් ගන්නවා
        WeatherSummary weather = weatherService.getWeatherForDestination(destination.getName(), "Sri Lanka");

        model.addAttribute("destination", destination);
        model.addAttribute("weather", weather); // 🚀 HTML එකට Weather දත්ත ටික පාස් කළා

        return "destination-details"; // බ්‍රවුසර් එකෙන් ලෝඩ් වෙන්න ඕන HTML පිටුවේ නම
    }
}