package com.cgi.flights.dto.response;

import java.util.List;

public record PlaneResponseDTO(Long id, String name, String producer, List<SeatResponseDTO> seats) {}
