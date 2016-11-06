package pl.jeppesen.workshops.flights.aircraft.data;

import org.testng.annotations.Test;
import pl.jeppesen.workshops.flights.aircraft.Aircraft;
import pl.jeppesen.workshops.flights.aircraft.JetAircraft;
import pl.jeppesen.workshops.flights.aircraft.TurbopropAircraft;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static org.testng.Assert.*;

/**
 * Created by mariusz.strzelecki on 02.11.16.
 */
public class SqliteAircraftDataProviderTest {


    @Test
    public void shouldGetJetAirfract() throws Exception {
        SqliteAircraftDataProvider aircraftDataProvider = new SqliteAircraftDataProvider("src/test/resources/aircrafts.db");

        Aircraft airfract = aircraftDataProvider.getAircraftIfExists("YL-PSD");

        assertEquals("YL-PSD", airfract.getId());
        assertEquals(LocalDate.of(2000, 03, 06), airfract.getFirstFlight());
        assertEquals("B738", airfract.getName());
        assertEquals("737", airfract.getModel());
        assertEquals("86N", airfract.getSeries());

        assertTrue(airfract instanceof JetAircraft);
        JetAircraft jetAircraft = (JetAircraft) airfract;
        assertEquals(2, jetAircraft.getEngines());
    }

    @Test
    public void shouldGetTurbopropAirfract() throws Exception {
        SqliteAircraftDataProvider aircraftDataProvider = new SqliteAircraftDataProvider("src/test/resources/aircrafts.db");

        Aircraft airfract = aircraftDataProvider.getAircraftIfExists("YL-BBW");

        assertEquals("YL-BBW", airfract.getId());
        assertNull(airfract.getFirstFlight());
        assertEquals("DH8D", airfract.getName());
        assertEquals("DHC-8", airfract.getModel());
        assertEquals("Q-402NG", airfract.getSeries());

        assertTrue(airfract instanceof TurbopropAircraft);
        TurbopropAircraft turbopropAircraft = (TurbopropAircraft) airfract;
        assertEquals(2, turbopropAircraft.getEngines());
    }

    @Test(expectedExceptions = AircraftNotFoundException.class)
    public void shouldThrowExceptionIfAircraftNotFound() throws Exception {
        SqliteAircraftDataProvider aircraftDataProvider = new SqliteAircraftDataProvider("src/test/resources/aircrafts.db");

        aircraftDataProvider.getAircraftIfExists("UNKNOWN");
    }

    @Test
    public void shouldReturnEmptyOptionalIfAircraftNotExist() throws Exception {
        SqliteAircraftDataProvider aircraftDataProvider = new SqliteAircraftDataProvider("src/test/resources/aircrafts.db");

        assertFalse(aircraftDataProvider.getAircraft("UNKNOWN").isPresent());
    }

    @Test
    public void shouldReturnValidOptional() throws Exception {
        SqliteAircraftDataProvider aircraftDataProvider = new SqliteAircraftDataProvider("src/test/resources/aircrafts.db");

        assertTrue(aircraftDataProvider.getAircraft("YL-BBW").isPresent());
        assertEquals("YL-BBW", aircraftDataProvider.getAircraft("YL-BBW").get().getId());
    }

    @Test
    public void shouldParseDate() {
        DateTimeFormatter customDmy = new DateTimeFormatterBuilder()
                .optionalStart()
                .appendValue(DAY_OF_MONTH)
                .appendLiteral("-")
                .optionalEnd()
                .appendPattern("MM-")
                .appendValueReduced(ChronoField.YEAR_OF_ERA, 2, 2, LocalDate.now().minusYears(80))
                .toFormatter();
        System.out.println(LocalDate.parse("02-03-98", customDmy));
        System.out.println(LocalDate.parse("02-03-10", customDmy));
        System.out.println(LocalDate.parse("03-98", customDmy));
    }

}