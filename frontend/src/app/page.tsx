import { getFlightController } from "@/api/flight-controller";
import type { GetFlightsParams } from "@/api/types";

async function getFlights(params: GetFlightsParams) {
  const flightController = getFlightController();
  try {
    const response = await flightController.getFlights(params);
    return response.data;
  } catch (error) {
    console.error(error);
  }
}

export default async function Home() {
  const flights = await getFlights({});
  console.log(flights);

  return (
    <div className="flex flex-col items-center justify-center h-screen bg-background">
      <h1>Init</h1>
    </div>
  );
}
