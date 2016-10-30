package pl.jeppesen.workshops.flights;

import pl.jeppesen.workshops.flights.dataprovider.CsvFlightDataProvider;
import pl.jeppesen.workshops.flights.model.Flight;
import pl.jeppesen.workshops.flights.validator.FlightValidator;
import pl.jeppesen.workshops.flights.validator.ValidIdFlightValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

public class Application {
    public static void main(String[] args) {
        FlightValidator flightValidator = new ValidIdFlightValidator();
        CsvFlightDataProvider flightDataProvider = new CsvFlightDataProvider("data/flights.csv");


        List<Flight> flights = flightDataProvider.getFlights();
        System.out.println(flights.size());
        System.out.println(flights.stream().filter(flight -> flightValidator.isValid(flight)).count());
    }
}
