package com.cgi.flights.service;

import com.cgi.flights.dto.response.CountryResponseDTO;
import com.cgi.flights.repository.CountryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CountryService {
  private final CountryRepository countryRepository;

  public List<CountryResponseDTO> getCountries() {
    return countryRepository.findAll().stream()
        .map(country -> new CountryResponseDTO(country.getId(), country.getName()))
        .toList();
  }
}
