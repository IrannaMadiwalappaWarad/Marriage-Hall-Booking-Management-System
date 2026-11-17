package com.example.marriagehall.controller;

import com.example.marriagehall.dto.HallBookingDTO;
import com.example.marriagehall.service.MarriageHallService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/hall-bookings")
public class MarriageHallController {

    private final MarriageHallService service;

    public MarriageHallController(MarriageHallService service) {
        this.service = service;
    }

    // 1. Create Booking (POST)
    @PostMapping
    public ResponseEntity<HallBookingDTO> createBooking(@Valid @RequestBody HallBookingDTO bookingDTO) {
        HallBookingDTO createdBooking = service.createBooking(bookingDTO);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED); // Returns 201 CREATED
    }

    // 2. Display All Bookings (GET)
    @GetMapping
    public ResponseEntity<List<HallBookingDTO>> getAllBookings() {
        return ResponseEntity.ok(service.getAllBookings()); // Returns 200 OK
    }

    // 3. Get Booking by ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<HallBookingDTO> getBookingById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getBookingById(id));
    }

    // 4. Update Booking (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<HallBookingDTO> updateBooking(
            @PathVariable Integer id,
            @Valid @RequestBody HallBookingDTO bookingDTO) {
        return ResponseEntity.ok(service.updateBooking(id, bookingDTO));
    }

    // 5. Cancel Booking (DELETE - Logical deletion via status)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Integer id) {
        service.cancelBooking(id);
        return ResponseEntity.noContent().build(); // Returns 204 NO CONTENT
    }

    // 6. Report: Filter by Dates
    @GetMapping("/reports/dates")
    public ResponseEntity<List<HallBookingDTO>> getBookingsByDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(service.getBookingsByDateRange(start, end));
    }

    // 7. Report: Filter by Minimum Capacity
    @GetMapping("/reports/capacity")
    public ResponseEntity<List<HallBookingDTO>> getBookingsByCapacity(
            @RequestParam Integer minCapacity) {
        return ResponseEntity.ok(service.getBookingsByMinCapacity(minCapacity));
    }

    // 8. Report: Filter by Status
    @GetMapping("/reports/status")
    public ResponseEntity<List<HallBookingDTO>> getBookingsByStatus(
            @RequestParam String status) {
        return ResponseEntity.ok(service.getBookingsByStatus(status));
    }
}