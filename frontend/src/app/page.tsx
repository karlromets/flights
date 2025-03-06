import { getFlightController } from "@/api/flight-controller";
import type { FlightResponseDTO, GetFlightsParams } from "@/api/types";
import { columns } from "@/components/features/flights-table/columns";
import { DataTable } from "@/components/features/flights-table/data-table";

async function getFlights(params: GetFlightsParams) {
  const flightController = getFlightController();
  try {
    const response = await flightController.getFlights(params);
    if (response.data.content) {
      return response.data.content as FlightResponseDTO[];
    }
    return [];
  } catch (error) {
    console.error(error);
  }
}

export default async function Home() {
  const flights = await getFlights({});
  console.log(flights);

  return (
    <div className="flex flex-col items-center justify-center h-screen bg-background">
      <div className="container mx-auto">
        {flights && flights.length > 0 ? (
          <DataTable columns={columns} data={flights} />
        ) : (
          <div className="flex flex-col items-center justify-center h-screen bg-background">
            <h1>No flights found</h1>
          </div>
        )}
      </div>
    </div>
  );
}
