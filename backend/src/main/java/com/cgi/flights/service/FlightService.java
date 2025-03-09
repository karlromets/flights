package com.cgi.flights.service;

import com.cgi.flights.dto.request.FlightFilterDTO;
import com.cgi.flights.dto.response.FlightResponseDTO;
import com.cgi.flights.mapper.FlightMapper;
import com.cgi.flights.mapper.SpecificationMapper;
import com.cgi.flights.model.Flight;
import com.cgi.flights.model.PaginationRequest;
import com.cgi.flights.model.PagingResult;
import com.cgi.flights.model.SeatBooking;
import com.cgi.flights.repository.FlightRepository;
import com.cgi.flights.utils.PaginationUtils;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlightService {
  private final FlightRepository flightRepository;
  private final SeatBookingService seatBookingService;
  private final FlightMapper flightMapper;
  private final SpecificationMapper specificationMapper;

  public PagingResult<FlightResponseDTO> getAllFlights(
      PaginationRequest request, FlightFilterDTO filter) {

    final Pageable pageable = PaginationUtils.getPageable(request);
    Specification<Flight> spec = specificationMapper.toFlightSpecification(filter);

    Page<Flight> flights = flightRepository.findAll(spec, pageable);
    List<FlightResponseDTO> flightResponse = new ArrayList<>();

    List<Long> flightIds = new ArrayList<>();
    for (Flight flight : flights) {
      flightIds.add(flight.getId());
    }

    Map<Long, List<SeatBooking>> seatBookingsByFlight =
        seatBookingService.getSeatBookingsForFlights(flightIds);

    for (Flight flight : flights) {
      flightResponse.add(flightMapper.toDto(flight, seatBookingsByFlight.get(flight.getId())));
    }

    return new PagingResult<>(
        flightResponse,
        flights.getTotalPages(),
        flights.getTotalElements(),
        flights.getSize(),
        flights.getNumber(),
        flights.isEmpty());
  }

  public FlightResponseDTO getFlightById(Long id) {
    Flight flight =
        flightRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Flight not found"));

    List<SeatBooking> seatBookingsByFlight = seatBookingService.getAllSeatBookingsByFlightId(id);

    return flightMapper.toDto(flight, seatBookingsByFlight);
  }
}
