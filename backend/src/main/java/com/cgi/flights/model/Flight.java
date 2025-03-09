package com.cgi.flights.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.Instant;
import java.util.List;
import lombok.Data;

@Data
@Entity
public class Flight {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  private Double price;

  @ManyToOne
  @JoinColumn(name = "departure_airport_id")
  private Airport departureAirport;

  @ManyToOne
  @JoinColumn(name = "arrival_airport_id")
  private Airport arrivalAirport;

  private Instant departureTime;
  private Instant arrivalTime;

  @OneToMany(mappedBy = "flight")
  private List<Booking> bookings;

  @ManyToOne
  @JoinColumn(name = "plane_id")
  private Plane plane;
}
