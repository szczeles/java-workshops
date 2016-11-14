package pl.jeppesen.workshops.flights.flight.validator;

import org.testng.annotations.Test;
import pl.jeppesen.workshops.flights.flight.Flight;

import java.time.LocalDate;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class DateRangeFlightValidatorTest {

    private final DateRangeFlightValidator dateRangeFlightValidator = new DateRangeFlightValidator("2016-01-01", "2016-10-01");

    @Test
    public void shouldRejectFlightBeforeMinDate() {
        assertFalse(dateRangeFlightValidator.isValid(new Flight(null, LocalDate.of(2015, 12, 31), null)));
    }

    @Test
    public void shouldRejectFlightAfterMaxDate() {
        assertFalse(dateRangeFlightValidator.isValid(new Flight(null, LocalDate.of(2016, 12, 31), null)));
    }

    @Test
    public void shouldAcceptFlightInRange() {
        assertTrue(dateRangeFlightValidator.isValid(new Flight(null, LocalDate.of(2016, 05, 31), null)));
    }

    @Test
    public void shouldAcceptFlightInMinDate() {
        assertTrue(dateRangeFlightValidator.isValid(new Flight(null, LocalDate.of(2016, 01, 01), null)));
    }

    @Test
    public void shouldAcceptFlightInMaxDate() {
        assertTrue(dateRangeFlightValidator.isValid(new Flight(null, LocalDate.of(2016, 10, 01), null)));
    }

}