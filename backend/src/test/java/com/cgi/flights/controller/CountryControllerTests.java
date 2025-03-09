package com.cgi.flights.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cgi.flights.dto.response.CountryResponseDTO;
import com.cgi.flights.service.CountryService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class CountryControllerTests {
  private MockMvc mockMvc;

  @Mock private CountryService countryService;

  @InjectMocks private CountryController countryController;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(countryController).build();
  }

  @Nested
  @DisplayName("getCountries Tests")
  class GetCountriesTests {
    @Test
    @DisplayName("Should return all countries")
    void getCountries_WhenRequested_ReturnsAllCountries() throws Exception {
      when(countryService.getCountries())
          .thenReturn(List.of(new CountryResponseDTO(1L, "Estonia")));

      mockMvc
          .perform(get("/api/countries"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(1)))
          .andExpect(jsonPath("$[0].id", is(1)))
          .andExpect(jsonPath("$[0].name", is("Estonia")));
    }
  }
}
