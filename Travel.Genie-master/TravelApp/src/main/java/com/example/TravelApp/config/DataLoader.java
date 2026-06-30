package com.example.TravelApp.config;

import com.example.TravelApp.model.Destination;
import com.example.TravelApp.model.Hotel; // 🎯 Hotel Model එක Import කළා
import com.example.TravelApp.model.TourPackage;
import com.example.TravelApp.model.User;
import com.example.TravelApp.service.DestinationService;
import com.example.TravelApp.service.HotelService; // 🎯 HotelService එක Import කළා
import com.example.TravelApp.service.TourPackageService;
import com.example.TravelApp.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserService userService;
    private final DestinationService destinationService;
    private final TourPackageService tourPackageService;
    private final HotelService hotelService; // 🎯 HotelService එක සඳහා Field එකක් හැදුවා

    // Constructor එක ඇතුලට HotelService එකත් Inject කරගත්තා
    public DataLoader(UserService userService, DestinationService destinationService, TourPackageService tourPackageService, HotelService hotelService) {
        this.userService = userService;
        this.destinationService = destinationService;
        this.tourPackageService = tourPackageService;
        this.hotelService = hotelService;
    }

    @Override
    public void run(String... args) {
        if (userService.findByEmail("admin@example.com").isEmpty()) {
            User admin = new User("Admin", "admin@example.com", "admin123", "ADMIN");
            userService.save(admin);
        }
        if (userService.findByEmail("user@example.com").isEmpty()) {
            User user = new User("Travel User", "user@example.com", "password", "USER");
            userService.save(user);
        }

        if (destinationService.findAll().isEmpty()) {
            // ==========================================
            // 1. DESTINATIONS
            // ==========================================

            // --- Beach Side ---
            Destination mirissa = destinationService.save(new Destination("Mirissa Beach", "Sri Lanka", "Beach", "Golden sands, surfing, and whale watching adventures.", "/images/Mirssa.jpa.jpeg", 180.00, 4.7));
            Destination hikkaduwa = destinationService.save(new Destination("Hikkaduwa Beach", "Sri Lanka", "Beach", "Famous for coral reefs, sea turtles, and vibrant nightlife.", "/images/Hikkaduwa.jpa.png", 160.00, 4.6));
            Destination nilaveli = destinationService.save(new Destination("Nilaveli Beach", "Sri Lanka", "Beach", "Pristine white sands and crystal-clear waters near Pigeon Island.", "/images/Nilaweli.jpa.png", 210.00, 4.8));
            Destination galleBeach = destinationService.save(new Destination("Galle Coast", "Sri Lanka", "Beach", "Scenic coastal views combining history with beautiful beaches.", "/images/Gallefort.jpa.png", 195.00, 4.8));

            // --- Mountain Side ---
            Destination hortonPlains = destinationService.save(new Destination("Horton Plains", "Sri Lanka", "Mountain", "Cold misty plains leading to the dramatic World's End cliff.", "/images/Hortanplace.jpa.png", 250.00, 4.8));
            Destination knuckles = destinationService.save(new Destination("Knuckles Range", "Sri Lanka", "Mountain", "Breathtaking misty mountain range, perfect for hikers.", "/images/Knucles.jpa.png", 230.00, 4.7));
            Destination pidurangala = destinationService.save(new Destination("Pidurangala Rock", "Sri Lanka", "Mountain", "Incredible sunrise views looking over the Sigiriya Rock fortress.", "/images/Pidurangala.jpa.png", 140.00, 4.9));
            Destination adamsPeak = destinationService.save(new Destination("Adam's Peak", "Sri Lanka", "Mountain", "Sacred mountain peak famous for spiritual sunrise climbs.", "/images/sriPadaya.jpa.png", 180.00, 4.9));

            // --- Adventure ---
            Destination kitulgala = destinationService.save(new Destination("Kitulgala River", "Sri Lanka", "Adventure", "White water rafting and thrilling canyoning experiences.", "/images/KIthulgala.jpa.png", 150.00, 4.6));
            Destination yala = destinationService.save(new Destination("Yala National Park", "Sri Lanka", "Adventure", "Exciting wildlife safari featuring leopards and wild elephants.", "/images/Yala.jpa.png", 280.00, 4.8));

            // --- Cultural & Historical / Buddhist Cultural ---
            Destination anuradhapura = destinationService.save(new Destination("Anuradhapura", "Sri Lanka", "Cultural", "Sacred ancient city filled with massive stupas and history.", "/images/Anuradhapura.jpa.png", 200.00, 4.8));
            Destination polonnaruwa = destinationService.save(new Destination("Polonnaruwa", "Sri Lanka", "Cultural", "Well-preserved ruins of royal palaces and the Gal Viharaya.", "/images/Polonnaruwa.jpa.png", 190.00, 4.7));
            Destination mahanuwara = destinationService.save(new Destination("Kandy", "Sri Lanka", "Cultural", "Home to the sacred Temple of the Tooth Relic and royal lake.", "/images/Kandy.jpa.png", 175.00, 4.9));


            // ==========================================
            // 2. TOUR PACKAGES
            // ==========================================

            // --- Beach Side Packages ---
            // 🎯 හෝටල් ඇඩ් කරන්න ලේසි වෙන්න, සේව් වෙන Object එක Variable එකකට ගත්තා
            TourPackage p1 = tourPackageService.save(new TourPackage("Mirissa Whale Watching", "4-day exciting beach vacation with a whale watching cruise.", 180.0, 4.7, "/images/Mirssa.jpa.jpeg", "Beach", 12, mirissa));
            tourPackageService.save(new TourPackage("Hikkaduwa Coral Surf", "3-day tropical escape exploring coral reefs and surfing shores.", 160.0, 4.6, "/images/Hikkaduwa.jpa.png", "Beach", 15, hikkaduwa));
            tourPackageService.save(new TourPackage("Nilaveli Serenity Getaway", "5-day tranquil stay at the pristine white-sand beach of Trinco.", 210.0, 4.8, "/images/Nilaweli.jpa.png", "Beach", 10, nilaveli));
            tourPackageService.save(new TourPackage("Galle Fort Coastal Walk", "3-day historic exploration mixed with beautiful southern coastlines.", 195.0, 4.8, "/images/Gallefort.jpa.png", "Beach", 14, galleBeach));

            // --- Mountain Side Packages ---
            tourPackageService.save(new TourPackage("Horton Plains World's End", "3-day nature trail through misty cold plains and high cliffs.", 250.0, 4.8, "/images/Hortanplace.jpa.png", "Mountain", 8, hortonPlains));
            tourPackageService.save(new TourPackage("Knuckles Misty Expedition", "4-day hardcore hiking tour deep into the biodiversity hotspot.", 230.0, 4.7, "/images/Knucles.jpa.png", "Mountain", 6, knuckles));
            tourPackageService.save(new TourPackage("Pidurangala Sunrise Hike", "2-day historic climb to view the ultimate sunrise over Sigiriya.", 140.0, 4.9, "/images/Pidurangala.jpa.png", "Mountain", 20, pidurangala));
            tourPackageService.save(new TourPackage("Sri Pada Spiritual Pilgrimage", "3-day sacred night-climb to catch the holy mountain shadow.", 180.0, 4.9, "/images/sriPadaya.jpa.png", "Mountain", 25, adamsPeak));

            // --- Adventure Packages ---
            tourPackageService.save(new TourPackage("Kitulgala White Water Thrill", "2-day ultimate adventure with rafting and waterfall trekking.", 150.0, 4.6, "/images/KIthulgala.jpa.png", "Adventure", 20, kitulgala));
            tourPackageService.save(new TourPackage("Yala Wild Leopard Safari", "3-day intense wildlife exploration inside the leopard kingdom.", 280.0, 4.8, "/images/Yala.jpa.png", "Adventure", 10, yala));

            // --- Cultural / Historical & Buddhist Cultural Packages ---
            tourPackageService.save(new TourPackage("Anuradhapura Sacred Kingdom", "3-day deep cultural tour around ancient giant stupas and ruins.", 200.0, 4.8, "/images/Anuradhapura.jpa.png", "Cultural", 15, anuradhapura));
            tourPackageService.save(new TourPackage("Polonnaruwa Medieval Ancient Tour", "3-day bicycle exploration through the historic royal capital ruins.", 190.0, 4.7, "/images/Polonnaruwa.jpa.png", "Cultural", 15, polonnaruwa));
            TourPackage p13 = tourPackageService.save(new TourPackage("Kandy Dalada Maligawa Heritage", "2-day blessed cultural visit to the sacred tooth relic temple.", 175.0, 4.9, "/images/Kandy.jpa.png", "Cultural", 30, mahanuwara));


            // ==========================================
            // 🎯 3. AUTOMATIC HOTELS LOADING
            // ==========================================

            // 🏨 Mirissa Package එකට හෝටල් එකතු කිරීම
            Hotel h1 = new Hotel();
            h1.setName("Cinnamon Bey Beruwala");
            h1.setExtraPrice(40.00); // USD හෝ LKR (පැකේජ් එකේ විදිහට)
            h1.setTourPackage(p1);
            hotelService.save(h1);

            Hotel h2 = new Hotel();
            h2.setName("Weligama Bay Marriott Resort");
            h2.setExtraPrice(85.00);
            h2.setTourPackage(p1);
            hotelService.save(h2);

            // 🏨 Kandy Package එකට හෝටල් එකතු කිරීම
            Hotel h3 = new Hotel();
            h3.setName("Earl's Regency Kandy");
            h3.setExtraPrice(50.00);
            h3.setTourPackage(p13);
            hotelService.save(h3);

            Hotel h4 = new Hotel();
            h4.setName("Grand Kandyan Hotel");
            h4.setExtraPrice(70.00);
            h4.setTourPackage(p13);
            hotelService.save(h4);
        }
    }
}