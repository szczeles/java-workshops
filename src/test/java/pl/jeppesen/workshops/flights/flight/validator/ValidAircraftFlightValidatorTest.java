package pl.jeppesen.workshops.flights.flight.validator;

import org.mapdb.DBMaker;
import org.mockito.AdditionalMatchers;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pl.jeppesen.workshops.flights.aircraft.Aircraft;
import pl.jeppesen.workshops.flights.aircraft.data.AircraftDataProvider;
import pl.jeppesen.workshops.flights.flight.Flight;

import java.util.Optional;
import java.util.regex.Matcher;

import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

/**
 * Created by mariusz.strzelecki on 02.11.16.
 */
public class ValidAircraftFlightValidatorTest {

    private ValidAircraftFlightValidator flightValidator;
    private AircraftDataProvider dataProvider;

    @BeforeMethod
    public void setUp () {
        dataProvider = Mockito.mock(AircraftDataProvider.class);
        Aircraft aircraft = Mockito.mock(Aircraft.class);
        when(dataProvider.getAircraft("ID")).thenReturn(Optional.of(aircraft));
        when(dataProvider.getAircraft(AdditionalMatchers.not(Matchers.eq("ID"))))
                .thenReturn(Optional.empty());

        flightValidator = new ValidAircraftFlightValidator(dataProvider, DBMaker.heapDB().make());
    }

    @Test
    public void shouldAcceptValidAircraft() {
        Flight f = new Flight();
        f.setAircraftId("ID");
        assertTrue(flightValidator.isValid(f));
    }

    @Test
    public void shouldRejectInvalidAircraft() {
        Flight f = new Flight();
        f.setAircraftId("INVALID");
        assertFalse(flightValidator.isValid(f));
    }

    @Test
    public void shouldUseCache() {
        Flight f = new Flight();
        f.setAircraftId("ID");

        assertTrue(flightValidator.isValid(f));
        Mockito.verify(dataProvider).getAircraft("ID");
        Mockito.verifyNoMoreInteractions(dataProvider);

        assertTrue(flightValidator.isValid(f));
        assertTrue(flightValidator.isValid(f));
    }

}