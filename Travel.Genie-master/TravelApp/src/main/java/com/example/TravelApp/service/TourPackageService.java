package com.example.TravelApp.service;

import com.example.TravelApp.model.TourPackage;
import com.example.TravelApp.repository.TourPackageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TourPackageService {

    private final TourPackageRepository tourPackageRepository;

    public TourPackageService(TourPackageRepository tourPackageRepository) {
        this.tourPackageRepository = tourPackageRepository;
    }

    public List<TourPackage> findAll() {
        return tourPackageRepository.findAll();
    }

    public List<TourPackage> searchByName(String name) {
        return tourPackageRepository.findByNameContainingIgnoreCase(name);
    }

    public List<TourPackage> findByCategory(String category) {
        return tourPackageRepository.findByCategoryContainingIgnoreCase(category);
    }

    public List<TourPackage> findByPriceRange(double minPrice, double maxPrice) {
        return tourPackageRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public Optional<TourPackage> findById(Long id) {
        return tourPackageRepository.findById(id);
    }

    public TourPackage save(TourPackage tourPackage) {
        return tourPackageRepository.save(tourPackage);
    }

    public void deleteById(Long id) {
        tourPackageRepository.deleteById(id);
    }
}
