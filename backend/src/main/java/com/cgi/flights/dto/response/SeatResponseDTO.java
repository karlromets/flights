package com.cgi.flights.dto.response;

import com.cgi.flights.model.SeatClass;

public record SeatResponseDTO(
    Long id,
    Long rowNumber,
    String columnLetter,
    SeatClass seatClass
) {
}
