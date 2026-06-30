package com.example.TravelApp.controller;

import com.example.TravelApp.model.*;
import com.example.TravelApp.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final DestinationService destinationService;
    private final TourPackageService tourPackageService;
    private final BookingService bookingService;
    private final ReviewService reviewService;
    private final HotelService hotelService; // 🎯 HotelService එක Inject කරන්න field එකක් හැදුවා

    // Constructor එක ඇතුලට HotelService එකත් ඇතුලත් කළා
    public AdminController(UserService userService, DestinationService destinationService,
                           TourPackageService tourPackageService, BookingService bookingService,
                           ReviewService reviewService, HotelService hotelService) {
        this.userService = userService;
        this.destinationService = destinationService;
        this.tourPackageService = tourPackageService;
        this.bookingService = bookingService;
        this.reviewService = reviewService;
        this.hotelService = hotelService;
    }

    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("userRole"));
    }

    @GetMapping
    public String dashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        model.addAttribute("totalUsers", userService.findAll().size());
        model.addAttribute("totalDestinations", destinationService.findAll().size());
        model.addAttribute("totalPackages", tourPackageService.findAll().size());
        model.addAttribute("totalBookings", bookingService.findAll().size());
        model.addAttribute("totalRevenue", bookingService.findAll().stream().mapToDouble(Booking::getTotalPrice).sum());
        model.addAttribute("recentBookings", bookingService.findAll().stream().limit(5).toList());

        return "admin/dashboard";
    }

    // ==========================================
    // 🏨 🎯 HOTELS MANAGEMENT SECTION
    // ==========================================

    @GetMapping("/hotels/add")
    public String showAddHotelForm(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("hotel", new Hotel());
        model.addAttribute("packages", tourPackageService.findAll()); // හෝටලය අයිති පැකේජ් එක තෝරන්න ලිස්ට් එක යවනවා
        return "add-hotel"; // templates/add-hotel.html පිටුවට යයි
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
        return "redirect:/admin/packages"; // සේව් වුනාට පස්සේ packages ලිස්ට් එකට රීඩිරෙක්ට් වෙනවා
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

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        userService.deleteById(id);
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

    @GetMapping("/destinations/edit/{id}")
    public String editDestinationForm(@PathVariable Long id, HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        var dest = destinationService.findById(id);
        if (dest.isPresent()) {
            model.addAttribute("destination", dest.get());
            return "admin/destinations-form";
        }
        return "redirect:/admin/destinations";
    }

    @PostMapping("/destinations/edit/{id}")
    public String editDestination(@PathVariable Long id, @RequestParam String name,
                                  @RequestParam String country, @RequestParam String category,
                                  @RequestParam String description, @RequestParam String imageUrl,
                                  @RequestParam Double price, @RequestParam Double rating,
                                  HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        var dest = destinationService.findById(id);
        if (dest.isPresent()) {
            Destination d = dest.get();
            d.setName(name);
            d.setCountry(country);
            d.setCategory(category);
            d.setDescription(description);
            d.setImageUrl(imageUrl);
            d.setPrice(price);
            d.setRating(rating);
            destinationService.save(d);
        }
        return "redirect:/admin/destinations";
    }

    @PostMapping("/destinations/delete/{id}")
    public String deleteDestination(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        destinationService.deleteById(id);
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

    // 🎯 1. අලුතින් පැකේජ් එකක් දාද්දී හෝටලයකුත් එකපාරම සේව් වන විදිහට වෙනස් කළා
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
            TourPackage savedPkg = tourPackageService.save(pkg); // පැකේජ් එක මුලින්ම සේව් කරනවා

            // 🏨 හෝටල් නමක් ෆෝම් එකෙන් එවලා තියෙනවා නම් ඒකත් ඩේටාබේස් එකට සේව් කරනවා
            if (hotelName != null && !hotelName.trim().isEmpty()) {
                Hotel hotel = new Hotel();
                hotel.setName(hotelName);
                hotel.setExtraPrice(hotelExtraPrice != null ? hotelExtraPrice : 0.0);
                hotel.setTourPackage(savedPkg); // අලුතින් හැදුණු පැකේජ් එකට ලින්ක් කරනවා
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

    // 🎯 2. පැකේජ් එක Edit කරලා Update කරද්දී අලුත් හෝටලයකුත් එකපාරම සේව් වන විදිහට වෙනස් කළා
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

            // 🏨 හෝටල් නමක් ෆෝම් එකෙන් එවලා තියෙනවා නම් ඒකත් ඩේටාබේස් එකට සේව් කරනවා
            if (hotelName != null && !hotelName.trim().isEmpty()) {
                Hotel hotel = new Hotel();
                hotel.setName(hotelName);
                hotel.setExtraPrice(hotelExtraPrice != null ? hotelExtraPrice : 0.0);
                hotel.setTourPackage(savedPkg); // දැනට තියෙන පැකේජ් එකට ලින්ක් කරනවා
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