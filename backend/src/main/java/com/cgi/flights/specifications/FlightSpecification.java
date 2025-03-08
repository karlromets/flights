package com.cgi.flights.specifications;

import static java.util.Objects.nonNull;

import com.cgi.flights.model.Flight;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

// https://medium.com/predictly-on-tech/complex-jpa-queries-with-specifications-46654f118fe3

@Data
@Builder
public class FlightSpecification implements Specification<Flight> {

  private String searchTerm;

  private Double price;

  private String arrivalCountry;
  private String arrivalCity;
  private String arrivalAirport;

  private String departureCountry;
  private String departureCity;
  private String departureAirport;

  private Instant departureTime;
  private Instant arrivalTime;

  public static final String DEPARTURE_AIRPORT_FIELD = "departureAirport";
  public static final String ARRIVAL_AIRPORT_FIELD = "arrivalAirport";
  public static final String COUNTRY_FIELD = "country";
  public static final String CITY_FIELD = "city";
  public static final String NAME_FIELD = "name";

  @SuppressWarnings("null")
  @Override
  public Predicate toPredicate(Root<Flight> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
    // Create a list to hold all our filter conditions
    List<Predicate> predicates = new ArrayList<>();

    // Method flow:
    // 1. Takes filter criteria (price, airports, times)
    // 2. Converts them into database query conditions (Predicates)
    // 3. Combines those conditions into a single WHERE clause

    // The parameters:
    // - root: represents the Flight entity and gives access to its fields
    // - query: represents the overall query being built
    // - cb: a factory for creating conditions (equals, like, between, etc.)

    // From 0.0 to price
    if (nonNull(price)) {
      predicates.add(betweenDouble(cb, root.get("price"), 0.0, price));
    }

    // If departureAirport given, query LIKE departureAirport
    if (StringUtils.isNotBlank(departureAirport)) {
      predicates.add(like(cb, root.get(DEPARTURE_AIRPORT_FIELD).get(NAME_FIELD), departureAirport));
    }

    // If departureCity given, query LIKE departureCity
    if (StringUtils.isNotBlank(departureCity)) {
      predicates.add(like(cb, root.get(DEPARTURE_AIRPORT_FIELD).get(CITY_FIELD), departureCity));
    }

    // If departureCountry given, query LIKE departureCountry
    if (StringUtils.isNotBlank(departureCountry)) {
      predicates.add(
          like(
              cb,
              root.get(DEPARTURE_AIRPORT_FIELD).get(COUNTRY_FIELD).get(NAME_FIELD),
              departureCountry));
    }

    // If arrivalAirport given, query LIKE arrivalAirport
    if (StringUtils.isNotBlank(arrivalAirport)) {
      predicates.add(like(cb, root.get(ARRIVAL_AIRPORT_FIELD).get(NAME_FIELD), arrivalAirport));
    }

    // If arrivalCity given, query LIKE arrivalCity
    if (StringUtils.isNotBlank(arrivalCity)) {
      predicates.add(like(cb, root.get(ARRIVAL_AIRPORT_FIELD).get(CITY_FIELD), arrivalCity));
    }

    // If arrivalCountry given, query LIKE arrivalCountry
    if (StringUtils.isNotBlank(arrivalCountry)) {
      predicates.add(
          like(
              cb,
              root.get(ARRIVAL_AIRPORT_FIELD).get(COUNTRY_FIELD).get(NAME_FIELD),
              arrivalCountry));
    }

    // If departureTime given, query >= departureTime
    if (nonNull(departureTime)) {
      predicates.add(cb.greaterThanOrEqualTo(root.get("departureTime"), departureTime));
    }

    // If arrivalTime given, query <= arrivalTime
    if (nonNull(arrivalTime)) {
      predicates.add(cb.lessThanOrEqualTo(root.get("arrivalTime"), arrivalTime));
    }

    // If searchTerm given, query LIKE searchTerm
    if (StringUtils.isNoneBlank(searchTerm)) {
      Predicate departureCountryPred =
          like(
              cb, root.get(DEPARTURE_AIRPORT_FIELD).get(COUNTRY_FIELD).get(NAME_FIELD), searchTerm);
      Predicate departureCityPred =
          like(cb, root.get(DEPARTURE_AIRPORT_FIELD).get(CITY_FIELD).get(NAME_FIELD), searchTerm);
      Predicate departureAirportPred =
          like(cb, root.get(DEPARTURE_AIRPORT_FIELD).get(NAME_FIELD), searchTerm);

      Predicate arrivalCountryPred =
          like(cb, root.get(ARRIVAL_AIRPORT_FIELD).get(COUNTRY_FIELD).get(NAME_FIELD), searchTerm);
      Predicate arrivalCityPred =
          like(cb, root.get(ARRIVAL_AIRPORT_FIELD).get(CITY_FIELD).get(NAME_FIELD), searchTerm);
      Predicate arrivalAirportPred =
          like(cb, root.get(ARRIVAL_AIRPORT_FIELD).get(NAME_FIELD), searchTerm);

      predicates.add(
          cb.or(
              departureCountryPred,
              departureCityPred,
              departureAirportPred,
              arrivalCountryPred,
              arrivalCityPred,
              arrivalAirportPred));
    }

    // return value becomes WHERE clause
    return cb.and(predicates.toArray(new Predicate[0]));
  }

  private Predicate like(CriteriaBuilder cb, Path<String> field, String searchTerm) {
    return cb.like(cb.lower(field), "%" + searchTerm.toLowerCase() + "%");
  }

  private Predicate betweenDouble(CriteriaBuilder cb, Path<Double> field, Double min, Double max) {
    return cb.between(field, min, max);
  }
}
