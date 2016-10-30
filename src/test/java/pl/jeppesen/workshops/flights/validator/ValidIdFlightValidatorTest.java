package pl.jeppesen.workshops.flights.validator;

import org.testng.annotations.Test;
import pl.jeppesen.workshops.flights.model.Flight;

import java.time.LocalDate;

import static org.testng.Assert.*;

public class ValidIdFlightValidatorTest {

    private final ValidIdFlightValidator validIdFlightValidator = new ValidIdFlightValidator();

    @Test
    public void shouldRejectFlightWithoutId() {
        assertFalse(
                validIdFlightValidator.isValid(new Flight(null, LocalDate.now(), "AIRLINE"))
        );
    }

    @Test
    public void shouldRejectFlightWithoutDate() {
        assertFalse(
                validIdFlightValidator.isValid(new Flight("ID", null, "AIRLINE"))
        );
    }

    @Test
    public void shouldRejectFlightWithoutAirline() {
        assertFalse(
                validIdFlightValidator.isValid(new Flight("ID", LocalDate.now(), null))
        );
    }
    @Test
    public void shouldAcceptFlightWithoutAirline() {
        assertTrue(
                validIdFlightValidator.isValid(new Flight("ID", LocalDate.now(), "AIRLINE"))
        );
    }
}