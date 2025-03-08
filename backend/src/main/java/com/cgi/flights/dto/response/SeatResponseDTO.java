package com.cgi.flights.dto.response;

public record SeatResponseDTO(
    Long id,
    Long rowNumber,
    String columnLetter,
    Double price,
    String SeatClass,
    boolean isWindow,
    boolean isAisle,
    boolean isExitRow,
    boolean isOccupied) {}
