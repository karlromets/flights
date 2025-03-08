"use client";

import { useContext, useState, useEffect } from "react";
import { SeatPreferencesContext } from "./seat-preferences-context";
import { Input } from "@/components/ui/input";
import { SeatToggleItem } from "./seat-toggles";
import { Blinds, Expand, Columns3, PanelLeftClose } from "lucide-react";
import { Label } from "@/components/ui/label";
import { Separator } from "@/components/ui/separator";

export default function SeatSuggestor() {
  const { preferences, setPreferences } = useContext(SeatPreferencesContext);
  const [count, setCount] = useState(preferences.count);
  const [isWindow, setIsWindow] = useState(preferences.isWindow);
  const [isExitRow, setIsExitRow] = useState(preferences.isExitRow);
  const [isAisle, setIsAisle] = useState(preferences.isAisle);
  const [adjacentSeats, setAdjacentSeats] = useState(preferences.adjacentSeats);

  useEffect(() => {
    setPreferences({
      count,
      isWindow,
      isExitRow,
      isAisle,
      adjacentSeats,
    });
  }, [count, isWindow, isExitRow, isAisle, adjacentSeats, setPreferences]);

  return (
    <div>
      <h2 className="text-2xl font-semibold mt-5 mb-2">Seat Preferences</h2>
      <Separator className="mb-5" />
      <div className="space-y-4">
        <div className="flex items-end gap-2">
          <div>
            <Label>Count</Label>
            <Input
              type="number"
              value={count}
              min={1}
              max={10}
              onChange={(e) => setCount(parseInt(e.target.value))}
              placeholder="Number of seats"
              className="mt-2"
            />
          </div>
          <div>
            <SeatToggleItem
              label="Adjacent seats"
              icon={<PanelLeftClose className="h-4 w-4" />}
              checked={adjacentSeats}
              onCheckedChange={setAdjacentSeats}
              disabled={count < 2}
            />
          </div>
        </div>

        <div className="flex flex-col gap-4">
          <SeatToggleItem
            label="Window Seat"
            icon={<Blinds className="h-4 w-4" />}
            checked={isWindow}
            onCheckedChange={setIsWindow}
            disabled={isAisle}
          />
          <SeatToggleItem
            label="Extra leg room"
            icon={<Expand className="h-4 w-4" />}
            checked={isExitRow}
            onCheckedChange={setIsExitRow}
          />
          <SeatToggleItem
            label="Aisle seat"
            icon={<Columns3 className="h-4 w-4" />}
            checked={isAisle}
            onCheckedChange={setIsAisle}
            disabled={isWindow}
          />
        </div>
      </div>
    </div>
  );
}
