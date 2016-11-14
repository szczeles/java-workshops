package pl.jeppesen.workshops.flights.flight.validator;

import com.beust.jcommander.internal.Lists;
import org.testng.annotations.Test;
import pl.jeppesen.workshops.flights.flight.Flight;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Created by mariusz.strzelecki on 01.11.16.
 */
public class AirportFlightValidatorTest {

    AirportFlightValidator validator = new AirportFlightValidator(Lists.newArrayList("WAW", "GDN"));

    @Test
    public void shouldAcceptFromAirport() {
        assertTrue(validator.isValid(getFlight("WAW", "LON")));
    }

    @Test
    public void shouldAcceptToAirport() {
        assertTrue(validator.isValid(getFlight("LON", "GDN")));
    }

    @Test
    public void shouldRejectAiportsOutsideList() {
        assertFalse(validator.isValid(getFlight("LON", "WWW")));
    }

    private Flight getFlight(String from, String to) {
        Flight flight = new Flight();
        flight.setFrom(from);
        flight.setTo(to);
        return flight;
    }

}