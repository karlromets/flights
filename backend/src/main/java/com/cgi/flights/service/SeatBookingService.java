package com.cgi.flights.service;

import com.cgi.flights.model.SeatBooking;
import com.cgi.flights.repository.SeatBookingRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeatBookingService {
  private final SeatBookingRepository seatBookingRepository;

  public List<SeatBooking> getAllSeatBookingsByFlightId(Long flightId) {
    return seatBookingRepository.findByFlightId(flightId);
  }

  public Map<Long, List<SeatBooking>> getSeatBookingsForFlights(List<Long> flightIds) {
    Map<Long, List<SeatBooking>> seatBookingsByFlight = new HashMap<>();

    seatBookingRepository
        .findAllById(flightIds)
        .forEach(
            sb -> {
              seatBookingsByFlight
                  .computeIfAbsent(sb.getFlight().getId(), k -> new ArrayList<>())
                  .add(sb);
            });

    return seatBookingsByFlight;
  }
}
