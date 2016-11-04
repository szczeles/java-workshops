package pl.jeppesen.workshops.flights;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.slf4j.Logger;
import pl.jeppesen.workshops.flights.aircraft.data.AircraftDataProvider;
import pl.jeppesen.workshops.flights.aircraft.data.SqliteAircraftDataProvider;
import pl.jeppesen.workshops.flights.configuration.Configuration;
import pl.jeppesen.workshops.flights.dumper.FlightDumper;
import pl.jeppesen.workshops.flights.dumper.H2FlightDumper;
import pl.jeppesen.workshops.flights.flight.Flight;
import pl.jeppesen.workshops.flights.flight.dataprovider.CsvFlightDataProvider;
import pl.jeppesen.workshops.flights.flight.validator.*;

import java.util.concurrent.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Application {
    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration("src/main/resources/configuration.xml");

        AircraftDataProvider aircraftDataProvider = new SqliteAircraftDataProvider("data/aircrafts.db");

        DB db = DBMaker.fileDB("/tmp/elo5")
                .fileMmapEnableIfSupported()
                .fileMmapPreclearDisable()
                .closeOnJvmShutdown().make();

        FlightValidator flightValidator = new OverallFlightValidator(Lists.newArrayList(
                new ValidIdFlightValidator(),
                new DateRangeFlightValidator(configuration.getDateRangeFrom(), configuration.getDateRangeTo()),
                new AirportFlightValidator(configuration.getAirports()),
                new ValidAircraftFlightValidator(aircraftDataProvider, db)
        ));

        CsvFlightDataProvider flightDataProvider = new CsvFlightDataProvider(configuration.getFlightsCsvPath());

        try (FlightDumper dumper = new H2FlightDumper("./data/dumper/h2")) {
            Stopwatch stopwatch = Stopwatch.createStarted();
            Stream<Flight> stream = StreamSupport.stream(flightDataProvider.getFlightsIterator().spliterator(), true)
                    .filter(flight -> flightValidator.isValid(flight));

            dumper.dumpStream(stream);

            System.out.println(dumper.count());
            System.out.println(stopwatch);
        } finally {
            aircraftDataProvider.close();
        }


    }
}
