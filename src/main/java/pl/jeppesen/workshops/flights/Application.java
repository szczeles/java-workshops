package pl.jeppesen.workshops.flights;

import pl.jeppesen.workshops.flights.dataprovider.CsvFlightDataProvider;
import pl.jeppesen.workshops.flights.model.Flight;
import pl.jeppesen.workshops.flights.validator.DateRangeFlightValidator;
import pl.jeppesen.workshops.flights.validator.FlightValidator;
import pl.jeppesen.workshops.flights.validator.ValidIdFlightValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

public class Application {
    public static void main(String[] args) {
        FlightValidator flightValidator = new ValidIdFlightValidator();
        DateRangeFlightValidator dateRangeFlightValidator = new DateRangeFlightValidator("2015-10-01", "2016-10-01");
        CsvFlightDataProvider flightDataProvider = new CsvFlightDataProvider("data/flights.csv");


        List<Flight> flights = flightDataProvider.getFlights();
        System.out.println(flights.size());
        System.out.println(flights.stream()
                .filter(flight -> flightValidator.isValid(flight))
                .filter(flight -> dateRangeFlightValidator.isValid(flight))
                .count());
    }
}
