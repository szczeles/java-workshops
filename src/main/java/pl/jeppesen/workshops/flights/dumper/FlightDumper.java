package pl.jeppesen.workshops.flights.dumper;

import pl.jeppesen.workshops.flights.flight.Flight;

import java.io.Closeable;
import java.util.stream.Stream;

/**
 * Created by mariusz.strzelecki on 04.11.16.
 */
public interface FlightDumper extends AutoCloseable {

    int count();

    void dumpStream(Stream<Flight> stream);
}
