package com.example.TravelApp.repository;

import com.example.TravelApp.model.TourPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourPackageRepository extends JpaRepository<TourPackage, Long> {
    List<TourPackage> findByNameContainingIgnoreCase(String name);
    List<TourPackage> findByCategoryContainingIgnoreCase(String category);
    List<TourPackage> findByPriceBetween(double minPrice, double maxPrice);
}
