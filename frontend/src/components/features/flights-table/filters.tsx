"use client";
import { useState } from "react";
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

const formSchema = z.object({
  searchTerm: z.string().min(1).min(3).max(150).optional(),
  departureTime: z.coerce.date().optional(),
  arrivalTime: z.coerce.date().optional(),
  price: z.number().min(10).max(2000).optional(),
  departureCountry: z.tuple([z.string(), z.string().optional()]).optional(),
  arrivalCountry: z.tuple([z.string(), z.string().optional()]).optional(),
});

export default function FlightFilters() {
  //   const [departureCountryName, setDepartureCountryName] = useState<string>("");
  //   const [departureStateName, setDepartureStateName] = useState<string>("");
  //   const [arrivalCountryName, setArrivalCountryName] = useState<string>("");
  //   const [arrivalStateName, setArrivalStateName] = useState<string>("");

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      departureTime: new Date(),
      arrivalTime: new Date(),
    },
  });

  function onSubmit(values: z.infer<typeof formSchema>) {
    try {
      console.log(values);
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
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8 max-w-3xl mx-auto py-10">
        <FormField
          control={form.control}
          name="searchTerm"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Search</FormLabel>
              <FormControl>
                <Input placeholder="Estonia" type="text" {...field} />
              </FormControl>
              <FormDescription>Search flights</FormDescription>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="departureTime"
          render={({ field }) => (
            <FormItem className="flex flex-col">
              <FormLabel>Departure</FormLabel>
              <Popover>
                <PopoverTrigger asChild>
                  <FormControl>
                    <Button
                      variant={"outline"}
                      className={cn("w-[240px] pl-3 text-left font-normal", !field.value && "text-muted-foreground")}
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
              <FormDescription>Date when the plane leaves</FormDescription>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="arrivalTime"
          render={({ field }) => (
            <FormItem className="flex flex-col">
              <FormLabel>Arrival</FormLabel>
              <Popover>
                <PopoverTrigger asChild>
                  <FormControl>
                    <Button
                      variant={"outline"}
                      className={cn("w-[240px] pl-3 text-left font-normal", !field.value && "text-muted-foreground")}
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
              <FormDescription>Date when the plane arrives</FormDescription>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="price"
          render={({ field: { value, onChange } }) => (
            <FormItem>
              <FormLabel>Price - {value || 2000}</FormLabel>
              <FormControl>
                <Slider
                  min={10}
                  max={2000}
                  step={10}
                  defaultValue={[2000]}
                  onValueChange={(vals) => {
                    onChange(vals[0]);
                  }}
                />
              </FormControl>
              <FormDescription>Adjust the price by sliding.</FormDescription>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="departureCountry"
          render={({ field }) => (
            <FormItem>
              <FormLabel>From</FormLabel>
              <FormControl>
                <LocationSelector
                  onCountryChange={(country) => {
                    // setDepartureCountryName(country?.name || "");
                    form.setValue(field.name, [country?.name || "", /* departureStateName || */ ""]);
                  }}
                  /* onStateChange={(state) => {
                    setDepartureStateName(state?.name || "");
                    form.setValue(field.name, [departureCountryName || "", state?.name || ""]);
                  }} */
                />
              </FormControl>
              <FormDescription>Where will you take off from?</FormDescription>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="arrivalCountry"
          render={({ field }) => (
            <FormItem>
              <FormLabel>To</FormLabel>
              <FormControl>
                <LocationSelector
                  onCountryChange={(country) => {
                    // setArrivalCountryName(country?.name || "");
                    form.setValue(field.name, [country?.name || "", /* arrivalStateName || */ ""]);
                  }}
                  /* onStateChange={(state) => {
                    setArrivalStateName(state?.name || "");
                    form.setValue(field.name, [arrivalCountryName || "", state?.name || ""]);
                  }} */
                />
              </FormControl>
              <FormDescription>Where will you land?</FormDescription>
              <FormMessage />
            </FormItem>
          )}
        />
        <Button type="submit">Submit</Button>
      </form>
    </Form>
  );
}
