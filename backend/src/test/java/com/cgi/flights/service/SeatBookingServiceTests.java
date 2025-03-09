package com.cgi.flights.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cgi.flights.model.Flight;
import com.cgi.flights.model.SeatBooking;
import com.cgi.flights.repository.SeatBookingRepository;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SeatBookingServiceTests {
  @Mock private SeatBookingRepository seatBookingRepository;

  @InjectMocks private SeatBookingService seatBookingService;

  private static final Long FLIGHT_ID = 1L;
  private static final Long SEAT_BOOKING_1_ID = 1L;
  private static final Long SEAT_BOOKING_2_ID = 2L;

  @Nested
  @DisplayName("getAllSeatBookingsByFlightId Tests")
  class GetAllSeatBookingsByFlightIdTests {
    @Test
    @DisplayName("Should return all seat bookings for a flight ID")
    void getAllSeatBookingsByFlightId_WhenCalled_ReturnsAllSeatBookingsForFlight() {
      List<SeatBooking> mockSeatBookings = createSeatBookings();
      when(seatBookingRepository.findByFlightId(FLIGHT_ID)).thenReturn(mockSeatBookings);

      List<SeatBooking> result = seatBookingService.getAllSeatBookingsByFlightId(FLIGHT_ID);

      assertAll(
          () -> assertThat(result).as("checking if the result has size 2").hasSize(2),
          () ->
              assertThat(result.get(0).getId())
                  .as("checking if the first seat booking id is correct")
                  .isEqualTo(SEAT_BOOKING_1_ID),
          () ->
              assertThat(result.get(1).getId())
                  .as("checking if the second seat booking id is correct")
                  .isEqualTo(SEAT_BOOKING_2_ID),
          () -> verify(seatBookingRepository, times(1)).findByFlightId(FLIGHT_ID));
    }
  }

  @Nested
  @DisplayName("getSeatBookingsForFlights Tests")
  class GetSeatBookingsForFlightsTests {
    @Test
    @DisplayName("Should return seat bookings for given flight IDs")
    void getSeatBookingsForFlights_WhenCalled_ReturnsSeatBookingsForFlights() {
      List<SeatBooking> mockSeatBookings = createSeatBookings();
      when(seatBookingRepository.findAllById(List.of(FLIGHT_ID))).thenReturn(mockSeatBookings);

      Map<Long, List<SeatBooking>> result =
          seatBookingService.getSeatBookingsForFlights(List.of(FLIGHT_ID));

      assertAll(
          () -> assertThat(result).as("checking map size").hasSize(1),
          () ->
              assertThat(result.get(FLIGHT_ID).get(0).getId())
                  .as("checking if the first seat booking id is correct")
                  .isEqualTo(SEAT_BOOKING_1_ID),
          () ->
              assertThat(result.get(FLIGHT_ID).get(1).getId())
                  .as("checking if the second seat booking id is correct")
                  .isEqualTo(SEAT_BOOKING_2_ID),
          () -> verify(seatBookingRepository, times(1)).findAllById(List.of(FLIGHT_ID)));
    }
  }

  private List<SeatBooking> createSeatBookings() {
    SeatBooking seatBooking1 = new SeatBooking();
    seatBooking1.setId(SEAT_BOOKING_1_ID);
    Flight flight1 = new Flight();
    flight1.setId(FLIGHT_ID);
    seatBooking1.setFlight(flight1);

    SeatBooking seatBooking2 = new SeatBooking();
    seatBooking2.setId(SEAT_BOOKING_2_ID);
    seatBooking2.setFlight(flight1);

    return List.of(seatBooking1, seatBooking2);
  }
}
