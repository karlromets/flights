"use client";

import { SeatPreferences } from "@/lib/utils";
import { createContext, useState } from "react";

interface SeatPreferencesContextType {
  preferences: SeatPreferences;
  setPreferences: (prefs: SeatPreferences) => void;
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
});

export function SeatPreferencesProvider({ children }: { children: React.ReactNode }) {
  const [preferences, setPreferences] = useState<SeatPreferences>({
    count: 1,
    isWindow: false,
    isAisle: false,
    isExitRow: false,
    adjacentSeats: false,
  });

  return (
    <SeatPreferencesContext.Provider value={{ preferences, setPreferences }}>
      {children}
    </SeatPreferencesContext.Provider>
  );
}
