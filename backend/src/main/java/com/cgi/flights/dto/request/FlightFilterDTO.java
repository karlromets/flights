package com.cgi.flights.dto.request;

public record FlightFilterDTO(
    String departureAirport,
    String arrivalAirport,
    String departureCity,
    String arrivalCity,
    String departureCountry,
    String arrivalCountry,
    String departureTime,
    String arrivalTime,
    String searchTerm,
    Double price) {}
