package pl.jeppesen.workshops.flights;

import pl.jeppesen.workshops.flights.model.Flight;

public class Application {
    public static void main(String[] args) {
        Flight flight = new Flight();
        flight.setAircraftId("AIRCRAFT-ID");
        flight.setAirline("LO");
        flight.setId("LO300");
        System.out.println(flight);
    }
}
