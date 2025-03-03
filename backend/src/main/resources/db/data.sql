-- Countries
INSERT INTO country (id, name) VALUES 
(1, 'United States'),
(2, 'United Kingdom'),
(3, 'France'),
(4, 'Germany'),
(5, 'Japan'),
(6, 'Australia'),
(7, 'Canada'),
(8, 'Spain'),
(9, 'Italy'),
(10, 'Netherlands');

-- Cities
INSERT INTO city (id, name) VALUES 
(1, 'New York'),
(2, 'London'),
(3, 'Paris'),
(4, 'Berlin'),
(5, 'Tokyo'),
(6, 'Sydney'),
(7, 'Toronto'),
(8, 'Madrid'),
(9, 'Rome'),
(10, 'Amsterdam'),
(11, 'Los Angeles'),
(12, 'Chicago'),
(13, 'San Francisco'),
(14, 'Miami');

-- Airports
INSERT INTO airport (id, name, country_id, city_id) VALUES 
(1, 'John F. Kennedy International Airport', 1, 1),
(2, 'Heathrow Airport', 2, 2),
(3, 'Charles de Gaulle Airport', 3, 3),
(4, 'Berlin Brandenburg Airport', 4, 4),
(5, 'Narita International Airport', 5, 5),
(6, 'Sydney Airport', 6, 6),
(7, 'Toronto Pearson International Airport', 7, 7),
(8, 'Adolfo Suárez Madrid–Barajas Airport', 8, 8),
(9, 'Leonardo da Vinci International Airport', 9, 9),
(10, 'Amsterdam Airport Schiphol', 10, 10),
(11, 'Los Angeles International Airport', 1, 11),
(12, 'O''Hare International Airport', 1, 12),
(13, 'San Francisco International Airport', 1, 13),
(14, 'Miami International Airport', 1, 14);

-- Plane Producers
INSERT INTO plane_producer (id, name) VALUES 
(1, 'Boeing'),
(2, 'Airbus'),
(3, 'Embraer'),
(4, 'Bombardier');

-- Planes
INSERT INTO plane (id, name, producer_id) VALUES 
(1, 'Boeing 747', 1),
(2, 'Airbus A380', 2),
(3, 'Boeing 787 Dreamliner', 1),
(4, 'Airbus A320', 2),
(5, 'Embraer E190', 3),
(6, 'Bombardier CRJ900', 4),
(7, 'Boeing 737', 1),
(8, 'Airbus A330', 2);

-- Seat Classes
INSERT INTO seat_class (id, name, price_multiplier) VALUES 
(1, 'Economy', 1.0),
(2, 'Premium Economy', 1.5),
(3, 'Business', 2.5),
(4, 'First Class', 4.0);

-- Seats for Boeing 747 (Plane ID 1)
-- First Class (4 seats)
INSERT INTO seat (id, row_number, column_letter, plane_id, seat_class_id) VALUES
(1, 1, 'A', 1, 4),
(2, 1, 'B', 1, 4),
(3, 1, 'C', 1, 4),
(4, 1, 'D', 1, 4),
-- Business Class (8 seats)
(5, 2, 'A', 1, 3),
(6, 2, 'B', 1, 3),
(7, 2, 'C', 1, 3),
(8, 2, 'D', 1, 3),
(9, 3, 'A', 1, 3),
(10, 3, 'B', 1, 3),
(11, 3, 'C', 1, 3),
(12, 3, 'D', 1, 3),
-- Premium Economy (12 seats)
(13, 4, 'A', 1, 2),
(14, 4, 'B', 1, 2),
(15, 4, 'C', 1, 2),
(16, 4, 'D', 1, 2),
(17, 4, 'E', 1, 2),
(18, 4, 'F', 1, 2),
(19, 5, 'A', 1, 2),
(20, 5, 'B', 1, 2),
(21, 5, 'C', 1, 2),
(22, 5, 'D', 1, 2),
(23, 5, 'E', 1, 2),
(24, 5, 'F', 1, 2),
-- Economy (24 seats)
(25, 6, 'A', 1, 1),
(26, 6, 'B', 1, 1),
(27, 6, 'C', 1, 1),
(28, 6, 'D', 1, 1),
(29, 6, 'E', 1, 1),
(30, 6, 'F', 1, 1),
(31, 7, 'A', 1, 1),
(32, 7, 'B', 1, 1),
(33, 7, 'C', 1, 1),
(34, 7, 'D', 1, 1),
(35, 7, 'E', 1, 1),
(36, 7, 'F', 1, 1),
(37, 8, 'A', 1, 1),
(38, 8, 'B', 1, 1),
(39, 8, 'C', 1, 1),
(40, 8, 'D', 1, 1),
(41, 8, 'E', 1, 1),
(42, 8, 'F', 1, 1),
(43, 9, 'A', 1, 1),
(44, 9, 'B', 1, 1),
(45, 9, 'C', 1, 1),
(46, 9, 'D', 1, 1),
(47, 9, 'E', 1, 1),
(48, 9, 'F', 1, 1);

-- Seats for Airbus A320 (Plane ID 4) - Simplified for brevity
-- Business Class (8 seats)
INSERT INTO seat (id, row_number, column_letter, plane_id, seat_class_id) VALUES
(49, 1, 'A', 4, 3),
(50, 1, 'B', 4, 3),
(51, 1, 'C', 4, 3),
(52, 1, 'D', 4, 3),
(53, 2, 'A', 4, 3),
(54, 2, 'B', 4, 3),
(55, 2, 'C', 4, 3),
(56, 2, 'D', 4, 3),
-- Economy (16 seats)
(57, 3, 'A', 4, 1),
(58, 3, 'B', 4, 1),
(59, 3, 'C', 4, 1),
(60, 3, 'D', 4, 1),
(61, 4, 'A', 4, 1),
(62, 4, 'B', 4, 1),
(63, 4, 'C', 4, 1),
(64, 4, 'D', 4, 1),
(65, 5, 'A', 4, 1),
(66, 5, 'B', 4, 1),
(67, 5, 'C', 4, 1),
(68, 5, 'D', 4, 1),
(69, 6, 'A', 4, 1),
(70, 6, 'B', 4, 1),
(71, 6, 'C', 4, 1),
(72, 6, 'D', 4, 1);

-- Flights
INSERT INTO flight (id, price, departure_time, arrival_time, departure_airport_id, arrival_airport_id, plane_id) VALUES 
-- Transatlantic flights
(1, 850.00, '2023-12-15 08:00:00', '2023-12-15 20:00:00', 1, 2, 1), -- JFK to Heathrow
(2, 900.00, '2023-12-15 22:00:00', '2023-12-16 10:00:00', 2, 1, 1), -- Heathrow to JFK
(3, 780.00, '2023-12-16 09:30:00', '2023-12-16 23:00:00', 1, 3, 3), -- JFK to Charles de Gaulle
(4, 820.00, '2023-12-16 14:00:00', '2023-12-17 04:00:00', 3, 1, 3), -- Charles de Gaulle to JFK

-- European flights
(5, 320.00, '2023-12-17 07:15:00', '2023-12-17 09:30:00', 2, 3, 4), -- Heathrow to Charles de Gaulle
(6, 310.00, '2023-12-17 11:45:00', '2023-12-17 14:00:00', 3, 2, 4), -- Charles de Gaulle to Heathrow
(7, 280.00, '2023-12-18 08:30:00', '2023-12-18 11:15:00', 3, 4, 5), -- Charles de Gaulle to Berlin
(8, 290.00, '2023-12-18 13:00:00', '2023-12-18 15:45:00', 4, 3, 5), -- Berlin to Charles de Gaulle

-- Domestic US flights
(9, 420.00, '2023-12-19 06:30:00', '2023-12-19 09:45:00', 1, 11, 7), -- JFK to LAX
(10, 410.00, '2023-12-19 11:30:00', '2023-12-19 14:45:00', 11, 1, 7), -- LAX to JFK
(11, 250.00, '2023-12-20 08:00:00', '2023-12-20 10:00:00', 1, 12, 6), -- JFK to Chicago
(12, 240.00, '2023-12-20 12:30:00', '2023-12-20 14:30:00', 12, 1, 6), -- Chicago to JFK

-- Asian routes
(13, 1200.00, '2023-12-21 00:30:00', '2023-12-21 18:30:00', 1, 5, 3), -- JFK to Tokyo
(14, 1250.00, '2023-12-22 22:00:00', '2023-12-23 16:00:00', 5, 1, 3), -- Tokyo to JFK

-- Australia routes
(15, 1500.00, '2023-12-23 01:00:00', '2023-12-24 06:00:00', 11, 6, 8), -- LAX to Sydney
(16, 1550.00, '2023-12-25 08:00:00', '2023-12-26 13:00:00', 6, 11, 8); -- Sydney to LAX

-- Bookings
INSERT INTO booking (id, flight_id, passenger_name, passenger_email, passenger_phone, created_at) VALUES 
(1, 1, 'John Smith', 'john.smith@example.com', '+1-555-123-4567', '2023-11-15 10:30:00'),
(2, 1, 'Emma Johnson', 'emma.johnson@example.com', '+1-555-234-5678', '2023-11-16 14:45:00'),
(3, 2, 'Michael Brown', 'michael.brown@example.com', '+44-20-1234-5678', '2023-11-17 09:15:00'),
(4, 3, 'Sophia Williams', 'sophia.williams@example.com', '+1-555-345-6789', '2023-11-18 16:20:00'),
(5, 5, 'James Davis', 'james.davis@example.com', '+44-20-2345-6789', '2023-11-19 11:10:00'),
(6, 9, 'Olivia Miller', 'olivia.miller@example.com', '+1-555-456-7890', '2023-11-20 13:25:00'),
(7, 13, 'William Wilson', 'william.wilson@example.com', '+1-555-567-8901', '2023-11-21 08:40:00'),
(8, 15, 'Ava Moore', 'ava.moore@example.com', '+1-555-678-9012', '2023-11-22 15:55:00');

-- Seat Bookings
INSERT INTO seat_booking (id, booking_id, flight_id, seat_id) VALUES 
(1, 1, 1, 1),  -- John Smith, First Class on JFK to Heathrow
(2, 2, 1, 5),  -- Emma Johnson, Business Class on JFK to Heathrow
(3, 3, 2, 2),  -- Michael Brown, First Class on Heathrow to JFK
(4, 4, 3, 13), -- Sophia Williams, Premium Economy on JFK to Charles de Gaulle
(5, 5, 5, 49), -- James Davis, Business Class on Heathrow to Charles de Gaulle
(6, 6, 9, 25), -- Olivia Miller, Economy on JFK to LAX
(7, 7, 13, 6), -- William Wilson, Business Class on JFK to Tokyo
(8, 8, 15, 57); -- Ava Moore, Economy on LAX to Sydney
