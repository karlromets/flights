package com.cgi.flights.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
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

  public boolean isWindow() {
    return plane.getWindowColumns().contains(columnLetter);
  }

  public boolean isAisle() {
    return plane.getAisleColumns().contains(columnLetter);
  }

  public boolean isExitRow() {
    return plane.getExitRows().contains(rowNumber);
  }
}
