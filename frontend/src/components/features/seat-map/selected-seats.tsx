"use client";

import { useContext, useEffect, useState } from "react";
import { SeatPreferencesContext } from "./seat-preferences-context";
import { SeatResponseDTO } from "@/api/types";
import { Separator } from "@/components/ui/separator";

export default function SelectedSeats() {
  const { selectedSeats } = useContext(SeatPreferencesContext);
  const [totalPrice, setTotalPrice] = useState(0);

  useEffect(() => {
    const total = selectedSeats.reduce((acc, s) => acc + s.price, 0);
    setTotalPrice(total);
  }, [selectedSeats]);

  return (
    <>
        <div className="mt-4 p-4 border rounded-lg">
          <h3 className="text-lg font-bold mb-2">Selected Seats</h3>
          <Separator className="my-2" />
          <div className="space-y-2">
            {selectedSeats.map((seat: SeatResponseDTO) => (
              <div key={seat.id} className="flex justify-between items-center">
                <span>
                  Seat {seat.id} | {seat.rowNumber}
                  {seat.columnLetter} | {seat.SeatClass}
                </span>
                <span className="text-emerald-500">${seat.price.toFixed(2)}</span>
              </div>
            ))}
          </div>
          <div className="mt-4 text-right">
            <span className="font-bold">Total Price:</span>{" "}
            <span className="text-emerald-500">${totalPrice.toFixed(2)}</span>
          </div>
        </div>
    </>
  );
}
