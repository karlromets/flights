"use client";

import { ColumnDef } from "@tanstack/react-table";
import type { AirportResponseDTO, FlightResponseDTO, PlaneResponseDTO } from "@/api/types";
import { format } from "date-fns";

export const columns: ColumnDef<FlightResponseDTO>[] = [
  {
    accessorKey: "id",
    header: () => <div className="text-right">ID</div>,
    cell: ({ row }) => {
      const id = row.getValue("id") as number;
      return <div className="text-right">{id}</div>;
    },
  },
  {
    accessorKey: "price",
    header: () => <div className="text-right">Price</div>,
    cell: ({ row }) => {
      const amount = parseFloat(row.getValue("price"));
      const formatted = new Intl.NumberFormat("en-US", {
        style: "currency",
        currency: "USD",
      }).format(amount);

      return <div className="text-right font-medium">{formatted}</div>;
    },
  },
  {
    accessorKey: "departureAirport",
    header: () => <div className="text-right">From</div>,
    cell: ({ row }) => {
      const departureAirport = row.getValue("departureAirport") as AirportResponseDTO;
      return <div className="text-right">{departureAirport.cityName}, {departureAirport.countryName}</div>;
    },
  },
  {
    accessorKey: "arrivalAirport",
    header: () => <div className="text-right">To</div>,
    cell: ({ row }) => {
      const arrivalAirport = row.getValue("arrivalAirport") as AirportResponseDTO;
      return <div className="text-right">{arrivalAirport.cityName}, {arrivalAirport.countryName}</div>;
    },
  },
  {
    accessorKey: "departureTime",
    header: () => <div className="text-right">Departure Time</div>,
    cell: ({ row }) => {
      const departureTime = row.getValue("departureTime") as string;
      return <div className="text-right">{format(departureTime, "EEE, MMM d")}</div>;
    },
  },
  {
    accessorKey: "arrivalTime",
    header: () => <div className="text-right">Arrival Time</div>,
    cell: ({ row }) => {
      const arrivalTime = row.getValue("arrivalTime") as string;
      return <div className="text-right">{format(arrivalTime, "EEE, MMM d")}</div>;
    },
  },
  {
    accessorKey: "plane",
    header: () => <div className="text-right">Plane</div>,
    cell: ({ row }) => {
      const plane = row.getValue("plane") as PlaneResponseDTO;
      return <div className="text-right">{plane.name}</div>;
    },
  },
];
