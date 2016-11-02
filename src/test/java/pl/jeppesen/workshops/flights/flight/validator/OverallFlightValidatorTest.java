package pl.jeppesen.workshops.flights.flight.validator;

import org.testng.annotations.Test;
import pl.jeppesen.workshops.flights.flight.Flight;

import java.util.ArrayList;

import static org.testng.Assert.*;

public class OverallFlightValidatorTest {

    @Test
    public void shouldAcceptIfAllValidatorsAccept() {
        ArrayList<FlightValidator> validators = new ArrayList<>();
        validators.add(validator(true));
        validators.add(validator(true));
        assertTrue(new OverallFlightValidator(validators).isValid(new Flight()));
    }

    @Test
    public void shouldRejectIfAnyValidatorReject() {
        ArrayList<FlightValidator> validators = new ArrayList<>();
        validators.add(validator(false));
        validators.add(validator(true));
        assertFalse(new OverallFlightValidator(validators).isValid(new Flight()));
    }

    private FlightValidator validator(boolean response) {
        return (flight) -> response;
    }

}