package com.cgi.flights.dto.response;

import java.time.Instant;
import java.util.List;

import com.cgi.flights.model.Airport;
import com.cgi.flights.model.Booking;
import com.cgi.flights.model.Plane;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FlightResponseDTO {
  private Long id;
  private Double price;
  private AirportResponseDTO departureAirport;
  private AirportResponseDTO arrivalAirport;
  private Instant departureTime;
  private Instant arrivalTime;
  private PlaneResponseDTO plane;
}
