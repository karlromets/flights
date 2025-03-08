"use client";
import { toast } from "sonner";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import { Form, FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { format } from "date-fns";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { Calendar } from "@/components/ui/calendar";
import { Calendar as CalendarIcon } from "lucide-react";
import { Slider } from "@/components/ui/slider";
import LocationSelector from "@/components/ui/location-input";
import { GetFlightsParams } from "@/api/types";

const formSchema = z.object({
  searchTerm: z.string().min(1).min(3).max(150).optional(),
  departureTime: z.coerce.date().optional(),
  arrivalTime: z.coerce.date().optional(),
  price: z.number().min(10).max(2000).optional(),
  departureCountry: z.tuple([z.string(), z.string().optional()]).optional(),
  arrivalCountry: z.tuple([z.string(), z.string().optional()]).optional(),
});

interface FlightFiltersProps {
  onFilterChange: (filters: GetFlightsParams) => void;
}

export default function FlightFilters({ onFilterChange }: FlightFiltersProps) {
  //   const [departureCountryName, setDepartureCountryName] = useState<string>("");
  //   const [departureStateName, setDepartureStateName] = useState<string>("");
  //   const [arrivalCountryName, setArrivalCountryName] = useState<string>("");
  //   const [arrivalStateName, setArrivalStateName] = useState<string>("");

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    // defaultValues: {
    //   departureTime: new Date(),
    //   arrivalTime: new Date(),
    // },
  });

  function onSubmit(values: z.infer<typeof formSchema>) {
    try {
      const apiParams: GetFlightsParams = {
        page: 0,
        size: 10,
        ...(values.searchTerm && { query: values.searchTerm }),
        ...(values.departureTime && { departureDate: values.departureTime.toISOString() }),
        ...(values.arrivalTime && { arrivalDate: values.arrivalTime.toISOString() }),
        ...(values.price && { price: values.price }),
        ...(values.departureCountry?.[0] && { departureCountry: values.departureCountry[0] }),
        ...(values.arrivalCountry?.[0] && { arrivalCountry: values.arrivalCountry[0] }),
      };

      onFilterChange(apiParams);
      // console.log(values);
      toast(
        <pre className="mt-2 w-[340px] rounded-md bg-slate-950 p-4">
          <code className="text-white">{JSON.stringify(values, null, 2)}</code>
        </pre>
      );
    } catch (error) {
      console.error("Form submission error", error);
      toast.error("Failed to submit the form. Please try again.");
    }
  }

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="w-full max-w-5xl mx-auto p-6">
        <div className="bg-card rounded-lg border shadow-sm p-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <FormField
              control={form.control}
              name="searchTerm"
              render={({ field }) => (
                <FormItem>
                  <FormLabel className="text-base">Search</FormLabel>
                  <FormControl>
                    <Input placeholder="Search flights..." type="text" className="w-full" {...field} value={field.value || ''} />
                  </FormControl>
                  <FormDescription>Search flights by any keyword</FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="price"
              render={({ field: { value, onChange } }) => (
                <FormItem>
                  <FormLabel className="text-base">Price Range</FormLabel>
                  <FormControl>
                    <div className="pt-2">
                      <Slider
                        min={10}
                        max={2000}
                        step={10}
                        defaultValue={[2000]}
                        onValueChange={(vals) => {
                          onChange(vals[0]);
                        }}
                      />
                    </div>
                  </FormControl>
                  <FormDescription>Maximum price: ${value || 2000}</FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="departureTime"
              render={({ field }) => (
                <FormItem className="flex flex-col">
                  <FormLabel className="text-base">Departure Date</FormLabel>
                  <Popover>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant={"outline"}
                          className={cn(
                            "w-full justify-start text-left font-normal",
                            !field.value && "text-muted-foreground"
                          )}
                        >
                          {field.value ? format(field.value, "PPP") : <span>Pick a date</span>}
                          <CalendarIcon className="ml-auto h-4 w-4 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-auto p-0" align="start">
                      <Calendar mode="single" selected={field.value} onSelect={field.onChange} initialFocus />
                    </PopoverContent>
                  </Popover>
                  <FormDescription>Select departure date</FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="arrivalTime"
              render={({ field }) => (
                <FormItem className="flex flex-col">
                  <FormLabel className="text-base">Arrival Date</FormLabel>
                  <Popover>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant={"outline"}
                          className={cn(
                            "w-full justify-start text-left font-normal",
                            !field.value && "text-muted-foreground"
                          )}
                        >
                          {field.value ? format(field.value, "PPP") : <span>Pick a date</span>}
                          <CalendarIcon className="ml-auto h-4 w-4 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-auto p-0" align="start">
                      <Calendar mode="single" selected={field.value} onSelect={field.onChange} initialFocus />
                    </PopoverContent>
                  </Popover>
                  <FormDescription>Select arrival date</FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="departureCountry"
              render={({ field }) => (
                <FormItem>
                  <FormLabel className="text-base">Departure Location</FormLabel>
                  <FormControl>
                    <LocationSelector
                      onCountryChange={(country) => {
                        form.setValue(field.name, [country?.name || "", ""]);
                      }}
                    />
                  </FormControl>
                  <FormDescription>Select departure country</FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="arrivalCountry"
              render={({ field }) => (
                <FormItem>
                  <FormLabel className="text-base">Arrival Location</FormLabel>
                  <FormControl>
                    <LocationSelector
                      onCountryChange={(country) => {
                        form.setValue(field.name, [country?.name || "", ""]);
                      }}
                    />
                  </FormControl>
                  <FormDescription>Select arrival country</FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>

          <div className="mt-8 flex justify-end">
            <Button type="submit" className="w-full sm:w-auto">
              Search Flights
            </Button>
          </div>
        </div>
      </form>
    </Form>
  );
}
