"use client"

import { ColumnDef } from "@tanstack/react-table"
import type { FlightResponseDTO } from "@/api/types";

export const columns: ColumnDef<FlightResponseDTO>[] = [
  {
    accessorKey: "id",
    header: "ID",
  },
  {
    accessorKey: "price",
    header: "Price",
  },
  {
    accessorKey: "departureAirport",
    header: "Departure Airport",
  },
  {
    accessorKey: "arrivalAirport",
    header: "Arrival Airport",
  },
  {
    accessorKey: "departureTime",
    header: "Departure Time",
  },
  {
    accessorKey: "arrivalTime",
    header: "Arrival Time",
  },
  {
    accessorKey: "plane",
    header: "Plane",
  },
]
