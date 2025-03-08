import { SeatResponseDTO } from "@/api/types";
import { clsx, type ClassValue } from "clsx";
import { twMerge } from "tailwind-merge";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export interface SeatPreferences {
  count: number;
  isWindow: boolean;
  isAisle: boolean;
  isExitRow: boolean;
  adjacentSeats: boolean;
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
    return this.seats.filter((s) => s.isWindow);
  }

  getAisleSeats(): SeatResponseDTO[] {
    return this.seats.filter((s) => s.isAisle);
  }

  getExitRowSeats(): SeatResponseDTO[] {
    return this.seats.filter((s) => s.isExitRow);
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

  getSuggestions(prefs: SeatPreferences): SeatResponseDTO[] {
    // Filter out occupied seats
    let suggestions = this.seats.filter((s) => !s.isOccupied);

    if (prefs.isAisle) {
      suggestions = suggestions.filter((s) => s.isAisle);
    } else if (prefs.isWindow) {
      suggestions = suggestions.filter((s) => s.isWindow);
    }
    if (prefs.isExitRow) {
      suggestions = suggestions.filter((s) => s.isExitRow);
    }

    if (prefs.adjacentSeats) {
      // Group seats by row
      const seatsByRow = new Map<number, SeatResponseDTO[]>();

      suggestions.forEach((s) => {
        if (!seatsByRow.has(s.rowNumber)) {
          seatsByRow.set(s.rowNumber, []);
        }
        seatsByRow.get(s.rowNumber)!.push(s);
      });

      const possibleSequences: SeatResponseDTO[][] = [];

      const c = prefs.count;

      seatsByRow.forEach((rowSeats) => {
        if (rowSeats.length > prefs.count) {
          for (let i = 0; i < rowSeats.length; i++) {
            let consecutive = true;

            for (let j = c - 1; j > 0; j--) {
              const possible = rowSeats.find((seat) => seat.id === rowSeats[i].id + j);
              if (!possible) {
                consecutive = false;
                break;
              }
            }

            if (consecutive) {
              possibleSequences.push(rowSeats.slice(i, i + prefs.count));
            }
          }
        }
      });

      if (possibleSequences.length > 0) {
        suggestions = possibleSequences.flat();
      } else {
        return [];
      }
    }

    if (suggestions.length < prefs.count) {
      return [];
    }

    return suggestions;
  }
}
