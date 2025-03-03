package com.cgi.flights.repository;

import com.cgi.flights.model.Plane;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaneRepository extends JpaRepository<Plane, Long> {}
