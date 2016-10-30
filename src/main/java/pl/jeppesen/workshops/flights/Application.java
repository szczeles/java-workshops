package pl.jeppesen.workshops.flights;

import pl.jeppesen.workshops.flights.dataprovider.CsvFlightDataProvider;
import pl.jeppesen.workshops.flights.model.Flight;
import pl.jeppesen.workshops.flights.validator.FlightValidator;
import pl.jeppesen.workshops.flights.validator.ValidIdFlightValidator;

import java.time.LocalDate;

public class Application {
    public static void main(String[] args) {
        FlightValidator flightValidator = new ValidIdFlightValidator();
        CsvFlightDataProvider flightDataProvider = new CsvFlightDataProvider("data/flights.csv");
        System.out.println(flightDataProvider.getFlights().size());

        //System.out.println(flightValidator.isValid(flight));
    }
}
