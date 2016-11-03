package pl.jeppesen.workshops.flights;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import pl.jeppesen.workshops.flights.aircraft.data.AircraftDataProvider;
import pl.jeppesen.workshops.flights.aircraft.data.SqliteAircraftDataProvider;
import pl.jeppesen.workshops.flights.configuration.Configuration;
import pl.jeppesen.workshops.flights.flight.Flight;
import pl.jeppesen.workshops.flights.flight.dataprovider.CsvFlightDataProvider;
import pl.jeppesen.workshops.flights.flight.validator.*;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Application {
    public static void main(String[] args) throws InterruptedException {
        Configuration configuration = new Configuration("src/main/resources/configuration.xml");

        AircraftDataProvider aircraftDataProvider = new SqliteAircraftDataProvider("data/aircrafts.db");

        FlightValidator flightValidator = new OverallFlightValidator(Lists.newArrayList(
                new ValidIdFlightValidator(),
                new DateRangeFlightValidator(configuration.getDateRangeFrom(), configuration.getDateRangeTo()),
                new AirportFlightValidator(configuration.getAirports()),
                new ValidAircraftFlightValidator(aircraftDataProvider)
        ));

        CsvFlightDataProvider flightDataProvider = new CsvFlightDataProvider(configuration.getFlightsCsvPath());

        //Stopwatch stopwatch = Stopwatch.createStarted();
        Stream<Flight> stream = StreamSupport.stream(flightDataProvider.getFlightsIterator().spliterator(), false)
                .filter(flight -> flightValidator.isValid(flight));



        //System.out.println(stopwatch);

    }

    public static class AircraftCounter implements Runnable {

        private final Stream<Flight> stream;

        public AircraftCounter(Stream<Flight> stream) {
            this.stream = stream;
        }

        @Override
        public void run() {
            stream.forEach(flight -> {int a = 1+1;});

        }
    }
}
