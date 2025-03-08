import { getFlightController } from "@/api/flight-controller";

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
  console.log(flight);
}
