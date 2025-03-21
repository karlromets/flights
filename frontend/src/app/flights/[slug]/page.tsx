import { getFlightController } from "@/api/flight-controller";
import { FlightDetails } from "@/components/features/seat-map/flight-details";
import SeatMap from "@/components/features/seat-map/seat-map";
import { SeatPreferencesProvider } from "@/components/features/seat-map/seat-preferences-context";
import SeatSuggestor from "@/components/features/seat-map/seat-suggestor";
import SelectedSeats from "@/components/features/seat-map/selected-seats";
import { Metadata } from "next";

export async function generateMetadata({ params }: FlightPageProps): Promise<Metadata> {
  const { slug } = await params;
  return {
    title: `Flight ${parseInt(slug)}`,
  };
}

async function getFlight(id: number) {
  const flightController = getFlightController();
  try {
    const response = await flightController.getFlightById(id);
    return response;
  } catch (error) {
    console.error(error);
    return null;
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

  if (!flight || !flight.plane || !flight.plane.seats) {
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
              <SelectedSeats />
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
