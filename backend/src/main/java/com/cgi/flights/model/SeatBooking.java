package com.cgi.flights.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "seat_booking")
public class SeatBooking {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "booking_id")
  private Booking booking;
  @ManyToOne
  @JoinColumn(name = "flight_id")
  private Flight flight;
  @ManyToOne
  @JoinColumn(name = "seat_id")
  private Seat seat;
}
