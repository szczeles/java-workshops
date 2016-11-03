package pl.jeppesen.workshops.flights;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import pl.jeppesen.workshops.flights.aircraft.data.AircraftDataProvider;
import pl.jeppesen.workshops.flights.aircraft.data.SqliteAircraftDataProvider;
import pl.jeppesen.workshops.flights.configuration.Configuration;
import pl.jeppesen.workshops.flights.flight.Flight;
import pl.jeppesen.workshops.flights.flight.dataprovider.CsvFlightDataProvider;
import pl.jeppesen.workshops.flights.flight.validator.*;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Application {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Configuration configuration = new Configuration("src/main/resources/configuration.xml");

        AircraftDataProvider aircraftDataProvider = new SqliteAircraftDataProvider("data/aircrafts.db");

        FlightValidator flightValidator = new OverallFlightValidator(Lists.newArrayList(
                new ValidIdFlightValidator(),
                new DateRangeFlightValidator(configuration.getDateRangeFrom(), configuration.getDateRangeTo()),
                new AirportFlightValidator(configuration.getAirports()),
                new ValidAircraftFlightValidator(aircraftDataProvider)
        ));
//
//        ,
//        new ValidAircraftFlightValidator(aircraftDataProvider)

        CsvFlightDataProvider flightDataProvider = new CsvFlightDataProvider(configuration.getFlightsCsvPath());

        Stopwatch stopwatch = Stopwatch.createStarted();
        //Stream<Flight> stream = StreamSupport.stream(flightDataProvider.getFlightsIterator().spliterator(), false)
        //        .filter(flight -> flightValidator.isValid(flight));

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        ArrayList<Future<Boolean>> tasks = Lists.newArrayList();
        for (Flight f : flightDataProvider.getFlightsIterator()) {
            tasks.add(executorService.submit(() -> flightValidator.isValid(f)));
        }
        executorService.shutdown();
        int valid = 0;
        for (Future<Boolean> task : tasks) {
            if (task.get()) {
                valid ++;
            }
        }
        executorService.awaitTermination(1, TimeUnit.DAYS);
        System.out.println(valid);

        System.out.println(stopwatch);

        aircraftDataProvider.close();

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
