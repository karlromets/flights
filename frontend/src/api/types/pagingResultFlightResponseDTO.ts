/**
 * Generated by orval v7.6.0 🍺
 * Do not edit manually.
 * OpenAPI definition
 * OpenAPI spec version: v0
 */
import type { FlightResponseDTO } from './flightResponseDTO';

export interface PagingResultFlightResponseDTO {
  content?: FlightResponseDTO[];
  totalPages?: number;
  totalElements?: number;
  size?: number;
  page?: number;
  empty?: boolean;
}
