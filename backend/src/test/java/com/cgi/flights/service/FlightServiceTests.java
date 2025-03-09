package com.cgi.flights.service;

import static com.cgi.flights.TestDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.cgi.flights.dto.request.FlightFilterDTO;
import com.cgi.flights.dto.response.FlightResponseDTO;
import com.cgi.flights.mapper.FlightMapper;
import com.cgi.flights.mapper.SpecificationMapper;
import com.cgi.flights.model.*;
import com.cgi.flights.repository.FlightRepository;
import com.cgi.flights.specifications.FlightSpecification;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class FlightServiceTests {
  @Mock private FlightRepository flightRepository;

  @Mock private SeatBookingService seatBookingService;

  @Mock private SpecificationMapper specificationMapper;

  @Mock private FlightMapper flightMapper;

  @InjectMocks private FlightService flightService;

  @Nested
  @DisplayName("getAllFlights Tests")
  class GetAllFlightsTests {
    @Test
    @DisplayName("Should return all flights")
    void getAllFlights_WhenCalled_ShouldReturnAllFlights() {
      PaginationRequest paginationRequest = createPaginationRequest();
      FlightFilterDTO filter = createEmptyFilter();

      List<Flight> flights = new ArrayList<>();
      flights.add(createSampleFlight());
      Page<Flight> flightPage = new PageImpl<>(flights);

      FlightSpecification spec = new FlightSpecification();
      when(specificationMapper.toFlightSpecification(filter)).thenReturn(spec);

      when(flightRepository.findAll(any(FlightSpecification.class), any(Pageable.class)))
          .thenReturn(flightPage);
      when(seatBookingService.getSeatBookingsForFlights(anyList())).thenReturn(new HashMap<>());

      PagingResult<FlightResponseDTO> result =
          flightService.getAllFlights(paginationRequest, filter);

      assertAll(
          () -> assertThat(result).as("checking if result is not null").isNotNull(),
          () ->
              assertThat(result.getContent())
                  .as("checking if number of flights matches")
                  .hasSameSizeAs(flights),
          () ->
              verify(flightRepository)
                  .findAll(any(FlightSpecification.class), any(Pageable.class)));
    }

    @Test
    @DisplayName("Should return empty list when no flights match criteria")
    void getAllFlights_WhenNoFlightsMatchCriteria_ShouldReturnEmptyList() {
      PaginationRequest paginationRequest = createPaginationRequest();
      FlightFilterDTO filter = createFilterWithNonExistentAirport();

      Page<Flight> emptyPage = new PageImpl<>(new ArrayList<>());

      FlightSpecification spec = new FlightSpecification();
      when(specificationMapper.toFlightSpecification(filter)).thenReturn(spec);

      when(flightRepository.findAll(any(FlightSpecification.class), any(Pageable.class)))
          .thenReturn(emptyPage);

      PagingResult<FlightResponseDTO> result =
          flightService.getAllFlights(paginationRequest, filter);

      assertAll(
          () -> assertTrue(result.isEmpty(), "checking if result is empty"),
          () ->
              assertThat(result.getContent())
                  .as("checking if content size is zero")
                  .hasSameSizeAs(new ArrayList<>()));
    }

    @Test
    @DisplayName("Should filter flights by departure time")
    void getAllFlights_WhenDepartureTimeProvided_ShouldFilterByDepartureTime() {
      PaginationRequest paginationRequest = createPaginationRequest();
      FlightFilterDTO filter = createFilterWithDepartureTime();

      List<Flight> flights = new ArrayList<>();
      flights.add(createSampleFlight());
      Page<Flight> flightPage = new PageImpl<>(flights);

      FlightSpecification spec = new FlightSpecification();
      when(specificationMapper.toFlightSpecification(filter)).thenReturn(spec);

      when(flightRepository.findAll(any(FlightSpecification.class), any(Pageable.class)))
          .thenReturn(flightPage);
      when(seatBookingService.getSeatBookingsForFlights(anyList())).thenReturn(new HashMap<>());

      PagingResult<FlightResponseDTO> result =
          flightService.getAllFlights(paginationRequest, filter);

      assertAll(
          () -> assertThat(result).as("checking if result is not null").isNotNull(),
          () ->
              assertThat(result.getContent())
                  .as("checking if number of flights matches")
                  .hasSameSizeAs(flights));
    }

    @Test
    @DisplayName("Should filter flights by arrival time")
    void getAllFlights_WhenArrivalTimeProvided_ShouldFilterByArrivalTime() {
      PaginationRequest paginationRequest = createPaginationRequest();
      FlightFilterDTO filter = createFilterWithArrivalTime();

      List<Flight> flights = new ArrayList<>();
      flights.add(createSampleFlight());
      Page<Flight> flightPage = new PageImpl<>(flights);

      FlightSpecification spec = new FlightSpecification();
      when(specificationMapper.toFlightSpecification(filter)).thenReturn(spec);

      when(flightRepository.findAll(any(FlightSpecification.class), any(Pageable.class)))
          .thenReturn(flightPage);
      when(seatBookingService.getSeatBookingsForFlights(anyList())).thenReturn(new HashMap<>());

      PagingResult<FlightResponseDTO> result =
          flightService.getAllFlights(paginationRequest, filter);

      assertAll(
          () -> assertThat(result).as("checking if result is not null").isNotNull(),
          () ->
              assertThat(result.getContent())
                  .as("checking if number of flights matches")
                  .hasSameSizeAs(flights));
    }
  }

  @Nested
  @DisplayName("getFlightById Tests")
  class GetFlightByIdTests {
    @Test
    @DisplayName("Should return flight by id")
    void getFlightById_WhenCalled_ShouldReturnFlightById() {
      Flight flight = createSampleFlight();
      List<SeatBooking> mockBookings = new ArrayList<>();
      FlightResponseDTO expectedDto = createFlightResponseDTO();

      when(flightRepository.findById(FIRST_ID)).thenReturn(Optional.of(flight));
      when(seatBookingService.getAllSeatBookingsByFlightId(FIRST_ID)).thenReturn(mockBookings);
      when(flightMapper.toDto(flight, mockBookings)).thenReturn(expectedDto);

      FlightResponseDTO result = flightService.getFlightById(FIRST_ID);

      assertAll(
          () -> assertThat(result).as("checking if result is not null").isNotNull(),
          () -> assertThat(result.getId()).as("checking if flight ID matches").isEqualTo(FIRST_ID),
          () -> verify(flightMapper, times(1)).toDto(flight, mockBookings),
          () -> verify(flightRepository, times(1)).findById(FIRST_ID));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when flight not found")
    void getFlightById_WhenFlightNotFound_ShouldThrowEntityNotFoundException() {
      when(flightRepository.findById(NON_EXISTENT_ID)).thenReturn(Optional.empty());

      assertThrows(
          EntityNotFoundException.class,
          () -> flightService.getFlightById(NON_EXISTENT_ID),
          "checking if EntityNotFoundException is thrown when flight not found");
      verify(flightRepository, times(1)).findById(NON_EXISTENT_ID);
    }
  }
}
