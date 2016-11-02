package pl.jeppesen.workshops.flights.flight.validator;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pl.jeppesen.workshops.flights.flight.Flight;

import java.time.LocalDate;

import static org.testng.Assert.*;

public class ValidIdFlightValidatorTest {

    private final ValidIdFlightValidator validIdFlightValidator = new ValidIdFlightValidator();

    @DataProvider(name = "multipleIdsDataProvider")
    public Object[][] testingData() {
        return new Object[][] {
                {"ID", LocalDate.now(), "AIRLINE", true},
                {null, LocalDate.now(), "AIRLINE", false},
                {"ID", null,            "AIRLINE", false},
                {"ID", LocalDate.now(), null     , false},
        };
    }

    @Test(dataProvider = "multipleIdsDataProvider")
    public void shouldTestValidator(String id, LocalDate date, String airline, boolean verdict) {
        assertEquals(verdict, validIdFlightValidator.isValid(
                new Flight(id, date, airline)
        ));
    }
}