// AI

import { format, differenceInHours, differenceInMinutes } from "date-fns";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Separator } from "@/components/ui/separator";
import { FlightResponseDTO } from "@/api/types";
import { ArrowRight, Clock, Plane } from "lucide-react";

interface FlightDetailsProps {
  flight: FlightResponseDTO;
}

export function FlightDetails({ flight }: FlightDetailsProps) {
  const departureDate = new Date(flight.departureTime);
  const arrivalDate = new Date(flight.arrivalTime);

  // Calculate flight duration
  const durationHours = differenceInHours(arrivalDate, departureDate);
  const durationMinutes = differenceInMinutes(arrivalDate, departureDate) % 60;

  return (
    <Card className="w-full">
      <CardHeader className="bg-primary/5">
        <CardTitle className="flex justify-between items-center">
          <div className="flex items-center gap-2 py-4">
            <Plane className="h-5 w-5" />
            <span>Flight #{flight.id}</span>
          </div>
          <Badge variant="outline" className="text-lg font-bold">
            ${flight.price}
          </Badge>
        </CardTitle>
      </CardHeader>
      <CardContent className="pt-6">
        <div className="grid grid-cols-[1fr_auto_1fr] gap-4 items-center">
          {/* Departure */}
          <div className="space-y-1">
            <p className="text-2xl font-bold">{flight.departureAirport.cityName}</p>
            <p className="text-sm text-muted-foreground">{flight.departureAirport.name}</p>
            <p className="text-sm text-muted-foreground">{flight.departureAirport.countryName}</p>
            <p className="text-lg font-medium mt-2">{format(departureDate, "h:mm a")}</p>
            <p className="text-sm text-muted-foreground">{format(departureDate, "EEEE, MMM d, yyyy")}</p>
          </div>

          {/* Flight info */}
          <div className="flex flex-col items-center gap-2">
            <div className="relative w-full flex items-center justify-center">
              <Separator className="w-full absolute" />
              <div className="bg-white dark:bg-background z-10 px-2 flex items-center gap-1">
                <Clock className="h-4 w-4 text-muted-foreground" />
                <span className="text-sm text-muted-foreground whitespace-nowrap">
                  {durationHours}h {durationMinutes}m
                </span>
              </div>
            </div>
            <ArrowRight className="h-5 w-5" />
            <Badge variant="outline" className="mt-2">
              {flight.plane.name}
            </Badge>
          </div>

          {/* Arrival */}
          <div className="space-y-1 text-right">
            <p className="text-2xl font-bold">{flight.arrivalAirport.cityName}</p>
            <p className="text-sm text-muted-foreground">{flight.arrivalAirport.name}</p>
            <p className="text-sm text-muted-foreground">{flight.arrivalAirport.countryName}</p>
            <p className="text-lg font-medium mt-2">{format(arrivalDate, "h:mm a")}</p>
            <p className="text-sm text-muted-foreground">{format(arrivalDate, "EEEE, MMM d, yyyy")}</p>
          </div>
        </div>
      </CardContent>
    </Card>
  );
}
