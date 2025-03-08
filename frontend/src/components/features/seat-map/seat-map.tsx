"use client";

import { SeatResponseDTO } from "@/api/types";
import { Button } from "@/components/ui/button";
import { SeatMapper } from "@/lib/utils";
import { SeatPreferencesContext } from "./seat-preferences-context";
import { useContext, useEffect, useMemo, useState } from "react";

interface SeatMapProps {
  seats: SeatResponseDTO[];
}

export default function SeatMap({ seats }: SeatMapProps) {
  const sm = useMemo(() => new SeatMapper(seats), [seats]);

  const { preferences } = useContext(SeatPreferencesContext);
  const [suggestions, setSuggestions] = useState<SeatResponseDTO[]>([]);

  useEffect(() => {
    setSuggestions(sm.getSuggestions(preferences));
  }, [preferences, sm]);

  return (
    <>
      <div className="flex">
        <span className="font-bold text-sm h-9 pr-[70px] py-2"></span>
        {sm.getColumns().map((col) => (
          <span className="font-bold text-sm h-9 px-[15.5px] py-2" key={col}>
            {col}
          </span>
        ))}
      </div>
      {sm.getRows().map((row) => (
        <div key={row} className="flex justify-center gap-2">
          <span className="font-bold text-sm h-9 px-[15.5px] pt-1" key={row}>
            {row}
          </span>
          {sm.getSeatsByRow(row).map((seat) => {
            const isMatch = suggestions.some((s) => s.id === seat.id);
            return (
              <Button
                variant={seat.isOccupied ? "destructive" : isMatch ? "default" : "secondary"}
                key={seat.id}
                className="w-6 h-6 p-4"
              >
                {seat.id}
              </Button>
            );
          })}
        </div>
      ))}
    </>
  );
}
