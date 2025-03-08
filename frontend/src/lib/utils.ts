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

  getWindowSeats(): SeatResponseDTO[] {
    return this.seats.filter((s) => s.isWindow)
  }
  
  getAisleSeats(): SeatResponseDTO[] {
    return this.seats.filter((s) => s.isAisle)
  }

  getExitRowSeats(): SeatResponseDTO[] {
    return this.seats.filter((s) => s.isExitRow)
  }

  getColumns(): string[] {
    const cols = new Set<string>();
    this.seats.forEach((s) => cols.add(s.columnLetter));
    return Array.from(cols);
  }

  getRows(): number[] {
    const cols = new Set<number>();
    this.seats.forEach((s) => cols.add(s.rowNumber));
    return Array.from(cols);
  }

  getSeatsByRow(row: number): SeatResponseDTO[] {
    return this.seats.filter((s) => s.rowNumber === row);
  }

  getSeatsByClass(seatClass: string): SeatResponseDTO[] {
    return this.seats.filter((s) => s.SeatClass === seatClass);
  }
}
