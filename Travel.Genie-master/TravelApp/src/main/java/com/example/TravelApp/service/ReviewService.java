package com.example.TravelApp.service;

import com.example.TravelApp.model.Review;
import com.example.TravelApp.model.TourPackage;
import com.example.TravelApp.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public List<Review> findByTourPackage(TourPackage tourPackage) {
        return reviewRepository.findByTourPackage(tourPackage);
    }

    public void deleteById(Long id) {
        reviewRepository.deleteById(id);
    }
}
