package pl.jeppesen.workshops.flights;

import pl.jeppesen.workshops.flights.model.Flight;
import pl.jeppesen.workshops.flights.validator.FlightValidator;
import pl.jeppesen.workshops.flights.validator.ValidIdFlightValidator;

import java.time.LocalDate;

public class Application {
    public static void main(String[] args) {
        Flight flight = new Flight();
        flight.setAircraftId("AIRCRAFT-ID");
        flight.setAirline("LO");
        flight.setId("LO300");
        flight.setDate(LocalDate.now());

        FlightValidator flightValidator = new ValidIdFlightValidator();
        System.out.println(flightValidator.isValid(flight));
    }
}
