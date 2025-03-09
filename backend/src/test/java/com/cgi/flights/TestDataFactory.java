package com.cgi.flights;

import com.cgi.flights.dto.request.FlightFilterDTO;
import com.cgi.flights.dto.response.AirportResponseDTO;
import com.cgi.flights.dto.response.FlightResponseDTO;
import com.cgi.flights.dto.response.PlaneResponseDTO;
import com.cgi.flights.dto.response.SeatResponseDTO;
import com.cgi.flights.model.Airport;
import com.cgi.flights.model.City;
import com.cgi.flights.model.Country;
import com.cgi.flights.model.Flight;
import com.cgi.flights.model.PaginationRequest;
import com.cgi.flights.model.PagingResult;
import com.cgi.flights.model.Plane;
import com.cgi.flights.model.PlaneProducer;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;

public class TestDataFactory {
  public static final Long FIRST_ID = 1L;
  public static final Long SECOND_ID = 2L;
  public static final Long NON_EXISTENT_ID = 999L;

  public static final Double FLIGHT_PRICE = 200.0;

  public static final Instant DEPARTURE_TIME = Instant.now();
  public static final Instant ARRIVAL_TIME = DEPARTURE_TIME.plus(1, ChronoUnit.HOURS);
  private static final String ARRIVAL_TIME_STR = "2023-01-02T12:00:00Z";
  private static final String DEPARTURE_TIME_STR = "2023-01-01T12:00:00Z";

  public static final String DEPARTURE_AIRPORT_NAME = "Departure Airport";
  public static final String DEPARTURE_COUNTRY_NAME = "Departure Country";
  public static final String DEPARTURE_CITY_NAME = "Departure City";

  public static final String ARRIVAL_AIRPORT_NAME = "Arrival Airport";
  public static final String ARRIVAL_COUNTRY_NAME = "Arrival Country";
  public static final String ARRIVAL_CITY_NAME = "Arrival City";

  public static final String PLANE_NAME = "Plane";
  public static final String PLANE_PRODUCER_NAME = "Plane Producer";

  public static final String SEAT_CLASS = "Economy";
  public static final double SEAT_PRICE = 200.0;
  public static final String SEAT_COLUMN_LETTER = "A";
  public static final Long SEAT_ROW_NUMBER = 1L;
  public static final boolean SEAT_IS_WINDOW = true;
  public static final boolean SEAT_IS_AISLE = false;
  public static final boolean SEAT_IS_EXIT_ROW = false;
  public static final boolean SEAT_IS_OCCUPIED = false;

  public static PagingResult<FlightResponseDTO> createEmptyPagingResult() {
    return new PagingResult<>(List.of(), 0, 0, 0, 0, false);
  }

  public static FlightFilterDTO createEmptyFilter() {
    return new FlightFilterDTO(null, null, null, null, null, null, null, null, null, null);
  }

  public static FlightFilterDTO createFilterWithArrivalTime() {
    return new FlightFilterDTO(
        null, null, null, null, null, null, null, ARRIVAL_TIME_STR, null, null);
  }

  public static FlightFilterDTO createFilterWithDepartureTime() {
    return new FlightFilterDTO(
        null, null, null, null, null, null, DEPARTURE_TIME_STR, null, null, null);
  }

  public static FlightFilterDTO createFilterWithNonExistentAirport() {
    return new FlightFilterDTO(
        "NonexistentAirport", null, null, null, null, null, null, null, null, null);
  }

  public static PaginationRequest createPaginationRequest() {
    return new PaginationRequest(0, 10, "id", Sort.Direction.DESC);
  }

  public static Flight createSampleFlight() {
    Country country = new Country();
    country.setId(FIRST_ID);
    country.setName("USA");

    City city = new City();
    city.setId(FIRST_ID);
    city.setName(DEPARTURE_CITY_NAME);

    Airport departureAirport = new Airport();
    departureAirport.setId(FIRST_ID);
    departureAirport.setName(DEPARTURE_AIRPORT_NAME);
    departureAirport.setCity(city);
    departureAirport.setCountry(country);

    City arrivalCity = new City();
    arrivalCity.setId(SECOND_ID);
    arrivalCity.setName(ARRIVAL_CITY_NAME);

    Airport arrivalAirport = new Airport();
    arrivalAirport.setId(SECOND_ID);
    arrivalAirport.setName(ARRIVAL_AIRPORT_NAME);
    arrivalAirport.setCity(arrivalCity);
    arrivalAirport.setCountry(country);

    PlaneProducer producer = new PlaneProducer();
    producer.setId(FIRST_ID);
    producer.setName(PLANE_PRODUCER_NAME);

    Plane plane = new Plane();
    plane.setId(FIRST_ID);
    plane.setName(PLANE_NAME);
    plane.setProducer(producer);
    plane.setSeats(new ArrayList<>());
    plane.setWindowColumns(List.of("A", "K"));
    plane.setAisleColumns(List.of("C", "D", "E", "F", "G", "H"));
    plane.setExitRows(List.of(4L, 11L, 22L, 35L, 42L));

    Flight flight = new Flight();
    flight.setId(FIRST_ID);
    flight.setDepartureAirport(departureAirport);
    flight.setArrivalAirport(arrivalAirport);
    flight.setDepartureTime(DEPARTURE_TIME);
    flight.setArrivalTime(ARRIVAL_TIME);
    flight.setPlane(plane);
    flight.setPrice(FLIGHT_PRICE);

    return flight;
  }

  public static FlightResponseDTO createFlightResponseDTO() {
    return FlightResponseDTO.builder()
        .id(FIRST_ID)
        .price(FLIGHT_PRICE)
        .departureAirport(createAirportResponseDTO(true))
        .arrivalAirport(createAirportResponseDTO(false))
        .departureTime(DEPARTURE_TIME)
        .arrivalTime(ARRIVAL_TIME)
        .plane(createPlaneResponseDTO())
        .build();
  }

  public static PagingResult<FlightResponseDTO> createFlightsPagingResult() {
    return new PagingResult<>(
        List.of(createFlightResponseDTO()), 1, 1L, 10, 0, false);
  }

  public static PlaneResponseDTO createPlaneResponseDTO() {
    return new PlaneResponseDTO(
        1L, PLANE_NAME, PLANE_PRODUCER_NAME, List.of(createSeatResponseDTO()));
  }

  public static AirportResponseDTO createAirportResponseDTO(boolean isDeparture) {
    return isDeparture
        ? new AirportResponseDTO(
            FIRST_ID, DEPARTURE_AIRPORT_NAME, DEPARTURE_CITY_NAME, DEPARTURE_COUNTRY_NAME)
        : new AirportResponseDTO(
            SECOND_ID, ARRIVAL_AIRPORT_NAME, ARRIVAL_CITY_NAME, ARRIVAL_COUNTRY_NAME);
  }

  public static SeatResponseDTO createSeatResponseDTO() {
    return new SeatResponseDTO(
        FIRST_ID,
        SEAT_ROW_NUMBER,
        SEAT_COLUMN_LETTER,
        SEAT_PRICE,
        SEAT_CLASS,
        SEAT_IS_WINDOW,
        SEAT_IS_AISLE,
        SEAT_IS_EXIT_ROW,
        SEAT_IS_OCCUPIED);
  }
}
