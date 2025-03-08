import { getFlightController } from "@/api/flight-controller";
import { Button } from "@/components/ui/button";
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

  if (!("plane" in flight) || !flight.plane.seats) {
    return <h1>Flight details not available</h1>;
  }
  const seats = flight.plane.seats;
  const sm = new SeatMapper(seats);

  console.log(seats);
  console.log("Total", sm.getTotal());
  console.log("Occupied", sm.getOccupied());
  console.log("Available", sm.getAvailable());
  console.log("Columns", sm.getColumns());
  console.log("Rows", sm.getRows());
  console.log("Seats by row", sm.getSeatsByRow(9));
  console.log("First Class", sm.getSeatsByClass("First Class"));
  console.log("Window", sm.getWindowSeats());
  console.log("Aisle", sm.getAisleSeats());
  console.log("Exit", sm.getExitRowSeats());

  const totalRows = sm.getRows();

  return (
    <>
      <div className="container mx-auto">
        <div className="flex flex-col gap-2">
          {totalRows.map((row) => (
            <div key={row} className="flex justify-center gap-2">
              {sm.getSeatsByRow(row).map((seat) => (
                <Button variant={seat.isOccupied ? "destructive" : "default"} key={seat.id} className="w-6 h-6 p-4">
                  {seat.id}
                </Button>
              ))}
            </div>
          ))}
        </div>
      </div>
    </>
  );
}
