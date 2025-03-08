import { getFlightController } from "@/api/flight-controller";
import { FlightDetails } from "@/components/features/seat-map/flight-details";
import SeatMap from "@/components/features/seat-map/seat-map";
import { SeatPreferencesProvider } from "@/components/features/seat-map/seat-preferences-context";
import SeatSuggestor from "@/components/features/seat-map/seat-suggestor";
// import { SeatMapper } from "@/lib/utils";

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

  return (
    <>
      <div className="container mx-auto">
        <SeatPreferencesProvider>
          <div className="grid grid-cols-2">
            <div>
              <FlightDetails flight={flight} />
              <SeatSuggestor />
            </div>
            <div className="flex flex-col gap-2 mx-auto w-[500px]">
              <SeatMap seats={seats} />
            </div>
          </div>
        </SeatPreferencesProvider>
      </div>
    </>
  );
}
