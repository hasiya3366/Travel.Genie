package com.example.TravelApp.service;

import com.example.TravelApp.model.Destination;
import com.example.TravelApp.repository.DestinationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DestinationService {

    private final DestinationRepository destinationRepository;

    public DestinationService(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    public List<Destination> findAll() {
        return destinationRepository.findAll();
    }

    public List<Destination> findByCountry(String country) {
        return destinationRepository.findByCountryContainingIgnoreCase(country);
    }

    public List<Destination> findByCategory(String category) {
        return destinationRepository.findByCategoryContainingIgnoreCase(category);
    }

    public List<Destination> findByPriceRange(double min, double max) {
        return destinationRepository.findByPriceBetween(min, max);
    }

    public Optional<Destination> findById(Long id) {
        return destinationRepository.findById(id);
    }

    public Destination save(Destination destination) {
        return destinationRepository.save(destination);
    }

    public void deleteById(Long id) {
        destinationRepository.deleteById(id);
    }
}
