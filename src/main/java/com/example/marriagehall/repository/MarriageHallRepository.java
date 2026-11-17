package com.example.marriagehall.repository;

import com.example.marriagehall.entity.MarriageHallBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MarriageHallRepository extends JpaRepository<MarriageHallBooking, Integer> {

    // 1. Finds all bookings within a specific date range
    List<MarriageHallBooking> findByEventDateBetween(LocalDate start, LocalDate end);

    // 2. Finds all halls that have at least the specified capacity
    List<MarriageHallBooking> findByHallCapacityGreaterThanEqual(Integer capacity);

    // 3. Finds bookings by status (e.g., "CONFIRMED", "CANCELLED"), ignoring case
    List<MarriageHallBooking> findByBookingStatusIgnoreCase(String status);

    // 4. Custom query to help us validate double-bookings later in the Service layer
    List<MarriageHallBooking> findByHallNameAndEventDate(String hallName, LocalDate eventDate);
}