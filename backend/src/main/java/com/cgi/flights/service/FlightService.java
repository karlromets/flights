package com.cgi.flights.service;

import com.cgi.flights.dto.request.FlightFilterDTO;
import com.cgi.flights.dto.response.AirportResponseDTO;
import com.cgi.flights.dto.response.FlightResponseDTO;
import com.cgi.flights.dto.response.PlaneResponseDTO;
import com.cgi.flights.dto.response.SeatResponseDTO;
import com.cgi.flights.model.Airport;
import com.cgi.flights.model.Flight;
import com.cgi.flights.model.PaginationRequest;
import com.cgi.flights.model.PagingResult;
import com.cgi.flights.model.Plane;
import com.cgi.flights.model.Seat;
import com.cgi.flights.model.SeatBooking;
import com.cgi.flights.repository.FlightRepository;
import com.cgi.flights.specifications.FlightSpecification;
import com.cgi.flights.utils.PaginationUtils;

import jakarta.persistence.EntityNotFoundException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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

  public PagingResult<FlightResponseDTO> getAllFlights(
      PaginationRequest request, FlightFilterDTO filter) {
    final Pageable pageable = PaginationUtils.getPageable(request);

    Specification<Flight> spec =
        FlightSpecification.builder()
            .departureAirport(filter.departureAirport())
            .arrivalAirport(filter.arrivalAirport())
            .departureCity(filter.departureCity())
            .arrivalCity(filter.arrivalCity())
            .departureCountry(filter.departureCountry())
            .arrivalCountry(filter.arrivalCountry())
            .departureTime(
                filter.departureTime() != null ? Instant.parse(filter.departureTime()) : null)
            .arrivalTime(filter.arrivalTime() != null ? Instant.parse(filter.arrivalTime()) : null)
            .searchTerm(filter.searchTerm())
            .price(filter.price())
            .build();

    Page<Flight> flights = flightRepository.findAll(spec, pageable);
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
              .plane(
                  mapToPlaneDTO(
                      flight.getPlane(),
                      flight.getPrice(),
                      seatBookingService.getAllSeatBookingsByFlightId(flight.getId())))
              .build());
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
        flightRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Flight not found"));

    return FlightResponseDTO.builder()
        .id(flight.getId())
        .price(flight.getPrice())
        .departureAirport(mapToAirportDTO(flight.getDepartureAirport()))
        .arrivalAirport(mapToAirportDTO(flight.getArrivalAirport()))
        .departureTime(flight.getDepartureTime())
        .arrivalTime(flight.getArrivalTime())
        .plane(
            mapToPlaneDTO(
                flight.getPlane(),
                flight.getPrice(),
                seatBookingService.getAllSeatBookingsByFlightId(flight.getId())))
        .build();
  }

  private AirportResponseDTO mapToAirportDTO(Airport airport) {
    return new AirportResponseDTO(
        airport.getId(),
        airport.getName(),
        airport.getCity().getName(),
        airport.getCountry().getName());
  }

  private PlaneResponseDTO mapToPlaneDTO(
      Plane plane, Double price, List<SeatBooking> seatBookings) {
    List<Seat> seats = plane.getSeats();
    List<SeatResponseDTO> seatsResponse = new ArrayList<>();

    for (Seat seat : seats) {
      boolean isOccupied =
          seatBookings.stream().anyMatch(sb -> sb.getSeat().getId().equals(seat.getId()));

      seatsResponse.add(mapToSeatDTO(seat, price, isOccupied));
    }

    return new PlaneResponseDTO(
        plane.getId(), plane.getName(), plane.getProducer().getName(), seatsResponse);
  }

  private SeatResponseDTO mapToSeatDTO(Seat seat, Double price, boolean isOccupied) {
    return new SeatResponseDTO(
        seat.getId(),
        seat.getRowNumber(),
        seat.getColumnLetter(),
        seat.getSeatClass().getPriceMultiplier() * price,
        seat.getSeatClass().getName(),
        seat.isWindow(),
        seat.isAisle(),
        seat.isExitRow(),
        isOccupied);
  }
}
