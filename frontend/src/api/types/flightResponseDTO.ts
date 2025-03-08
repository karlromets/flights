/**
 * Generated by orval v7.6.0 🍺
 * Do not edit manually.
 * OpenAPI definition
 * OpenAPI spec version: v0
 */
import type { AirportResponseDTO } from "./airportResponseDTO";
import type { PlaneResponseDTO } from "./planeResponseDTO";

export interface FlightResponseDTO {
  id?: number;
  price?: number;
  departureAirport?: AirportResponseDTO;
  arrivalAirport?: AirportResponseDTO;
  departureTime?: string;
  arrivalTime?: string;
  plane?: PlaneResponseDTO;
}
