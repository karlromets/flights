package com.cgi.flights.dto.response;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingsResponseDTO {
  private Long id;
  private Long seatId;
  private Instant createdAt;
}
