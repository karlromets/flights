package com.cgi.flights.controller;

import com.cgi.flights.dto.request.FlightFilterDTO;
import com.cgi.flights.dto.response.FlightResponseDTO;
import com.cgi.flights.model.PaginationRequest;
import com.cgi.flights.model.PagingResult;
import com.cgi.flights.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flights")
public class FlightController {
  private final FlightService flightService;

  @GetMapping
  public ResponseEntity<PagingResult<FlightResponseDTO>> getFlights(
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "10") Integer size,
      @RequestParam(required = false, defaultValue = "id") String sortField,
      @RequestParam(required = false, defaultValue = "DESC") Sort.Direction direction,
      @RequestParam(required = false) String departureAirport,
      @RequestParam(required = false) String arrivalAirport,
      @RequestParam(required = false) String departureCity,
      @RequestParam(required = false) String arrivalCity,
      @RequestParam(required = false) String departureCountry,
      @RequestParam(required = false) String arrivalCountry,
      @RequestParam(required = false) String departureTime,
      @RequestParam(required = false) String arrivalTime,
      @RequestParam(required = false) String searchTerm,
      @RequestParam(required = false) Double price) {
    FlightFilterDTO filter =
        new FlightFilterDTO(
            departureAirport,
            arrivalAirport,
            departureCity,
            arrivalCity,
            departureCountry,
            arrivalCountry,
            departureTime,
            arrivalTime,
            searchTerm,
            price);
    final PaginationRequest request = new PaginationRequest(page, size, sortField, direction);
    final PagingResult<FlightResponseDTO> flights = flightService.getAllFlights(request, filter);
    return ResponseEntity.ok(flights);
  }

  @GetMapping("/{id}")
  public ResponseEntity<FlightResponseDTO> getFlightById(@PathVariable Long id) {
    return ResponseEntity.ok(flightService.getFlightById(id));
  }
}
