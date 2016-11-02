package pl.jeppesen.workshops.flights.flight.dataprovider;

import pl.jeppesen.workshops.flights.flight.Flight;

import java.util.List;

public interface FlightDataProvider {
    List<Flight> getFlights();

    Iterable<Flight> getFlightsIterator();
}
