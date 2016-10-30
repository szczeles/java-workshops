package pl.jeppesen.workshops.flights.validator;

import pl.jeppesen.workshops.flights.model.Flight;

import java.util.List;

public class OverallFlightValidator implements FlightValidator {
    private List<FlightValidator> validators;

    public OverallFlightValidator(List<FlightValidator> validators) {
        this.validators = validators;
    }

    @Override
    public boolean isValid(Flight f) {
        for (FlightValidator validator : validators) {
            if (!validator.isValid(f)) {
                return false;
            }
        }
        return true;
    }
}
