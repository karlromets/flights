package com.cgi.flights.dto.response;

public record AirportResponseDTO(
    Long id,
    String name,
    String cityName,
    String countryName
) {}