package com.cgi.flights.controller;

import static com.cgi.flights.TestDataFactory.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cgi.flights.dto.request.FlightFilterDTO;
import com.cgi.flights.dto.response.FlightResponseDTO;
import com.cgi.flights.exception.GlobalExceptionHandler;
import com.cgi.flights.model.PaginationRequest;
import com.cgi.flights.model.PagingResult;
import com.cgi.flights.service.FlightService;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
      PagingResult<FlightResponseDTO> response = createFlightsPagingResult();
      FlightFilterDTO filter = createEmptyFilter();
      final PaginationRequest request = createPaginationRequest();

      when(flightService.getAllFlights(request, filter)).thenReturn(response);

      // When/Then
      mockMvc
          .perform(get("/api/flights").contentType(APPLICATION_JSON))
          .andExpectAll(
              status().isOk(),
              jsonPath(CONTENT_PATH, hasSize(1)),
              jsonPath(FIRST_ELEMENT_PATH + ".id", is(FIRST_ID.intValue())),
              jsonPath(FIRST_ELEMENT_PATH + ".price", is(FLIGHT_PRICE)),
              jsonPath(DEPARTURE_AIRPORT_PATH + ".name", is(DEPARTURE_AIRPORT_NAME)),
              jsonPath(DEPARTURE_AIRPORT_PATH + ".cityName", is(DEPARTURE_CITY_NAME)),
              jsonPath(DEPARTURE_AIRPORT_PATH + ".countryName", is(DEPARTURE_COUNTRY_NAME)),
              jsonPath(ARRIVAL_AIRPORT_PATH + ".name", is(ARRIVAL_AIRPORT_NAME)),
              jsonPath(ARRIVAL_AIRPORT_PATH + ".cityName", is(ARRIVAL_CITY_NAME)),
              jsonPath(ARRIVAL_AIRPORT_PATH + ".countryName", is(ARRIVAL_COUNTRY_NAME)),
              jsonPath(
                  FIRST_ELEMENT_PATH + ".departureTime", is(convertToEpochTime(DEPARTURE_TIME))),
              jsonPath(FIRST_ELEMENT_PATH + ".arrivalTime", is(convertToEpochTime(ARRIVAL_TIME))),
              jsonPath(PLANE_PATH + ".name", is(PLANE_NAME)),
              jsonPath(PLANE_PATH + ".producer", is(PLANE_PRODUCER_NAME)),
              jsonPath(PLANE_PATH + ".seats", hasSize(1)),
              jsonPath(SEATS_PATH + ".id", is(FIRST_ID.intValue())),
              jsonPath(SEATS_PATH + ".rowNumber", is(SEAT_ROW_NUMBER.intValue())),
              jsonPath(SEATS_PATH + ".columnLetter", is(SEAT_COLUMN_LETTER)),
              jsonPath(SEATS_PATH + ".price", is(SEAT_PRICE)),
              jsonPath(SEATS_PATH + ".SeatClass", is(SEAT_CLASS)),
              jsonPath(SEATS_PATH + ".isWindow", is(SEAT_IS_WINDOW)),
              jsonPath(SEATS_PATH + ".isAisle", is(SEAT_IS_AISLE)),
              jsonPath(SEATS_PATH + ".isExitRow", is(SEAT_IS_EXIT_ROW)),
              jsonPath(SEATS_PATH + ".isOccupied", is(SEAT_IS_OCCUPIED)));
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
