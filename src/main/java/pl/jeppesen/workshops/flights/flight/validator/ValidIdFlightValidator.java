package pl.jeppesen.workshops.flights.flight.validator;

import pl.jeppesen.workshops.flights.flight.Flight;

public class ValidIdFlightValidator implements FlightValidator {
    @Override
    public boolean isValid(Flight f) {
        return f.getId() != null && f.getDate() != null && f.getAirline() != null;
    }
}
