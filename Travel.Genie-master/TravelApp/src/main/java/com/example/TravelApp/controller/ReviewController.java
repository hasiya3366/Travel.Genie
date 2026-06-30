package com.example.TravelApp.controller;

import com.example.TravelApp.model.Review;
import com.example.TravelApp.model.TourPackage;
import com.example.TravelApp.model.User;
import com.example.TravelApp.service.ReviewService;
import com.example.TravelApp.service.TourPackageService;
import com.example.TravelApp.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReviewController {

    private final ReviewService reviewService;
    private final TourPackageService tourPackageService;
    private final UserService userService;

    public ReviewController(ReviewService reviewService, TourPackageService tourPackageService, UserService userService) {
        this.reviewService = reviewService;
        this.tourPackageService = tourPackageService;
        this.userService = userService;
    }

    @PostMapping("/packages/review/add")
    public String addReview(@RequestParam Long packageId,
                            @RequestParam int rating,
                            @RequestParam String comment,
                            HttpSession session) {

        System.out.println("====== REVIEW SUBMIT START ======");
        System.out.println("Package ID: " + packageId + " | Rating: " + rating + " | Comment: " + comment);

        // 🔐 ඔයාගේ ප්‍රොජෙක්ට් එකේ සෙෂන් එකට දාන්න පුළුවන් පොදු නම් කිහිපයක්ම මෙතන චෙක් කරනවා
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) userId = (Long) session.getAttribute("id");
        if (userId == null && session.getAttribute("user") != null) {
            userId = ((User) session.getAttribute("user")).getId();
        }

        System.out.println("Session User ID Found: " + userId);

        if (userId == null) {
            System.out.println("❌ ERROR: User is not logged in or Session variable name mismatch!");
            return "redirect:/login";
        }

        var userOpt = userService.findById(userId);
        var pkgOpt = tourPackageService.findById(packageId);

        if (userOpt.isPresent() && pkgOpt.isPresent()) {
            User user = userOpt.get();
            TourPackage pkg = pkgOpt.get();

            Review review = new Review();
            review.setUser(user);
            review.setTourPackage(pkg);
            review.setRating(rating);
            review.setComment(comment);

            reviewService.save(review);
            System.out.println("✅ SUCCESS: Review saved to database!");
        } else {
            System.out.println("❌ ERROR: User or Package not found in Database!");
        }

        System.out.println("====== REVIEW SUBMIT END ======");
        return "redirect:/packages/" + packageId;
    }
}