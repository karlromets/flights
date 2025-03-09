package com.cgi.flights.mapper;

import java.time.Instant;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cgi.flights.dto.request.FlightFilterDTO;
import com.cgi.flights.specifications.FlightSpecification;

@Mapper(componentModel = "spring")
public interface SpecificationMapper {

  @Mapping(target = "departureAirport", source = "departureAirport")
  @Mapping(target = "arrivalAirport", source = "arrivalAirport")
  @Mapping(target = "departureCity", source = "departureCity")
  @Mapping(target = "arrivalCity", source = "arrivalCity")
  @Mapping(target = "departureCountry", source = "departureCountry")
  @Mapping(target = "arrivalCountry", source = "arrivalCountry")
  @Mapping(target = "departureTime", expression = "java(parseInstant(filter.departureTime()))")
  @Mapping(target = "arrivalTime", expression = "java(parseInstant(filter.arrivalTime()))")
  @Mapping(target = "searchTerm", source = "searchTerm")
  @Mapping(target = "price", source = "price")
  FlightSpecification toFlightSpecification(FlightFilterDTO filter);

  default Instant parseInstant(String timeString) {
    return timeString != null ? Instant.parse(timeString) : null;
  }
}
