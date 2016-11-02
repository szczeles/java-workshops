package pl.jeppesen.workshops.flights.flight.validator;

import pl.jeppesen.workshops.flights.flight.Flight;

public interface FlightValidator {
    boolean isValid(Flight f);
}
