package com.cgi.flights.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cgi.flights.model.SeatBooking;
import com.cgi.flights.repository.SeatBookingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatBookingService {
    private final SeatBookingRepository seatBookingRepository; 

    public List<SeatBooking> getAllSeatBookingsByFlightId(Long flightId) {
        return seatBookingRepository.findByFlightId(flightId);
    }
}
