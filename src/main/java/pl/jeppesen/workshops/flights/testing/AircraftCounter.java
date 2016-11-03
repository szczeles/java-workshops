package pl.jeppesen.workshops.flights.testing;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import pl.jeppesen.workshops.flights.Application;
import pl.jeppesen.workshops.flights.aircraft.data.AircraftDataProvider;
import pl.jeppesen.workshops.flights.aircraft.data.SqliteAircraftDataProvider;
import pl.jeppesen.workshops.flights.configuration.Configuration;
import pl.jeppesen.workshops.flights.flight.Flight;
import pl.jeppesen.workshops.flights.flight.dataprovider.CsvFlightDataProvider;
import pl.jeppesen.workshops.flights.flight.validator.*;

import java.util.HashSet;

/**
 * Created by mariusz.strzelecki on 03.11.16.
 */
public class AircraftCounter implements  Runnable {
    private final HashSet<String> aircrafts;
    private Iterable<Flight> flightsIterator;
    private FlightValidator flightValidator;
    private boolean stopMe;

    public AircraftCounter(Iterable<Flight> flightsIterator) {

        this.flightsIterator = flightsIterator;
        aircrafts = Sets.newHashSet();
    }

    @Override
    public void run() {

        for (Flight flight : flightsIterator) {
            aircrafts.add(flight.getAircraftId());
        }
        System.out.println(aircrafts.size());


    }

    public static void main(String[] args) throws InterruptedException {

        Configuration configuration = new Configuration("src/main/resources/configuration.xml");
        CsvFlightDataProvider flightDataProvider = new CsvFlightDataProvider(configuration.getFlightsCsvPath());

        AircraftCounter counter = new AircraftCounter(flightDataProvider.getFlightsIterator());
        Thread counterThread = new Thread(counter);
        Stopwatch stopwatch = Stopwatch.createStarted();
        counterThread.start();

        counterThread.join();
        System.out.println(stopwatch);
    }
}
