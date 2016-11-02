package pl.jeppesen.workshops.flights;

import com.google.common.collect.Lists;
import pl.jeppesen.workshops.flights.configuration.Configuration;
import pl.jeppesen.workshops.flights.flight.dataprovider.CsvFlightDataProvider;
import pl.jeppesen.workshops.flights.flight.validator.*;

import java.util.stream.StreamSupport;

public class Application {
    public static void main(String[] args) {
        Configuration configuration = new Configuration("src/main/resources/configuration.xml");

        FlightValidator flightValidator = new OverallFlightValidator(Lists.newArrayList(
                new ValidIdFlightValidator(),
                new DateRangeFlightValidator(configuration.getDateRangeFrom(), configuration.getDateRangeTo()),
                new AirportFlightValidator(configuration.getAirports())
        ));

        CsvFlightDataProvider flightDataProvider = new CsvFlightDataProvider(configuration.getFlightsCsvPath());

        System.out.println(StreamSupport.stream(flightDataProvider.getFlightsIterator().spliterator(), false)
                .filter(flight -> flightValidator.isValid(flight))
                .count());
        ;
    }
}
