package com.cgi.flights.model;

import java.util.List;

import jakarta.persistence.Column;
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
public class Airport {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String name;

  @ManyToOne
  @JoinColumn(name = "country_id")
  private Country country;

  @ManyToOne
  @JoinColumn(name = "city_id")
  private City city;

  @OneToMany(mappedBy = "departureAirport")
  private List<Flight> departingFlights;

  @OneToMany(mappedBy = "arrivalAirport")
  private List<Flight> arrivingFlights;
}
