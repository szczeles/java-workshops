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
        return validators.stream().map(validator -> !validator.isValid(f)).findFirst().isPresent();
    }
}
