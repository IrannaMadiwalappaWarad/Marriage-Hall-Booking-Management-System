package com.example.marriagehall.service;

import com.example.marriagehall.dto.HallBookingDTO;
import java.time.LocalDate;
import java.util.List;

public interface MarriageHallService {
    HallBookingDTO createBooking(HallBookingDTO bookingDTO);
    List<HallBookingDTO> getAllBookings();
    HallBookingDTO getBookingById(Integer id);
    HallBookingDTO updateBooking(Integer id, HallBookingDTO bookingDTO);
    void cancelBooking(Integer id);
    List<HallBookingDTO> getBookingsByDateRange(LocalDate start, LocalDate end);
    List<HallBookingDTO> getBookingsByMinCapacity(Integer capacity);
    List<HallBookingDTO> getBookingsByStatus(String status);
}