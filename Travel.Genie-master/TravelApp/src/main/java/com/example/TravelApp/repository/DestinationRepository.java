package com.example.TravelApp.repository;

import com.example.TravelApp.model.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {
    List<Destination> findByCountryContainingIgnoreCase(String country);
    List<Destination> findByCategoryContainingIgnoreCase(String category);
    List<Destination> findByPriceBetween(double minPrice, double maxPrice);
}
