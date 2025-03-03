package com.cgi.flights.repository;

import com.cgi.flights.model.SeatBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatBookingRepository extends JpaRepository<SeatBooking, Long> {}
