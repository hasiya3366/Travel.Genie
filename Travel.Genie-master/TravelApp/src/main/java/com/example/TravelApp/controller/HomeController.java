package com.example.TravelApp.controller;

import com.example.TravelApp.model.Destination;
import com.example.TravelApp.model.TourPackage;
import com.example.TravelApp.service.DestinationService;
import com.example.TravelApp.service.TourPackageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final DestinationService destinationService;
    private final TourPackageService tourPackageService;

    public HomeController(DestinationService destinationService, TourPackageService tourPackageService) {
        this.destinationService = destinationService;
        this.tourPackageService = tourPackageService;
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        List<Destination> destinations = destinationService.findAll();
        List<TourPackage> packages = tourPackageService.findAll();
        model.addAttribute("destinations", destinations);
        model.addAttribute("packages", packages);
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

}
