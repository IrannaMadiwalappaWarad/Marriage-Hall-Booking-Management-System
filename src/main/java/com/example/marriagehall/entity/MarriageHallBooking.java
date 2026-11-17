package com.example.marriagehall.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MarriageHallBookings")
public class MarriageHallBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BookingID")
    private Integer bookingId;

    @Column(name = "CustomerName", nullable = false, length = 100)
    private String customerName;

    @Column(name = "HallName", nullable = false, length = 100)
    private String hallName;

    @Column(name = "EventDate", nullable = false)
    private LocalDate eventDate;

    @Column(name = "HallCapacity", nullable = false)
    private Integer hallCapacity;

    @Column(name = "RentAmount", nullable = false, precision = 10, scale = 2)
    private BigDecimal rentAmount;

    @Column(name = "BookingStatus", nullable = false, length = 20)
    private String bookingStatus;
}