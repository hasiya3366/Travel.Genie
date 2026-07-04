package com.example.TravelApp.controller;

import com.example.TravelApp.model.*;
import com.example.TravelApp.repository.BookingRepository;
import com.example.TravelApp.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final DestinationService destinationService;
    private final TourPackageService tourPackageService;
    private final BookingService bookingService;
    private final ReviewService reviewService;
    private final HotelService hotelService; 
    private final BookingRepository bookingRepository;

    public AdminController(UserService userService, DestinationService destinationService,
                           TourPackageService tourPackageService, BookingService bookingService,
                           ReviewService reviewService, HotelService hotelService, BookingRepository bookingRepository) {
        this.userService = userService;
        this.destinationService = destinationService;
        this.tourPackageService = tourPackageService;
        this.bookingService = bookingService;
        this.reviewService = reviewService;
        this.hotelService = hotelService;
        this.bookingRepository = bookingRepository;
    }

    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("userRole"));
    }

    @GetMapping
    public String dashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        List<Booking> allBookingsList = bookingService.findAll();
        
        // Active Processed Orders Count (Excluding CANCELLED)
        long activeBookingsCount = allBookingsList.stream()
                .filter(b -> !"CANCELLED".equalsIgnoreCase(b.getStatus()))
                .count();

        // Counter Parameter Mapping Workspace
        model.addAttribute("totalUsers", userService.findAll().size());
        model.addAttribute("totalDestinations", destinationService.findAll().size());
        model.addAttribute("totalPackages", tourPackageService.findAll().size());
        model.addAttribute("totalBookings", activeBookingsCount); // Mapped to active orders only
        
        // Accurate total revenue calculation excluding cancelled metrics
        Double revenue = bookingRepository.getTotalRevenue();
        model.addAttribute("totalRevenue", revenue != null ? revenue : 0.0);
        
        // Render tour packages dataset layout data matrix
        List<TourPackage> allPackages = tourPackageService.findAll();
        model.addAttribute("bookings", allPackages); 

        return "admin/dashboard";
    }

    // 🎯 NEW: LIVE AUTO-UPDATE REFRESH API INTEGRATION WITH CANCELLED COUNTER MATRIX
    @GetMapping("/api/dashboard-stats")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getLiveDashboardStats(HttpSession session) {
        Map<String, Object> stats = new HashMap<>();
        
        if (!isAdmin(session)) {
            stats.put("error", "Unauthorized access configuration mapping.");
            return ResponseEntity.status(403).body(stats);
        }

        List<Booking> allBookingsList = bookingService.findAll();
        
        // Count Active processed orders (Excluding CANCELLED)
        long activeBookingsCount = allBookingsList.stream()
                .filter(b -> !"CANCELLED".equalsIgnoreCase(b.getStatus()))
                .count();

        // Count Cancelled orders explicitly for the Premium Bar Chart Matrix
        long cancelledBookingsCount = allBookingsList.stream()
                .filter(b -> "CANCELLED".equalsIgnoreCase(b.getStatus()))
                .count();

        Double revenue = bookingRepository.getTotalRevenue();

        // Build Payload Map Response
        stats.put("totalUsers", userService.findAll().size());
        stats.put("totalDestinations", destinationService.findAll().size());
        stats.put("totalPackages", tourPackageService.findAll().size());
        stats.put("totalBookings", activeBookingsCount); 
        stats.put("totalCancelled", cancelledBookingsCount); // Mapped for Chart option 1
        stats.put("totalRevenue", revenue != null ? revenue : 0.0);

        return ResponseEntity.ok(stats);
    }

    // ==========================================
    // HOTELS MANAGEMENT SECTION
    // ==========================================

    @GetMapping("/hotels/add")
    public String showAddHotelForm(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("hotel", new Hotel());
        model.addAttribute("packages", tourPackageService.findAll()); 
        return "add-hotel"; 
    }

    @PostMapping("/hotels/add")
    public String saveHotel(@ModelAttribute Hotel hotel, @RequestParam Long packageId, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        TourPackage tourPackage = tourPackageService.findById(packageId).orElse(null);
        if (tourPackage != null) {
            hotel.setTourPackage(tourPackage);
            hotelService.save(hotel);
        }
        return "redirect:/admin/packages"; 
    }

    // ==========================================
    // USERS SECTION
    // ==========================================

    @GetMapping("/users")
    public String listUsers(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("users", userService.findAll());
        return "admin/users-list";
    }

    @GetMapping("/users/add")
    public String addUserForm(HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        return "admin/users-form";
    }

    @PostMapping("/users/add")
    public String addUser(@RequestParam String name, @RequestParam String email,
                          @RequestParam String password, @RequestParam String role,
                          @RequestParam String phone, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        User user = new User(name, email, password, role);
        user.setPhone(phone);
        userService.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        var user = userService.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "admin/users-form";
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id, @RequestParam String name,
                           @RequestParam String email, @RequestParam String role,
                           @RequestParam String phone, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        var user = userService.findById(id);
        if (user.isPresent()) {
            User u = user.get();
            u.setName(name);
            u.setEmail(email);
            u.setRole(role);
            u.setPhone(phone);
            userService.save(u);
        }
        return "redirect:/admin/users";
    }

    // ==========================================
    // DESTINATIONS SECTION
    // ==========================================

    @GetMapping("/destinations")
    public String listDestinations(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("destinations", destinationService.findAll());
        return "admin/destinations-list";
    }

    @GetMapping("/destinations/add")
    public String addDestinationForm(HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        return "admin/destinations-form";
    }

    @PostMapping("/destinations/add")
    public String addDestination(@RequestParam String name, @RequestParam String country,
                                 @RequestParam String category, @RequestParam String description,
                                 @RequestParam String imageUrl, @RequestParam Double price,
                                 @RequestParam Double rating, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        Destination dest = new Destination(name, country, category, description, imageUrl, price, rating);
        destinationService.save(dest);
        return "redirect:/admin/destinations";
    }

    // ==========================================
    // PACKAGES SECTION
    // ==========================================

    @GetMapping("/packages")
    public String listPackages(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("packages", tourPackageService.findAll());
        return "admin/packages-list";
    }

    @GetMapping("/packages/add")
    public String addPackageForm(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("destinations", destinationService.findAll());
        return "admin/packages-form";
    }

    @PostMapping("/packages/add")
    public String addPackage(@RequestParam String name, @RequestParam String category,
                             @RequestParam String description, @RequestParam Double price,
                             @RequestParam Double rating, @RequestParam String imageUrl,
                             @RequestParam Integer maxTravelers, @RequestParam Long destinationId,
                             @RequestParam String mapUrl, @RequestParam String includes,
                             @RequestParam(required = false) String hotelName,
                             @RequestParam(required = false) Double hotelExtraPrice, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        var dest = destinationService.findById(destinationId);
        if (dest.isPresent()) {
            TourPackage pkg = new TourPackage(name, description, price, rating, imageUrl, category, maxTravelers, mapUrl, dest.get());
            pkg.setIncludes(includes);
            TourPackage savedPkg = tourPackageService.save(pkg); 

            if (hotelName != null && !hotelName.trim().isEmpty()) {
                Hotel hotel = new Hotel();
                hotel.setName(hotelName);
                hotel.setExtraPrice(hotelExtraPrice != null ? hotelExtraPrice : 0.0);
                hotel.setTourPackage(savedPkg); 
                hotelService.save(hotel);
            }
        }
        return "redirect:/admin/packages";
    }

    @GetMapping("/packages/edit/{id}")
    public String editPackageForm(@PathVariable Long id, HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        var pkg = tourPackageService.findById(id);
        if (pkg.isPresent()) {
            model.addAttribute("package", pkg.get());
            model.addAttribute("destinations", destinationService.findAll());
            return "admin/packages-form";
        }
        return "redirect:/admin/packages";
    }

    @PostMapping("/packages/edit/{id}")
    public String editPackage(@PathVariable Long id, @RequestParam String name,
                              @RequestParam String category, @RequestParam String description,
                              @RequestParam Double price, @RequestParam Double rating,
                              @RequestParam String imageUrl, @RequestParam Integer maxTravelers,
                              @RequestParam Long destinationId, @RequestParam String mapUrl,
                              @RequestParam String includes,
                              @RequestParam(required = false) String hotelName,
                              @RequestParam(required = false) Double hotelExtraPrice, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        var pkg = tourPackageService.findById(id);
        if (pkg.isPresent()) {
            var dest = destinationService.findById(destinationId);
            TourPackage p = pkg.get();
            p.setName(name);
            p.setCategory(category);
            p.setDescription(description);
            p.setPrice(price);
            p.setRating(rating);
            p.setImageUrl(imageUrl);
            p.setMaxTravelers(maxTravelers);
            p.setMapUrl(mapUrl);
            p.setIncludes(includes);
            if (dest.isPresent()) {
                p.setDestination(dest.get());
            }
            TourPackage savedPkg = tourPackageService.save(p);

            if (hotelName != null && !hotelName.trim().isEmpty()) {
                Hotel hotel = new Hotel();
                hotel.setName(hotelName);
                hotel.setExtraPrice(hotelExtraPrice != null ? hotelExtraPrice : 0.0);
                hotel.setTourPackage(savedPkg); 
                hotelService.save(hotel);
            }
        }
        return "redirect:/admin/packages";
    }

    @PostMapping("/packages/delete/{id}")
    public String deletePackage(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        tourPackageService.deleteById(id);
        return "redirect:/admin/packages";
    }

    // ==========================================
    // BOOKINGS & REVIEWS SECTION
    // ==========================================

    @GetMapping("/bookings")
    public String listBookings(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("bookings", bookingService.findAll());
        return "admin/bookings-list";
    }

    @GetMapping("/reviews")
    public String listReviews(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("reviews", reviewService.findAll());
        return "admin/reviews-list";
    }

    @PostMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        reviewService.deleteById(id);
        return "redirect:/admin/reviews";
    }
}
