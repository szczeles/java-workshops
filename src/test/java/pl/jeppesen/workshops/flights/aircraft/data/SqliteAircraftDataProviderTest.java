package pl.jeppesen.workshops.flights.aircraft.data;

import org.testng.annotations.Test;
import pl.jeppesen.workshops.flights.aircraft.Aircraft;
import pl.jeppesen.workshops.flights.aircraft.JetAircraft;
import pl.jeppesen.workshops.flights.aircraft.TurbopropAircraft;

import java.time.LocalDate;

import static org.testng.Assert.*;

/**
 * Created by mariusz.strzelecki on 02.11.16.
 */
public class SqliteAircraftDataProviderTest {


    @Test
    public void shouldGetJetAirfract() throws Exception {
        SqliteAircraftDataProvider aircraftDataProvider = new SqliteAircraftDataProvider("src/test/resources/aircrafts.db");

        Aircraft airfract = aircraftDataProvider.getAirfract("YL-PSD");

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

        Aircraft airfract = aircraftDataProvider.getAirfract("YL-BBW");

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

        aircraftDataProvider.getAirfract("UNKNOWN");
    }

}