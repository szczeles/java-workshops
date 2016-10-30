package pl.jeppesen.workshops.flights.validator;

import pl.jeppesen.workshops.flights.model.Flight;

public interface FlightValidator {
    boolean isValid(Flight f);
}
