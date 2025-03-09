"use client";

import { SeatResponseDTO } from "@/api/types";
import { SeatPreferences } from "@/lib/utils";
import { createContext, useState } from "react";

interface SeatPreferencesContextType {
  preferences: SeatPreferences;
  setPreferences: (prefs: SeatPreferences) => void;
  selectedSeats: SeatResponseDTO[];
  setSelectedSeats: (seats: SeatResponseDTO[]) => void;
}

export const SeatPreferencesContext = createContext<SeatPreferencesContextType>({
  preferences: {
    count: 1,
    isWindow: false,
    isAisle: false,
    isExitRow: false,
    adjacentSeats: false,
  },
  setPreferences: () => {},
  selectedSeats: [],
  setSelectedSeats: () => {},
});

export function SeatPreferencesProvider({ children }: { children: React.ReactNode }) {
  const [preferences, setPreferences] = useState<SeatPreferences>({
    count: 1,
    isWindow: false,
    isAisle: false,
    isExitRow: false,
    adjacentSeats: false,
  });
  const [selectedSeats, setSelectedSeats] = useState<SeatResponseDTO[]>([]);
  
  return (
    <SeatPreferencesContext.Provider
      value={{ preferences, setPreferences, selectedSeats, setSelectedSeats }}
    >
      {children}
    </SeatPreferencesContext.Provider>
  );
}
