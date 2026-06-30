package com.example.TravelApp.repository;

import com.example.TravelApp.model.Review;
import com.example.TravelApp.model.TourPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTourPackage(TourPackage tourPackage);
}
