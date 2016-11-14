package pl.jeppesen.workshops.flights.testing;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Sets;
import pl.jeppesen.workshops.flights.configuration.Configuration;
import pl.jeppesen.workshops.flights.flight.Flight;
import pl.jeppesen.workshops.flights.flight.dataprovider.CsvFlightDataProvider;

import java.util.Set;

/**
 * Created by mariusz.strzelecki on 03.11.16.
 */
public class AircraftCounter implements  Runnable {
    private final Set<String> aircrafts;
    private Iterable<Flight> flightsIterator;

    public AircraftCounter(Iterable<Flight> flightsIterator, Set<String> aircrafts) {

        this.flightsIterator = flightsIterator;
        this.aircrafts = aircrafts;
    }

    @Override
    public void run() {

        for (Flight flight : flightsIterator) {
            if (flight.getAircraftId() == null) {
                continue;
            }
            aircrafts.add(flight.getAircraftId());
        }


    }

    public static void main(String[] args) throws InterruptedException {

        Configuration configuration = new Configuration("src/main/resources/configuration.xml");

        Set<String> aircrafts = Sets.newConcurrentHashSet();

        AircraftCounter counter = new AircraftCounter(
                new CsvFlightDataProvider(configuration.getFlightsCsvPath()).getFlightsIterator(), aircrafts);
        Thread counterThread1 = new Thread(counter);

        AircraftCounter counter2 = new AircraftCounter(
                new CsvFlightDataProvider(configuration.getFlightsCsvPath()).getFlightsIterator(), aircrafts);
        Thread counterThread2 = new Thread(counter2);

        ;
        counterThread1.start();
        counterThread2.start();
        Stopwatch stopwatch = Stopwatch.createStarted();

        counterThread1.join();
        counterThread2.join();
        System.out.println(aircrafts.size());
        System.out.println(stopwatch);
    }
}
