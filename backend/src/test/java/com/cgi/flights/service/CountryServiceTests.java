package com.cgi.flights.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cgi.flights.dto.response.CountryResponseDTO;
import com.cgi.flights.model.Country;
import com.cgi.flights.repository.CountryRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CountryServiceTests {
  @Mock private CountryRepository countryRepository;

  @InjectMocks private CountryService countryService;

  private static final String COUNTRY_1_NAME = "Estonia";
  private static final Long COUNTRY_1_ID = 1L;
  private static final String COUNTRY_2_NAME = "Latvia";
  private static final Long COUNTRY_2_ID = 2L;

  @Nested
  @DisplayName("getCountries Tests")
  class GetCountriesTests {
    @Test
    @DisplayName("Should return all countries")
    void getCountries_WhenCalled_ReturnsAllCountries() {
      when(countryRepository.findAll()).thenReturn(createCountries());

      List<CountryResponseDTO> result = countryService.getCountries();

      assertAll(
          () -> assertThat(result).as("checking if the result has size 2").hasSize(2),
          () ->
              assertThat(result.get(0).name())
                  .as("checking if the first country is Estonia")
                  .isEqualTo(COUNTRY_1_NAME),
          () ->
              assertThat(result.get(0).id())
                  .as("checking if the first country id is 1")
                  .isEqualTo(COUNTRY_1_ID),
          () ->
              assertThat(result.get(1).name())
                  .as("checking if the second country is Latvia")
                  .isEqualTo(COUNTRY_2_NAME),
          () ->
              assertThat(result.get(1).id())
                  .as("checking if the second country id is 2")
                  .isEqualTo(COUNTRY_2_ID),
          () -> verify(countryRepository, times(1)).findAll());
    }
  }

  private List<Country> createCountries() {
    Country country1 = new Country();
    country1.setId(COUNTRY_1_ID);
    country1.setName(COUNTRY_1_NAME);

    Country country2 = new Country();
    country2.setId(COUNTRY_2_ID);
    country2.setName(COUNTRY_2_NAME);

    return List.of(country1, country2);
  }
}
