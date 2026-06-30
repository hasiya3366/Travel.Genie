package com.example.TravelApp.service;

import com.example.TravelApp.model.Hotel;
import com.example.TravelApp.repository.HotelRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public Optional<Hotel> findById(Long id) {
        return hotelRepository.findById(id);
    }

    // 🎯 හෝටලයක් සේව් කරන්න අලුත් මෙතඩ් එකක් එකතු කළා
    public Hotel save(Hotel hotel) {
        return hotelRepository.save(hotel);
    }
}