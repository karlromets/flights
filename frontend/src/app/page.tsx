import { getFlightController } from "@/api/flight-controller";
import { PaginatedFlightsTable } from "@/components/features/flights-table/paginated-flights-table";
import { Metadata } from "next";

export const metadata: Metadata = {
  title: "Flights",
};

export const dynamic = "force-dynamic";
export const fetchCache = "force-no-store";

async function getInitialFlights() {
  const flightController = getFlightController();
  try {
    const response = await flightController.getFlights({ page: 0, size: 10 });
    return response;
  } catch (error) {
    console.error(error);
    return { content: [], totalPages: 0 };
  }
}

export default async function Home() {
  const initialFlights = await getInitialFlights();

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-background">
      <div className="container mx-auto">
          <PaginatedFlightsTable initialData={initialFlights} />
      </div>
    </div>
  );
}
