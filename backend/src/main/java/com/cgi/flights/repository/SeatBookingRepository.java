package com.cgi.flights.repository;

import com.cgi.flights.model.SeatBooking;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatBookingRepository extends JpaRepository<SeatBooking, Long> {
    List<SeatBooking> findByFlightId(Long flightId);
}
