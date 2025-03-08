package com.cgi.flights.dto.response;

import jakarta.validation.constraints.NotNull;

public record SeatResponseDTO(
    @NotNull Long id,
    @NotNull Long rowNumber,
    @NotNull String columnLetter,
    @NotNull Double price,
    @NotNull String SeatClass,
    @NotNull boolean isWindow,
    @NotNull boolean isAisle,
    @NotNull boolean isExitRow,
    @NotNull boolean isOccupied) {}
