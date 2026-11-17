package com.example.marriagehall.service;

import com.example.marriagehall.dto.HallBookingDTO;
import com.example.marriagehall.entity.MarriageHallBooking;
import com.example.marriagehall.exception.BookingConflictException;
import com.example.marriagehall.exception.BookingNotFoundException;
import com.example.marriagehall.repository.MarriageHallRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MarriageHallServiceImpl implements MarriageHallService {

    private final MarriageHallRepository repository;

    // Constructor Injection (Best Practice)
    public MarriageHallServiceImpl(MarriageHallRepository repository) {
        this.repository = repository;
    }

    @Override
    public HallBookingDTO createBooking(HallBookingDTO dto) {
        validateDoubleBooking(dto.getHallName(), dto.getEventDate(), null);
        
        MarriageHallBooking entity = mapToEntity(dto);
        // Force uppercase for consistent status tracking
        entity.setBookingStatus(dto.getBookingStatus().toUpperCase()); 
        
        MarriageHallBooking savedEntity = repository.save(entity);
        return mapToDTO(savedEntity);
    }

    @Override
    public List<HallBookingDTO> getAllBookings() {
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public HallBookingDTO getBookingById(Integer id) {
        MarriageHallBooking entity = repository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with ID: " + id));
        return mapToDTO(entity);
    }

    @Override
    public HallBookingDTO updateBooking(Integer id, HallBookingDTO dto) {
        MarriageHallBooking existingBooking = repository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Cannot update. Booking not found with ID: " + id));

        // Pass the current ID so we don't conflict with our own existing booking date
        validateDoubleBooking(dto.getHallName(), dto.getEventDate(), id);

        existingBooking.setCustomerName(dto.getCustomerName());
        existingBooking.setHallName(dto.getHallName());
        existingBooking.setEventDate(dto.getEventDate());
        existingBooking.setHallCapacity(dto.getHallCapacity());
        existingBooking.setRentAmount(dto.getRentAmount());
        existingBooking.setBookingStatus(dto.getBookingStatus().toUpperCase());

        MarriageHallBooking updatedEntity = repository.save(existingBooking);
        return mapToDTO(updatedEntity);
    }

    @Override
    public void cancelBooking(Integer id) {
        MarriageHallBooking entity = repository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Cannot cancel. Booking not found with ID: " + id));
        
        // Status-based cancellation preserves database history
        entity.setBookingStatus("CANCELLED");
        repository.save(entity);
    }

    @Override
    public List<HallBookingDTO> getBookingsByDateRange(LocalDate start, LocalDate end) {
        return repository.findByEventDateBetween(start, end).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<HallBookingDTO> getBookingsByMinCapacity(Integer capacity) {
        return repository.findByHallCapacityGreaterThanEqual(capacity).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<HallBookingDTO> getBookingsByStatus(String status) {
        return repository.findByBookingStatusIgnoreCase(status).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // --- Private Helper Methods ---

    private void validateDoubleBooking(String hallName, LocalDate eventDate, Integer currentBookingId) {
        List<MarriageHallBooking> existingBookings = repository.findByHallNameAndEventDate(hallName, eventDate);
        
        for (MarriageHallBooking booking : existingBookings) {
            // If updating, ignore the record we are currently modifying
            if (currentBookingId != null && booking.getBookingId().equals(currentBookingId)) {
                continue;
            }
            // Cancelled bookings do not block new bookings
            if ("CANCELLED".equalsIgnoreCase(booking.getBookingStatus())) {
                continue;
            }
            // If we reach here, an active conflict exists
            throw new BookingConflictException("Conflict: The hall '" + hallName + "' is already booked for an active event on " + eventDate);
        }
    }

    private MarriageHallBooking mapToEntity(HallBookingDTO dto) {
        MarriageHallBooking entity = new MarriageHallBooking();
        entity.setBookingId(dto.getBookingId()); // Will be null on creation, auto-generated by MySQL
        entity.setCustomerName(dto.getCustomerName());
        entity.setHallName(dto.getHallName());
        entity.setEventDate(dto.getEventDate());
        entity.setHallCapacity(dto.getHallCapacity());
        entity.setRentAmount(dto.getRentAmount());
        entity.setBookingStatus(dto.getBookingStatus());
        return entity;
    }

    private HallBookingDTO mapToDTO(MarriageHallBooking entity) {
        HallBookingDTO dto = new HallBookingDTO();
        dto.setBookingId(entity.getBookingId());
        dto.setCustomerName(entity.getCustomerName());
        dto.setHallName(entity.getHallName());
        dto.setEventDate(entity.getEventDate());
        dto.setHallCapacity(entity.getHallCapacity());
        dto.setRentAmount(entity.getRentAmount());
        dto.setBookingStatus(entity.getBookingStatus());
        return dto;
    }
}