package pl.jeppesen.workshops.flights;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import pl.jeppesen.workshops.flights.aircraft.data.AircraftDataProvider;
import pl.jeppesen.workshops.flights.aircraft.data.SqliteAircraftDataProvider;
import pl.jeppesen.workshops.flights.configuration.ArgumentParser;
import pl.jeppesen.workshops.flights.configuration.Configuration;
import pl.jeppesen.workshops.flights.configuration.OutputDB;
import pl.jeppesen.workshops.flights.dumper.FlightDumper;
import pl.jeppesen.workshops.flights.flight.Flight;
import pl.jeppesen.workshops.flights.flight.dataprovider.CsvFlightDataProvider;
import pl.jeppesen.workshops.flights.flight.validator.*;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Gets flights and aircrafts data, validates entries and saves result into sqlite/h2 database
 */
public class DataMerger {

    public static void main(String[] args) throws Exception {
        ArgumentParser parser = new ArgumentParser("data-merger", args);
        try {
            parser.parse();
        } catch (ArgumentParser.ArgumentsInvalid e) {
            parser.printHelp(e.getMessage());
            return;
        }

        run(new Configuration(parser.getConfig()), parser.getDb());
    }

    private static void run(Configuration configuration, OutputDB resultingDB) throws Exception {
        AircraftDataProvider aircraftDataProvider = new SqliteAircraftDataProvider(configuration.getAircraftDBPath());

        DB db = DBMaker.tempFileDB()
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

        try (FlightDumper dumper = resultingDB.initialize(configuration)) {
            Stopwatch stopwatch = Stopwatch.createStarted();
            Stream<Flight> stream = StreamSupport.stream(flightDataProvider.getFlightsIterator().spliterator(), true)
                    .filter(flight -> flightValidator.isValid(flight));

            dumper.dumpStream(stream);
            dumper.dumpAircrafts(aircraftDataProvider);

            System.out.println(dumper.count());
            System.out.println(dumper.countReal());
            System.out.println(stopwatch);
        } finally {
            aircraftDataProvider.close();
        }
    }
}
