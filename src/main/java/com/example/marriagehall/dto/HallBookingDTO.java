package com.example.marriagehall.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HallBookingDTO {

    private Integer bookingId;

    @NotBlank(message = "Customer name cannot be empty")
    private String customerName;

    @NotBlank(message = "Hall name cannot be empty")
    private String hallName;

    @NotNull(message = "Event date is required")
    @Future(message = "Event date must be in the future")
    private LocalDate eventDate;

    @NotNull(message = "Hall capacity is required")
    @Min(value = 51, message = "Hall capacity must be greater than 50")
    private Integer hallCapacity;

    @NotNull(message = "Rent amount is required")
    @Min(value = 1, message = "Rent amount must be greater than 0")
    private BigDecimal rentAmount;

    @NotBlank(message = "Booking status cannot be empty")
    private String bookingStatus;
}