package com.example.TravelApp.controller;

import com.example.TravelApp.model.Booking;
import com.example.TravelApp.model.Hotel;
import com.example.TravelApp.model.TourPackage;
import com.example.TravelApp.model.User;
import com.example.TravelApp.service.BookingService;
import com.example.TravelApp.service.HotelService;
import com.example.TravelApp.service.TourPackageService;
import com.example.TravelApp.service.UserService;
import com.example.TravelApp.service.PdfService; 

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class BookingController {

    private final BookingService bookingService;
    private final TourPackageService tourPackageService;
    private final UserService userService;
    private final HotelService hotelService;
    private final PdfService pdfService; 

    public BookingController(BookingService bookingService, TourPackageService tourPackageService,
                             UserService userService, HotelService hotelService, PdfService pdfService) {
        this.bookingService = bookingService;
        this.tourPackageService = tourPackageService;
        this.userService = userService;
        this.hotelService = hotelService;
        this.pdfService = pdfService;
    }

    // 🎯 පැකේජ් එකක් බුක් කරන මුල් පිටුව පෙන්වීම
    @GetMapping({"/book/{packageId}", "/book/{packageId}/"})
    public String bookPackage(@PathVariable Long packageId, Model model) {
        TourPackage tourPackage = tourPackageService.findById(packageId).orElse(null);
        if (tourPackage == null) {
            return "redirect:/packages";
        }
        model.addAttribute("package", tourPackage);
        model.addAttribute("booking", new Booking());
        return "book-package";
    }

    // 🎯 බුකින් එක Form එකෙන් සබ්මිට් කරද්දී මිල ගණන් හදලා සේဝ် කරන තැන
    @PostMapping("/book")
    public String placeBooking(@ModelAttribute Booking booking,
                               @RequestParam Long packageId,
                               @RequestParam String userEmail,
                               @RequestParam(required = false) Long hotelId,
                               @RequestParam(required = false, defaultValue = "1") Integer hotelNights,
                               @RequestParam(required = false) String foodSource,
                               @RequestParam(required = false) String outsideFoodType,
                               @RequestParam(required = false) String transportMode,
                               HttpServletRequest request,
                               Model model) {

        Optional<TourPackage> optionalPackage = tourPackageService.findById(packageId);
        Optional<User> optionalUser = userService.findByEmail(userEmail);
        if (optionalPackage.isEmpty()) {
            return "redirect:/packages";
        }
        TourPackage tourPackage = optionalPackage.get();

        if (optionalUser.isEmpty()) {
            model.addAttribute("error", "User not found. Please register or use a valid email.");
            model.addAttribute("package", tourPackage);
            model.addAttribute("booking", booking == null ? new Booking() : booking);
            return "book-package";
        }
        User user = optionalUser.get();
        booking.setTourPackage(tourPackage);
        booking.setUser(user);

        double basePrice = tourPackage.getPrice();
        double extraPricePerNight = 0.0;
        String selectedHotelName = "No Hotel";

        if (hotelId != null) {
            Optional<Hotel> optionalHotel = hotelService.findById(hotelId);
            if (optionalHotel.isPresent()) {
                Hotel hotel = optionalHotel.get();
                extraPricePerNight = hotel.getExtraPrice();
                selectedHotelName = hotel.getName();
            }
        }

        int days = (hotelNights != null && hotelNights > 0) ? hotelNights : 1;
        double totalFoodCost = 0.0;

        if (hotelId != null) {
            if ("hotel".equals(foodSource)) {
                for (int i = 1; i <= days; i++) {
                    String breakfast = request.getParameter("day-" + i + "-breakfast");
                    String lunch = request.getParameter("day-" + i + "-lunch");
                    String dinner = request.getParameter("day-" + i + "-dinner");

                    if (breakfast != null) {
                        if ("local".equals(breakfast)) totalFoodCost += 150.00;
                        else if ("western".equals(breakfast)) totalFoodCost += 450.00;
                        else if ("outside".equals(breakfast)) totalFoodCost += 100.00;
                    }
                    if (lunch != null) {
                        if ("local".equals(lunch)) totalFoodCost += 200.00;
                        else if ("western".equals(lunch)) totalFoodCost += 600.00;
                        else if ("outside".equals(lunch)) totalFoodCost += 100.00;
                    }
                    if (dinner != null) {
                        if ("local".equals(dinner)) totalFoodCost += 250.00;
                        else if ("western".equals(dinner)) totalFoodCost += 550.00;
                        else if ("outside".equals(dinner)) totalFoodCost += 100.00;
                    }
                }
            } else if ("outside".equals(foodSource) && outsideFoodType != null) {
                double outsidePricePerDay = 0.0;
                switch (outsideFoodType) {
                    case "delivery" -> outsidePricePerDay = 250.00;
                    case "guide" -> outsidePricePerDay = 400.00;
                    default -> outsidePricePerDay = 0.00;
                }
                totalFoodCost = outsidePricePerDay * days;
            }
        }

        double transPrice = 0.0;
        String selectedVehicle = "No Vehicle";
        if (transportMode != null && hotelId != null) {
            switch (transportMode) {
                case "bike" -> { transPrice = 1000.00; selectedVehicle = "Bike"; }
                case "threewheel" -> { transPrice = 1500.00; selectedVehicle = "Three-Wheel"; }
                case "car" -> { transPrice = 3500.00; selectedVehicle = "Car"; }
            }
        }

        double extraHotelAndTransCost = (extraPricePerNight + transPrice) * booking.getTravelers() * days;
        double extraFoodCostTotal = totalFoodCost * booking.getTravelers();
        double totalPrice = (basePrice * booking.getTravelers()) + extraHotelAndTransCost + extraFoodCostTotal;

        booking.setTotalPrice(totalPrice);
        bookingService.save(booking);

        model.addAttribute("booking", booking);
        model.addAttribute("hotelName", selectedHotelName);
        model.addAttribute("vehicle", selectedVehicle);
        model.addAttribute("hotelNights", days);

        return "booking-confirmation";
    }

   @GetMapping("/book/payment")
    public String showPaymentPage() {
        // කිසිම ඩේටා එකක් චෙක් කරන්නේ නැහැ, කෙළින්ම පේජ් එක විතරක් ඇරලා දෙනවා
        return "book/payment"; 
    }

    // 📄 INVOICE PDF DOWNLOAD ENDPOINT
    @GetMapping("/booking/download-receipt")
    public ResponseEntity<InputStreamResource> downloadReceipt(
            @RequestParam String email,
            @RequestParam String date,
            @RequestParam String travelers,
            @RequestParam String packageName,
            @RequestParam String hotelName,
            @RequestParam String vehicle,
            @RequestParam String totalPrice) {

        Map<String, Object> data = new HashMap<>();
        data.put("email", email);
        data.put("date", date);
        data.put("travelers", travelers);
        data.put("packageName", packageName);
        data.put("hotelName", hotelName);
        data.put("vehicle", vehicle);
        data.put("totalPrice", totalPrice);

        ByteArrayInputStream bis = pdfService.generateBookingReceipt(data);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=TravelApp_Receipt.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    // 🎯 යූසර්ගේ පැරණි Booking History පෙන්වන පිටුව
    @GetMapping("/bookings")
    public String bookingHistory(@RequestParam String userEmail, Model model) {
        Optional<User> user = userService.findByEmail(userEmail);
        if (user.isEmpty()) {
            model.addAttribute("bookings", List.of());
            model.addAttribute("userEmail", userEmail);
            return "booking-history";
        }
        List<Booking> bookings = bookingService.findByUser(user.get());
        model.addAttribute("bookings", bookings);
        model.addAttribute("userEmail", userEmail);
        return "booking-history";
    }
}
