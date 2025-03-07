"use client";

import { useState, useEffect, useRef } from "react";
import { getFlightController } from "@/api/flight-controller";
import type { GetFlightsParams, PagingResultFlightResponseDTO } from "@/api/types";
import { columns } from "@/components/features/flights-table/columns";
import { DataTable } from "@/components/features/flights-table/data-table";
import FlightFilters from "@/components/features/flights-table/filters";
const flightController = getFlightController();

async function getFlights(params: GetFlightsParams) {
  try {
    const response = await flightController.getFlights(params);
    return response;
  } catch (error) {
    console.error(error);
  }
}

interface PaginatedFlightsTableProps {
  initialData: PagingResultFlightResponseDTO;
}

export function PaginatedFlightsTable({ initialData }: PaginatedFlightsTableProps) {
  const [flights, setFlights] = useState(initialData);
  const [pagination, setPagination] = useState({
    pageIndex: 0,
    pageSize: 10,
  });
  const initialFetchDone = useRef(false);

  useEffect(() => {
    async function fetchFlights() {
      if (pagination.pageIndex === 0 && !initialFetchDone.current) {
        initialFetchDone.current = true;
        return;
      }

      const response = await getFlights({
        page: pagination.pageIndex,
        size: pagination.pageSize,
      });
      setFlights(response!.data);
    }

    fetchFlights();
  }, [pagination, initialData]);

  return (
    <>
    <div className="container mx-auto">
      <FlightFilters />
    </div>
    <div className="container mx-auto">
      {flights && flights.content && flights.content.length > 0 ? (
        <DataTable
          columns={columns}
          data={flights.content}
          totalPages={flights.totalPages || 1}
          onPaginationChange={setPagination}
        />
      ) : (
        <div className="flex flex-col items-center justify-center h-screen bg-background">
          <h1>No flights found</h1>
        </div>
      )}
    </div>
    </>
  );
}
