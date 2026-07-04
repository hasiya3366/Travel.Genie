package com.example.TravelApp.repository;

import com.example.TravelApp.model.Booking;
import com.example.TravelApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByUser(User user);

    @Query(value = "SELECT COALESCE(SUM(total_price), 0.0) FROM booking WHERE status != 'CANCELLED'", nativeQuery = true)
    Double getTotalRevenue();
}
