package com.cgi.flights.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
public class Plane {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String name;

  @ManyToOne
  @JoinColumn(name = "producer_id")
  private PlaneProducer producer;

  @OneToMany(mappedBy = "plane", cascade = CascadeType.ALL)
  private List<Seat> seats;

  private List<String> windowColumns;
  private List<String> aisleColumns;
  private List<Long> exitRows;
}
