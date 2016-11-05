package pl.jeppesen.workshops.flights;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import org.apache.commons.cli.*;
import org.jetbrains.annotations.NotNull;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.slf4j.Logger;
import pl.jeppesen.workshops.flights.aircraft.data.AircraftDataProvider;
import pl.jeppesen.workshops.flights.aircraft.data.SqliteAircraftDataProvider;
import pl.jeppesen.workshops.flights.configuration.Configuration;
import pl.jeppesen.workshops.flights.configuration.OutputDB;
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
        Options options = getCliOptions();
        CommandLineParser parser = new DefaultParser();
        CommandLine cli;

        try {
            cli = parser.parse(options, args);

            if (cli.hasOption("help")) {
                printHelp(options);
            }

            Configuration configuration = new Configuration(cli.getOptionValue("config"));
            OutputDB resultingDB = OutputDB.valueOf(cli.getOptionValue("output").toUpperCase());

            run(configuration, resultingDB);
        } catch (ParseException e) {
            printHelp(options);
        }
    }

    private static void printHelp(Options options) {
        new HelpFormatter().printHelp("flights-etl", options);
        System.exit(0);
    }

    @NotNull
    private static Options getCliOptions() {
        Options options = new Options();
        options.addOption("d", "db", true, "resulting database, h2 or sqlite");
        options.addOption("c", "config", true, "configuration xml location");
        options.addOption("h", "help", false, "print help message");
        return options;
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

            System.out.println(dumper.count());
            System.out.println(dumper.countReal());
            System.out.println(stopwatch);
        } finally {
            aircraftDataProvider.close();
        }
    }
}
