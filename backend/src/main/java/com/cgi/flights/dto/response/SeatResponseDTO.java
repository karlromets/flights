package com.cgi.flights.dto.response;

import com.cgi.flights.model.SeatClass;

public record SeatResponseDTO(
    Long id,
    Long rowNumber,
    String columnLetter,
    Double price,
    String SeatClass,
    boolean isWindow,
    boolean isAisle,
    boolean isExitRow) {}
