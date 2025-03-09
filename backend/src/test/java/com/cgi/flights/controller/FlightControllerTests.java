package com.cgi.flights.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cgi.flights.dto.request.FlightFilterDTO;
import com.cgi.flights.dto.response.AirportResponseDTO;
import com.cgi.flights.dto.response.FlightResponseDTO;
import com.cgi.flights.dto.response.PlaneResponseDTO;
import com.cgi.flights.dto.response.SeatResponseDTO;
import com.cgi.flights.exception.GlobalExceptionHandler;
import com.cgi.flights.model.PaginationRequest;
import com.cgi.flights.model.PagingResult;
import com.cgi.flights.service.FlightService;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class FlightControllerTests {

  private MockMvc mockMvc;

  @Mock private FlightService flightService;

  @InjectMocks private FlightController flightController;

  @BeforeEach
  void setUp() {
    mockMvc =
        MockMvcBuilders.standaloneSetup(flightController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
  }

  private static final String CONTENT_PATH = "$.content";
  private static final String FIRST_ELEMENT_PATH = CONTENT_PATH + "[0]";
  private static final String DEPARTURE_AIRPORT_PATH = FIRST_ELEMENT_PATH + ".departureAirport";
  private static final String ARRIVAL_AIRPORT_PATH = FIRST_ELEMENT_PATH + ".arrivalAirport";
  private static final String PLANE_PATH = FIRST_ELEMENT_PATH + ".plane";
  private static final String SEATS_PATH = PLANE_PATH + ".seats[0]";

  @Nested
  @DisplayName("getFlights Tests")
  class GetFlightsTests {

    @Test
    @DisplayName("Should return all flights")
    void getFlights_WhenRequested_ReturnsAllFlights() throws Exception {
      // Given
      Instant departure = Instant.now();
      Instant arrival = departure.plus(1, ChronoUnit.HOURS);
      PagingResult<FlightResponseDTO> response = createListFlightResponseDTO(departure, arrival);
      FlightFilterDTO filter =
          new FlightFilterDTO(null, null, null, null, null, null, null, null, null, null);
      final PaginationRequest request = new PaginationRequest(0, 10, "id", Sort.Direction.DESC);

      when(flightService.getAllFlights(request, filter)).thenReturn(response);

      // When/Then
      mockMvc
          .perform(get("/api/flights").contentType(APPLICATION_JSON))
          .andExpectAll(
              status().isOk(),
              jsonPath(CONTENT_PATH, hasSize(1)),
              jsonPath(FIRST_ELEMENT_PATH + ".id", is(1)),
              jsonPath(FIRST_ELEMENT_PATH + ".price", is(100.0)),
              jsonPath(DEPARTURE_AIRPORT_PATH + ".name", is("DepartureAirport")),
              jsonPath(DEPARTURE_AIRPORT_PATH + ".cityName", is("DepartureCity")),
              jsonPath(DEPARTURE_AIRPORT_PATH + ".countryName", is("DepartureCountry")),
              jsonPath(ARRIVAL_AIRPORT_PATH + ".name", is("ArrivalAirport")),
              jsonPath(ARRIVAL_AIRPORT_PATH + ".cityName", is("ArrivalCity")),
              jsonPath(ARRIVAL_AIRPORT_PATH + ".countryName", is("ArrivalCountry")),
              jsonPath(FIRST_ELEMENT_PATH + ".departureTime", is(convertToEpochTime(departure))),
              jsonPath(FIRST_ELEMENT_PATH + ".arrivalTime", is(convertToEpochTime(arrival))),
              jsonPath(PLANE_PATH + ".name", is("PlaneName")),
              jsonPath(PLANE_PATH + ".model", is("PlaneModel")),
              jsonPath(PLANE_PATH + ".seats", hasSize(1)),
              jsonPath(SEATS_PATH + ".id", is(1)),
              jsonPath(SEATS_PATH + ".rowNumber", is(1)),
              jsonPath(SEATS_PATH + ".columnLetter", is("A")),
              jsonPath(SEATS_PATH + ".price", is(100.0)),
              jsonPath(SEATS_PATH + ".SeatClass", is("Class")),
              jsonPath(SEATS_PATH + ".isWindow", is(true)),
              jsonPath(SEATS_PATH + ".isAisle", is(true)),
              jsonPath(SEATS_PATH + ".isExitRow", is(false)),
              jsonPath(SEATS_PATH + ".isOccupied", is(false)));
    }

    @Test
    @DisplayName("Should return no flights")
    void getFlights_NoFlights_ReturnsNoFlights() throws Exception {
      when(flightService.getAllFlights(any(PaginationRequest.class), any(FlightFilterDTO.class)))
          .thenReturn(createEmptyPagingResult());

      mockMvc
          .perform(get("/api/flights").contentType(APPLICATION_JSON))
          .andExpectAll(status().isOk(), jsonPath("$.content", hasSize(0)));
    }
  }

  @Nested
  @DisplayName("getFlightById Tests")
  class GetFlightByIdTests {
    @Test
    @DisplayName("Should return flight")
    void getFlightById_WhenRequested_ReturnsFlight() throws Exception {
      when(flightService.getFlightById(any(Long.class))).thenReturn(createFlightResponseDTO());

      mockMvc
          .perform(get("/api/flights/1").contentType(APPLICATION_JSON))
          .andExpectAll(status().isOk(), jsonPath("$.id", is(1)));
    }

    @Test
    @DisplayName("Should return 404")
    void getFlightById_NoFlight_Returns404() throws Exception {
      when(flightService.getFlightById(any(Long.class)))
          .thenThrow(new EntityNotFoundException("Flight not found"));

      mockMvc
          .perform(get("/api/flights/1").contentType(APPLICATION_JSON))
          .andExpectAll(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 400")
    void getFlightById_InvalidParam_Returns400() throws Exception {
      mockMvc
          .perform(get("/api/flights/invalid").contentType(APPLICATION_JSON))
          .andExpectAll(
              status().isBadRequest(),
              jsonPath("$.error", is("Invalid ID format: must be numeric")));
    }
  }

  private PagingResult<FlightResponseDTO> createListFlightResponseDTO(
      Instant departure, Instant arrival) {
    FlightResponseDTO flightResponse =
        FlightResponseDTO.builder()
            .id(1L)
            .price(100.0)
            .departureAirport(createAirportResponseDTO(true))
            .arrivalAirport(createAirportResponseDTO(false))
            .departureTime(departure)
            .arrivalTime(arrival)
            .plane(createPlaneResponseDTO())
            .build();

    return new PagingResult<FlightResponseDTO>(List.of(flightResponse), 1, 1L, 10, 0, false);
  }

  private PlaneResponseDTO createPlaneResponseDTO() {
    return new PlaneResponseDTO(1L, "PlaneName", "PlaneModel", List.of(createSeatResponseDTO()));
  }

  private AirportResponseDTO createAirportResponseDTO(boolean isDeparture) {
    if (isDeparture) {
      return new AirportResponseDTO(1L, "DepartureAirport", "DepartureCity", "DepartureCountry");
    }
    return new AirportResponseDTO(2L, "ArrivalAirport", "ArrivalCity", "ArrivalCountry");
  }

  private SeatResponseDTO createSeatResponseDTO() {
    return new SeatResponseDTO(1L, 1L, "A", 100.0, "Class", true, true, false, false);
  }

  private FlightResponseDTO createFlightResponseDTO() {
    return FlightResponseDTO.builder().id(1L).build();
  }

  private PagingResult<FlightResponseDTO> createEmptyPagingResult() {
    return new PagingResult<>(List.of(), 0, 0, 0, 0, false);
  }

  // Generated with the help of AI
  // To match our Instant to the JSON epoch time format
  private BigDecimal convertToEpochTime(Instant instant) {
    BigDecimal seconds = BigDecimal.valueOf(instant.getEpochSecond());
    BigDecimal nanos =
        BigDecimal.valueOf(instant.getNano())
            .divide(BigDecimal.valueOf(1_000_000_000.0), 9, RoundingMode.HALF_UP);
    return seconds.add(nanos).setScale(9, RoundingMode.HALF_UP);
  }
}
