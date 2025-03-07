import { getFlightController } from "@/api/flight-controller";
import { PaginatedFlightsTable } from "@/components/features/flights-table/paginated-flights-table";

async function getInitialFlights() {
  const flightController = getFlightController();
  try {
    const response = await flightController.getFlights({ page: 0, size: 10 });
    return response.data;
  } catch (error) {
    console.error(error);
    return { content: [], totalPages: 0 };
  }
}

export default async function Home() {
  const initialFlights = await getInitialFlights();

  return (
    <div className="flex flex-col items-center justify-center h-screen bg-background">
      <div className="container mx-auto">
        <PaginatedFlightsTable initialData={initialFlights} />
      </div>
    </div>
  );
}
