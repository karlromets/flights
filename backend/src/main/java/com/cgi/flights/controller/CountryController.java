package com.cgi.flights.controller;

import com.cgi.flights.dto.response.CountryResponseDTO;
import com.cgi.flights.service.CountryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/countries")
public class CountryController {
  private final CountryService countryService;

  @GetMapping
  public ResponseEntity<List<CountryResponseDTO>> getCountries() {
    List<CountryResponseDTO> countries = countryService.getCountries();
    return ResponseEntity.ok(countries);
  }
}
