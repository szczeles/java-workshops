package pl.jeppesen.workshops.flights;

import com.google.common.collect.Lists;
import pl.jeppesen.workshops.flights.dataprovider.CsvFlightDataProvider;
import pl.jeppesen.workshops.flights.model.Flight;
import pl.jeppesen.workshops.flights.validator.DateRangeFlightValidator;
import pl.jeppesen.workshops.flights.validator.FlightValidator;
import pl.jeppesen.workshops.flights.validator.OverallFlightValidator;
import pl.jeppesen.workshops.flights.validator.ValidIdFlightValidator;

import javax.swing.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class Application {
    public static void main(String[] args) {
        FlightValidator flightValidator = new OverallFlightValidator(Lists.newArrayList(
                new ValidIdFlightValidator(),
                new DateRangeFlightValidator("2015-10-01", "2016-10-01")
        ));

        CsvFlightDataProvider flightDataProvider = new CsvFlightDataProvider("data/flights.csv");


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
