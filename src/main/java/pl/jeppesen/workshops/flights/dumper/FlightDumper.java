package pl.jeppesen.workshops.flights.dumper;

import pl.jeppesen.workshops.flights.aircraft.data.AircraftDataProvider;
import pl.jeppesen.workshops.flights.flight.Flight;

import java.util.stream.Stream;

/**
 * Created by mariusz.strzelecki on 04.11.16.
 */
public interface FlightDumper extends AutoCloseable {

    int count();

    void dumpStream(Stream<Flight> stream);

    int countReal();

    void dumpAircrafts(AircraftDataProvider aircraftDataProvider);
}
