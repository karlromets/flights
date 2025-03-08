import { SeatResponseDTO } from "@/api/types";
import { clsx, type ClassValue } from "clsx";
import { twMerge } from "tailwind-merge";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export class SeatMapper {
  private seats: SeatResponseDTO[];

  constructor(seats: SeatResponseDTO[]) {
    this.seats = seats;
  }

  getTotal(): number {
    return this.seats.length;
  }

  getOccupied(): number {
    return this.seats.filter((s) => s.isOccupied).length;
  }

  getAvailable(): number {
    return this.seats.filter((s) => !s.isOccupied).length;
  }

  getColumns(): string[] {
    const cols = new Set<string>();
    this.seats.forEach((seat) => cols.add(seat.columnLetter));
    return Array.from(cols);
  }

  getRows(): number[] {
    const cols = new Set<number>();
    this.seats.forEach((seat) => cols.add(seat.rowNumber));
    return Array.from(cols);
  }
}
