package com.cgi.flights.repository;

import com.cgi.flights.model.Seat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
  List<Seat> findByPlaneId(Long planeId);
}
