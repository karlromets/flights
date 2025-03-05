package com.cgi.flights.controller;

import com.cgi.flights.dto.response.FlightResponseDTO;
import com.cgi.flights.service.FlightService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flights")
public class FlightController {
  private final FlightService flightService;

  @GetMapping
  public List<FlightResponseDTO> getFlights() {
    return flightService.getAllFlights();
  }

  @GetMapping("/{id}")
  public FlightResponseDTO getFlightById(@PathVariable Long id) {
    return flightService.getFlightById(id);
  }
}
