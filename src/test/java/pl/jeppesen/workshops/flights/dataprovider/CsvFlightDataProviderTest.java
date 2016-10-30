package pl.jeppesen.workshops.flights.dataprovider;

import org.testng.annotations.Test;
import pl.jeppesen.workshops.flights.model.Flight;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.testng.Assert.*;

public class CsvFlightDataProviderTest {

    @Test
    public void shouldReadFlightsFromCsv() {
        String fileLocation = "src/test/resources/flights.csv";
        CsvFlightDataProvider csvFlightDataProvider = new CsvFlightDataProvider(fileLocation);

        List<Flight> flights = csvFlightDataProvider.getFlights();

        assertEquals(flights.size(), 2);
        assertEquals(flights.get(0).getId(), "4U2734");
        assertEquals(flights.get(0).getDate(), LocalDate.of(2016, 10, 22));
        assertEquals(flights.get(0).getTo(), "KRK");
        assertEquals(flights.get(0).getFrom(), "STR");
        assertEquals(flights.get(0).getStd(), LocalDateTime.of(2016, 10, 22, 13, 35));
        assertEquals(flights.get(0).getSta(), LocalDateTime.of(2016, 10, 22, 15, 05));
        assertEquals(flights.get(0).getAirline(), "4U");
        assertEquals(flights.get(0).getAircraftId(), "D-AKNR");
    }

}