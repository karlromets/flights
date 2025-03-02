package com.cgi.flights.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Seat {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "plane_id")
  private Plane plane;

  private Long rowNumber;
  private String columnLetter;

  @ManyToOne
  @JoinColumn(name = "seat_class_id")
  private SeatClass seatClass;

  @OneToMany(mappedBy = "seat")
  private List<SeatBooking> bookings;
}
