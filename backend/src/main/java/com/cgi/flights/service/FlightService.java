package com.cgi.flights.service;

import com.cgi.flights.dto.response.FlightResponseDTO;
import com.cgi.flights.model.Flight;
import com.cgi.flights.repository.FlightRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlightService {
  private final FlightRepository flightRepository;

  public List<FlightResponseDTO> getAllFlights() {
    List<Flight> flights = flightRepository.findAll();
    List<FlightResponseDTO> flightResponse = new ArrayList<>();

    for (Flight flight : flights) {
      flightResponse.add(
          FlightResponseDTO.builder()
              .id(flight.getId())
              .price(flight.getPrice())
              .departureAirport(flight.getDepartureAirport())
              .arrivalAirport(flight.getArrivalAirport())
              .departureTime(flight.getDepartureTime())
              .arrivalTime(flight.getArrivalTime())
              .bookings(flight.getBookings())
              .plane(flight.getPlane())
              .build());
    }

    return flightResponse;
  }

  public Flight getFlightById(Long id) {
    return flightRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Flight not found"));
  }
}
