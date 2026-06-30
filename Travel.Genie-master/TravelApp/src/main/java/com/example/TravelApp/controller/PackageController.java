package com.example.TravelApp.controller;

import com.example.TravelApp.model.Review;
import com.example.TravelApp.model.TourPackage;
import com.example.TravelApp.service.ReviewService;
import com.example.TravelApp.service.TourPackageService;
import com.example.TravelApp.service.WeatherService;
import com.example.TravelApp.service.WeatherSummary;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PackageController {

    private final TourPackageService tourPackageService;
    private final ReviewService reviewService;
    private final WeatherService weatherService;

    public PackageController(TourPackageService tourPackageService, ReviewService reviewService, WeatherService weatherService) {
        this.tourPackageService = tourPackageService;
        this.reviewService = reviewService;
        this.weatherService = weatherService;
    }

    @GetMapping("/packages")
    public String packages(Model model, @RequestParam(required = false) String category) {
        List<TourPackage> packages;
        if (category != null && !category.isBlank()) {
            packages = tourPackageService.findByCategory(category);
        } else {
            packages = tourPackageService.findAll();
        }
        model.addAttribute("packages", packages);
        return "packages";
    }

    @GetMapping("/packages/{id}")
    public String packageDetails(@PathVariable Long id, Model model) {
        TourPackage tourPackage = tourPackageService.findById(id).orElse(null);
        if (tourPackage == null) {
            return "redirect:/packages";
        }

        // 🎯 ඩේටාබේස් එකෙන් අලුත්ම රිවීව්ස් ටික Cache නොවී කෙලින්ම Filter කරලා ගන්නවා
        List<Review> reviews = reviewService.findAll().stream()
                .filter(r -> r.getTourPackage() != null && r.getTourPackage().getId().equals(id))
                .toList();

        String destinationName = tourPackage.getDestination() != null ? tourPackage.getDestination().getName() : null;
        String countryName = tourPackage.getDestination() != null ? tourPackage.getDestination().getCountry() : null;
        WeatherSummary weather = weatherService.getWeatherForDestination(destinationName, countryName);

        model.addAttribute("package", tourPackage);
        model.addAttribute("reviews", reviews); // 🎯 Thymeleaf එකට Fresh Reviews ලිස්ට් එක පාස් කළා
        model.addAttribute("weather", weather);

        return "package-details";
    }
}