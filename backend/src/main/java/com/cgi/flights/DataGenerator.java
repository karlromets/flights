package com.cgi.flights;

import com.cgi.flights.model.*;
import com.cgi.flights.repository.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataGenerator implements CommandLineRunner {
  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(DataGenerator.class);
  private final BookingRepository bookingRepository;
  private final FlightRepository flightRepository;
  private final SeatRepository seatRepository;
  private final SeatBookingRepository seatBookingRepository;
  private final CountryRepository countryRepository;
  private final AirportRepository airportRepository;
  private final PlaneRepository planeRepository;
  private final Random random = new Random();

  public DataGenerator(
      BookingRepository bookingRepository,
      FlightRepository flightRepository,
      SeatRepository seatRepository,
      SeatBookingRepository seatBookingRepository,
      CountryRepository countryRepository,
      AirportRepository airportRepository,
      PlaneRepository planeRepository) {
    this.bookingRepository = bookingRepository;
    this.flightRepository = flightRepository;
    this.seatRepository = seatRepository;
    this.seatBookingRepository = seatBookingRepository;
    this.countryRepository = countryRepository;
    this.airportRepository = airportRepository;
    this.planeRepository = planeRepository;
  }

  int flightTransactionCount = 0;
  int bookingTransactionCount = 0;
  int seatTransactionCount = 0;
  int seatBookingTransactionCount = 0;
  int countryTransactionCount = 0;
  int airportTransactionCount = 0;
  int planeTransactionCount = 0;

  @Override
  public void run(String... args) {
    // Get all flights
    List<Country> allCountries = countryRepository.findAll();
    List<Airport> allAirports = airportRepository.findAll();
    List<Plane> allPlanes = planeRepository.findAll();
    countryTransactionCount++;
    airportTransactionCount++;
    planeTransactionCount++;

    // Group airports by country for easier access
    Map<Long, List<Airport>> airportsByCountry = new HashMap<>();
    for (Airport airport : allAirports) {
      Long countryId = airport.getCountry().getId();
      if (!airportsByCountry.containsKey(countryId)) {
        airportsByCountry.put(countryId, new ArrayList<>());
      }
      airportsByCountry.get(countryId).add(airport);
    }

    // Generate flights for each country and between countries
    List<Flight> generatedFlights = new ArrayList<>();

    // Generate domestic flights within each country
    for (Country country : allCountries) {
      List<Airport> countryAirports = airportsByCountry.get(country.getId());
      if (countryAirports != null && countryAirports.size() > 1) {
        generatedFlights.addAll(generateDomesticFlights(countryAirports, allPlanes));
      }
    }

    // Generate international flights between countries
    generatedFlights.addAll(generateInternationalFlights(airportsByCountry, allPlanes));

    flightTransactionCount++;
    flightRepository.saveAll(generatedFlights);

    // Generate bookings for the flights
    generateBookings(generatedFlights);

    logger.info("Successfully generated {} flights and bookings", generatedFlights.size());
    logger.info(
        "Transaction counts\n"
            + " flight: {}\n"
            + " booking: {}\n"
            + " seatBooking: {}\n"
            + " country: {}\n"
            + " airport: {}\n"
            + " plane: {}",
        flightTransactionCount,
        bookingTransactionCount,
        seatBookingTransactionCount,
        countryTransactionCount,
        airportTransactionCount,
        planeTransactionCount);
  }

  private List<Flight> generateDomesticFlights(List<Airport> airports, List<Plane> planes) {
    List<Flight> flights = new ArrayList<>();

    for (int i = 0; i < airports.size(); i++) {
      for (int j = 0; j < airports.size(); j++) {
        if (i != j && random.nextDouble() < 0.7) { // 70% chance to create a flight between airports
          Airport departure = airports.get(i);
          Airport arrival = airports.get(j);

          // Generate 1-3 flights between these airports
          int numFlights = random.nextInt(3) + 1;

          for (int k = 0; k < numFlights; k++) {
            Plane plane = getRandomItem(planes);
            if (plane == null) {
              logger.warn("Skipping flight creation due to missing plane");
              continue;
            }
            Flight flight = createFlight(departure, arrival, plane, 100.0, 800.0);
            flights.add(flight);
          }
        }
      }
    }

    return flights;
  }

  private List<Flight> generateInternationalFlights(
      Map<Long, List<Airport>> airportsByCountry, List<Plane> planes) {
    List<Flight> flights = new ArrayList<>();
    List<Country> countries = countryRepository.findAll();
    countryTransactionCount++;

    for (int i = 0; i < countries.size(); i++) {
      for (int j = 0; j < countries.size(); j++) {
        if (i != j && random.nextDouble() < 0.5) { // 50% chance to create flights between countries
          Country departureCountry = countries.get(i);
          Country arrivalCountry = countries.get(j);

          List<Airport> departureAirports = airportsByCountry.get(departureCountry.getId());
          List<Airport> arrivalAirports = airportsByCountry.get(arrivalCountry.getId());

          if (departureAirports != null
              && !departureAirports.isEmpty()
              && arrivalAirports != null
              && !arrivalAirports.isEmpty()) {

            flights.addAll(generateCountryPairFlights(planes, departureAirports, arrivalAirports));
          }
        }
      }
    }

    return flights;
  }

  private List<Flight> generateCountryPairFlights(
      List<Plane> planes,
      List<Airport> departureAirports,
      List<Airport> arrivalAirports) {
    // Choose random airports from each country
    Airport departure = getRandomItem(departureAirports);
    Airport arrival = getRandomItem(arrivalAirports);

    if (departure == null || arrival == null) {
      logger.warn("Skipping flight creation due to missing airport");
      return new ArrayList<>(); // Return empty list instead of void
    }

    // Generate 1-2 flights between these airports
    int numFlights = random.nextInt(2) + 1;

    List<Flight> flightsToSave = new ArrayList<>();

    for (int k = 0; k < numFlights; k++) {
      Plane plane = getRandomItem(planes);
      if (plane == null) {
        logger.warn("Skipping flight creation due to missing plane");
        continue;
      }
      // International flights are more expensive
      Flight flight = createFlight(departure, arrival, plane, 300.0, 1500.0);
      flightsToSave.add(flight);

      // Create return flight
      if (random.nextDouble() < 0.9) { // 90% chance for return flight
        Flight returnFlight = createReturnFlight(flight);
        flightsToSave.add(returnFlight);
      }
    }
    return flightsToSave;
  }

  private Flight createFlight(
      Airport departure, Airport arrival, Plane plane, double minPrice, double maxPrice) {
    Flight flight = new Flight();
    flight.setDepartureAirport(departure);
    flight.setArrivalAirport(arrival);
    flight.setPlane(plane);

    // Generate random price between min and max
    double price = minPrice + (maxPrice - minPrice) * random.nextDouble();
    flight.setPrice(Math.round(price * 100.0) / 100.0); // Round to 2 decimal places

    // Generate departure time within the next 30 days
    LocalDateTime now = LocalDateTime.now();
    int daysAhead = random.nextInt(30) + 1;
    int hourOfDay = random.nextInt(24);
    int minuteOfHour = random.nextInt(4) * 15; // 0, 15, 30, or 45 minutes

    LocalDateTime departureDateTime =
        now.plusDays(daysAhead)
            .withHour(hourOfDay)
            .withMinute(minuteOfHour)
            .withSecond(0)
            .withNano(0);

    Instant departureTime = departureDateTime.toInstant(ZoneOffset.UTC);
    flight.setDepartureTime(departureTime);

    // Calculate flight duration based on distance (simplified)
    long durationMinutes;
    if (departure.getCountry().getId().equals(arrival.getCountry().getId())) {
      // Domestic flight: 1-3 hours
      durationMinutes = (random.nextInt(2 * 60) + 60);
    } else {
      // International flight: 3-14 hours
      durationMinutes = (random.nextInt(11 * 60) + 3 * 60);
    }

    Instant arrivalTime = departureTime.plus(durationMinutes, ChronoUnit.MINUTES);
    flight.setArrivalTime(arrivalTime);

    return flight;
  }

  private Flight createReturnFlight(Flight outboundFlight) {
    Flight returnFlight = new Flight();
    returnFlight.setDepartureAirport(outboundFlight.getArrivalAirport());
    returnFlight.setArrivalAirport(outboundFlight.getDepartureAirport());
    returnFlight.setPlane(outboundFlight.getPlane());

    // Slight price variation for return flight
    double priceVariation = 0.9 + (random.nextDouble() * 0.2); // 0.9 to 1.1
    returnFlight.setPrice(Math.round(outboundFlight.getPrice() * priceVariation * 100.0) / 100.0);

    // Set departure time to be after arrival time of outbound flight
    Instant outboundArrival = outboundFlight.getArrivalTime();

    // Return flight departs 2-24 hours after outbound flight arrives
    long hoursLater = random.nextInt(23) + 2;
    Instant returnDeparture = outboundArrival.plus(hoursLater, ChronoUnit.HOURS);
    returnFlight.setDepartureTime(returnDeparture);

    // Same duration as outbound flight, approximately
    long durationMinutes =
        ChronoUnit.MINUTES.between(
            outboundFlight.getDepartureTime(), outboundFlight.getArrivalTime());

    // Add small variation to duration (+/- 30 minutes)
    durationMinutes += (random.nextInt(61) - 30);

    Instant returnArrival = returnDeparture.plus(durationMinutes, ChronoUnit.MINUTES);
    returnFlight.setArrivalTime(returnArrival);

    return returnFlight;
  }

  private void generateBookings(List<Flight> flights) {
    logger.info("Generating bookings for {} flights", flights.size());

    Map<Long, List<Seat>> seatsByPlane = new HashMap<>();

    planeRepository.findAll().forEach(plane -> {
      List<Seat> seats = seatRepository.findByPlaneId(plane.getId());
      seatTransactionCount++;
      seatsByPlane.putIfAbsent(plane.getId(), seats);
    });
    planeTransactionCount++;

    List<Booking> bookingsToSave = new ArrayList<>();
    List<SeatBooking> seatBookingsToSave = new ArrayList<>();

    // For each flight, decide on a booking pattern
    for (Flight flight : flights) {
      // Get seats for this plane
      List<Seat> availableSeats = seatsByPlane.get(flight.getPlane().getId());

      if (availableSeats.isEmpty()) {
        logger.debug("No seats available for plane ID {}", flight.getPlane().getId());
        continue;
      }

      // Decide on occupancy level
      double occupancyRate = determineOccupancyRate();
      int totalSeatsToBook = (int) (availableSeats.size() * occupancyRate);

      if (totalSeatsToBook == 0 && random.nextDouble() < 0.1) {
        // 10% chance for at least one booking even on "empty" flights
        totalSeatsToBook = random.nextInt(3) + 1;
      }

      // Book seats in groups (simulates group bookings)
      List<Seat> bookedSeats = new ArrayList<>();
      int remainingSeats = totalSeatsToBook;

      while (remainingSeats > 0 && bookedSeats.size() < availableSeats.size()) {
        // Create a new booking
        Booking booking = new Booking();
        booking.setFlight(flight);
        bookingsToSave.add(booking);

        // Decide how many seats in this booking (1-4)
        int seatsInBooking = Math.min(random.nextInt(4) + 1, remainingSeats);
        seatsInBooking = Math.min(seatsInBooking, availableSeats.size() - bookedSeats.size());

        // Book seats
        seatBookingsToSave.addAll(
            bookSeatsInBatch(flight, availableSeats, bookedSeats, booking, seatsInBooking));

        remainingSeats -= seatsInBooking;
      }

      logger.info(
          "Flight {}: Booked {}/{} seats ({}% occupancy)",
          flight.getId(),
          bookedSeats.size(),
          availableSeats.size(),
          Math.round((bookedSeats.size() * 100.0) / availableSeats.size()));
    }

    bookingRepository.saveAll(bookingsToSave);
    bookingTransactionCount++;
    seatBookingRepository.saveAll(seatBookingsToSave);
    seatBookingTransactionCount++;
  }

  private List<SeatBooking> bookSeatsInBatch(
      Flight flight,
      List<Seat> availableSeats,
      List<Seat> bookedSeats,
      Booking booking,
      int seatsInBooking) {
    List<SeatBooking> seatBookings = new ArrayList<>();
    for (int i = 0; i < seatsInBooking; i++) {
      Seat seat = getRandomUnbookedSeat(availableSeats, bookedSeats);
      if (seat == null) break; // No more available seats

      bookedSeats.add(seat);

      // Create seat booking
      SeatBooking seatBooking = new SeatBooking();
      seatBooking.setBooking(booking);
      seatBooking.setFlight(flight);
      seatBooking.setSeat(seat);

      seatBookings.add(seatBooking);
    }
    return seatBookings;
  }

  private double determineOccupancyRate() {
    // Distribution of flight occupancy:
    // - 10% of flights: Empty or nearly empty (0-10% occupancy)
    // - 20% of flights: Low occupancy (10-30%)
    // - 40% of flights: Medium occupancy (30-70%)
    // - 20% of flights: High occupancy (70-90%)
    // - 10% of flights: Full or nearly full (90-100%)
    double rand = random.nextDouble();

    // if (rand < 0.1) {
    // return random.nextDouble() * 0.1; // 0-10%
    /* } else  */ if (rand < 0.3) {
      return 0.1 + random.nextDouble() * 0.2; // 10-30%
    } else if (rand < 0.7) {
      return 0.3 + random.nextDouble() * 0.4; // 30-70%
    } else if (rand < 0.9) {
      return 0.7 + random.nextDouble() * 0.2; // 70-90%
    } else {
      return 0.9 + random.nextDouble() * 0.1; // 90-100%
    }
  }

  // Helper method to get a random item from a list
  private <T> T getRandomItem(List<T> list) {
    if (list == null || list.isEmpty()) {
      return null;
    }
    return list.get(random.nextInt(list.size()));
  }

  // Helper method to get a random seat that hasn't been booked yet
  private Seat getRandomUnbookedSeat(List<Seat> availableSeats, List<Seat> bookedSeats) {
    if (bookedSeats.size() >= availableSeats.size()) {
      return null; // All seats are booked
    }

    Seat seat;
    do {
      seat = getRandomItem(availableSeats);
    } while (bookedSeats.contains(seat));

    return seat;
  }
}
