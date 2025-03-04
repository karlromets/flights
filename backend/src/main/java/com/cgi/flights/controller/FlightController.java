package com.cgi.flights.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cgi.flights.dto.response.FlightResponseDTO;
import com.cgi.flights.service.FlightService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flights")
public class FlightController {
    private final FlightService flightService;

    @GetMapping
    public List<FlightResponseDTO> getFlights() {
        return flightService.getAllFlights();
    }
}
