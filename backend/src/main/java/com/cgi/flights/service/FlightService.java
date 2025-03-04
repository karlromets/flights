package com.cgi.flights.service;

import com.cgi.flights.dto.response.AirportResponseDTO;
import com.cgi.flights.dto.response.FlightResponseDTO;
import com.cgi.flights.dto.response.PlaneResponseDTO;
import com.cgi.flights.model.Airport;
import com.cgi.flights.model.Flight;
import com.cgi.flights.model.Plane;
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
              .departureAirport(mapToAirportDTO(flight.getDepartureAirport()))
              .arrivalAirport(mapToAirportDTO(flight.getArrivalAirport()))
              .departureTime(flight.getDepartureTime())
              .arrivalTime(flight.getArrivalTime())
              .plane(mapToPlaneDTO(flight.getPlane()))
              .build());
    }

    return flightResponse;
  }

  public Flight getFlightById(Long id) {
    return flightRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Flight not found"));
  }

  private AirportResponseDTO mapToAirportDTO(Airport airport) {
    return new AirportResponseDTO(
        airport.getId(),
        airport.getName(),
        airport.getCity().getName(),
        airport.getCountry().getName());
  }

  private PlaneResponseDTO mapToPlaneDTO(Plane plane) {
    return new PlaneResponseDTO(plane.getId(), plane.getName(), plane.getProducer().getName());
  }
}
