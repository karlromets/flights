package com.cgi.flights.dto.response;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FlightResponseDTO {
  @NotNull private Long id;
  @NotNull private Double price;
  @NotNull private AirportResponseDTO departureAirport;
  @NotNull private AirportResponseDTO arrivalAirport;
  @NotNull private Instant departureTime;
  @NotNull private Instant arrivalTime;
  @NotNull private PlaneResponseDTO plane;
}
