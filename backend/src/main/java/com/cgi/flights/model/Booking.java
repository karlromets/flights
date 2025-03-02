package com.cgi.flights.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
import lombok.Data;

@Data
@Entity
public class Booking {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "flight_id")
  private Flight flight;

  private String passengerName;
  private String passengerEmail;
  private String passengerPhone;

  private Instant createdAt = Instant.now();
}
