package com.cgi.flights.service;

import com.cgi.flights.dto.request.FlightFilterDTO;
import com.cgi.flights.dto.response.AirportResponseDTO;
import com.cgi.flights.dto.response.BookingsResponseDTO;
import com.cgi.flights.dto.response.FlightResponseDTO;
import com.cgi.flights.dto.response.PlaneResponseDTO;
import com.cgi.flights.dto.response.SeatResponseDTO;
import com.cgi.flights.model.Airport;
import com.cgi.flights.model.Booking;
import com.cgi.flights.model.Flight;
import com.cgi.flights.model.PaginationRequest;
import com.cgi.flights.model.PagingResult;
import com.cgi.flights.model.Plane;
import com.cgi.flights.model.Seat;
import com.cgi.flights.model.SeatBooking;
import com.cgi.flights.repository.FlightRepository;
import com.cgi.flights.specifications.FlightSpecification;
import com.cgi.flights.utils.PaginationUtils;
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
              .bookings(
                  mapToBookingsResponseDTO(
                      flight.getBookings(),
                      seatBookingService.getAllSeatBookingsByFlightId(flight.getId())))
              .plane(mapToPlaneDTO(flight.getPlane()))
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
        flightRepository.findById(id).orElseThrow(() -> new RuntimeException("Flight not found"));

    return FlightResponseDTO.builder()
        .id(flight.getId())
        .price(flight.getPrice())
        .departureAirport(mapToAirportDTO(flight.getDepartureAirport()))
        .arrivalAirport(mapToAirportDTO(flight.getArrivalAirport()))
        .departureTime(flight.getDepartureTime())
        .arrivalTime(flight.getArrivalTime())
        .bookings(
            mapToBookingsResponseDTO(
                flight.getBookings(),
                seatBookingService.getAllSeatBookingsByFlightId(flight.getId())))
        .plane(mapToPlaneDTO(flight.getPlane()))
        .build();
  }

  private List<BookingsResponseDTO> mapToBookingsResponseDTO(
      List<Booking> bookings, List<SeatBooking> seatBookings) {
    List<BookingsResponseDTO> boookingsResponse = new ArrayList<>();

    for (Booking booking : bookings) {
      SeatBooking matchingSeatBooking = null;
      for (SeatBooking seatBooking : seatBookings) {
        if (seatBooking.getBooking().getId().equals(booking.getId())) {
          matchingSeatBooking = seatBooking;
          break;
        }
      }

      if (matchingSeatBooking == null) {
        continue;
      }

      Seat seat = matchingSeatBooking.getSeat();

      boookingsResponse.add(
          BookingsResponseDTO.builder()
              .id(booking.getId())
              .seatId(seat.getId())
              .createdAt(booking.getCreatedAt())
              .build());
    }

    return boookingsResponse;
  }

  private AirportResponseDTO mapToAirportDTO(Airport airport) {
    return new AirportResponseDTO(
        airport.getId(),
        airport.getName(),
        airport.getCity().getName(),
        airport.getCountry().getName());
  }

  private PlaneResponseDTO mapToPlaneDTO(Plane plane) {
    List<Seat> seats = plane.getSeats();
    List<SeatResponseDTO> seatsResponse = new ArrayList<>();

    for (Seat seat : seats) {
      seatsResponse.add(mapToSeatDTO(seat));
    }

    return new PlaneResponseDTO(
        plane.getId(), plane.getName(), plane.getProducer().getName(), seatsResponse);
  }

  private SeatResponseDTO mapToSeatDTO(Seat seat) {
    return new SeatResponseDTO(
        seat.getId(),
        seat.getRowNumber(),
        seat.getColumnLetter(),
        seat.getSeatClass(),
        seat.isWindow(),
        seat.isAisle(),
        seat.isExitRow());
  }
}
