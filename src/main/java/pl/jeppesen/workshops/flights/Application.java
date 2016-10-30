package pl.jeppesen.workshops.flights;

import com.google.common.collect.Lists;
import pl.jeppesen.workshops.flights.configuration.Configuration;
import pl.jeppesen.workshops.flights.dataprovider.CsvFlightDataProvider;
import pl.jeppesen.workshops.flights.model.Flight;
import pl.jeppesen.workshops.flights.validator.DateRangeFlightValidator;
import pl.jeppesen.workshops.flights.validator.FlightValidator;
import pl.jeppesen.workshops.flights.validator.OverallFlightValidator;
import pl.jeppesen.workshops.flights.validator.ValidIdFlightValidator;

import java.util.List;

public class Application {
    public static void main(String[] args) {
        Configuration configuration = new Configuration("src/main/resources/configuration.xml");

        FlightValidator flightValidator = new OverallFlightValidator(Lists.newArrayList(
                new ValidIdFlightValidator(),
                new DateRangeFlightValidator(configuration.getDateRangeFrom(), configuration.getDateRangeTo())
        ));

        CsvFlightDataProvider flightDataProvider = new CsvFlightDataProvider(configuration.getFlightsCsvPath());


        List<Flight> flights = flightDataProvider.getFlights();
        System.out.println(flights.size());
        System.out.println(flights.stream()
                .filter(flight -> flightValidator.isValid(flight))
                .count());

        flights.stream()
                .filter(flight -> flightValidator.isValid(flight))
                .limit(10)
                .forEach(System.out::println);
    }
}
