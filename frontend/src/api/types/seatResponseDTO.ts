/**
 * Generated by orval v7.6.0 🍺
 * Do not edit manually.
 * OpenAPI definition
 * OpenAPI spec version: v0
 */
import type { SeatClass } from "./seatClass";

export interface SeatResponseDTO {
  id?: number;
  rowNumber?: number;
  columnLetter?: string;
  seatClass?: SeatClass;
  isWindow?: boolean;
  isAisle?: boolean;
  isExitRow?: boolean;
}
