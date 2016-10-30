package pl.jeppesen.workshops.flights.dataprovider;

import pl.jeppesen.workshops.flights.model.Flight;

import java.util.List;

public interface FlightDataProvider {
    List<Flight> getFlights();
}
