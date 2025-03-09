package com.cgi.flights.mapper;

import com.cgi.flights.dto.response.AirportResponseDTO;
import com.cgi.flights.dto.response.FlightResponseDTO;
import com.cgi.flights.dto.response.PlaneResponseDTO;
import com.cgi.flights.dto.response.SeatResponseDTO;
import com.cgi.flights.model.Airport;
import com.cgi.flights.model.Flight;
import com.cgi.flights.model.Plane;
import com.cgi.flights.model.Seat;
import com.cgi.flights.model.SeatBooking;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FlightMapper {

  @Mapping(
      target = "departureAirport",
      expression = "java(mapToAirportDTO(flight.getDepartureAirport()))")
  @Mapping(
      target = "arrivalAirport",
      expression = "java(mapToAirportDTO(flight.getArrivalAirport()))")
  @Mapping(
      target = "plane",
      expression =
          "java(mapToPlaneDTO(flight.getPlane(), flight.getPrice(), seatBookings != null ?"
              + " seatBookings : List.of()))")
  FlightResponseDTO toDto(Flight flight, List<SeatBooking> seatBookings);

  default AirportResponseDTO mapToAirportDTO(Airport airport) {
    return new AirportResponseDTO(
        airport.getId(),
        airport.getName(),
        airport.getCity().getName(),
        airport.getCountry().getName());
  }

  default PlaneResponseDTO mapToPlaneDTO(
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

  default SeatResponseDTO mapToSeatDTO(Seat seat, Double price, boolean isOccupied) {
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
