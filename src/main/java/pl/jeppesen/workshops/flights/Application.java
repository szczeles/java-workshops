package pl.jeppesen.workshops.flights;

import pl.jeppesen.workshops.flights.dataprovider.CsvFlightDataProvider;
import pl.jeppesen.workshops.flights.model.Flight;
import pl.jeppesen.workshops.flights.validator.FlightValidator;
import pl.jeppesen.workshops.flights.validator.ValidIdFlightValidator;

import java.time.LocalDate;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        FlightValidator flightValidator = new ValidIdFlightValidator();
        CsvFlightDataProvider flightDataProvider = new CsvFlightDataProvider("data/flights.csv");


        List<Flight> flights = flightDataProvider.getFlights();
        System.out.println(flights.size());
        int valid = 0;
        for (Flight flight : flights) {
            if (flightValidator.isValid(flight)) {
                valid++;
            }
        }
        System.out.println(valid);
    }
}
