import { getFlightController } from "@/api/flight-controller";
import { SeatMapper } from "@/lib/utils";

async function getFlight(id: number) {
  const flightController = getFlightController();
  try {
    const response = await flightController.getFlightById(id);
    return response.data;
  } catch (error) {
    console.error(error);
    return { content: [], totalPages: 0 };
  }
}

interface FlightPageProps {
  params: Promise<{
    slug: string;
  }>;
}

export default async function FlightPage({ params }: FlightPageProps) {
  const { slug } = await params;
  const flight = await getFlight(parseInt(slug));
  
  if(!('plane' in flight) || !flight.plane.seats) {
    return <h1>Flight details not available</h1>
  }
  const seats = flight.plane.seats;
  const sm = new SeatMapper(seats);

  console.log(seats);
  console.log("Total", sm.getTotal());
  console.log("Occupied", sm.getOccupied());
  console.log("Available", sm.getAvailable());
  console.log("Columns", sm.getColumns());
  console.log("Rows", sm.getRows());

  return (
    <>
      {/* {flight && flight.plane.seats.map((seat => {

  }))} */}
    </>
  );
}
