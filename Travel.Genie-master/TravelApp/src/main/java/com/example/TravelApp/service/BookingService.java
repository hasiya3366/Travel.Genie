package com.example.TravelApp.service;

import com.example.TravelApp.model.Booking;
import com.example.TravelApp.model.User;
import com.example.TravelApp.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public List<Booking> findByUser(User user) {
        return bookingRepository.findByUser(user);
    }

    public void deleteById(Long id) {
        bookingRepository.deleteById(id);
    }
}
