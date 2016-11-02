package pl.jeppesen.workshops.flights.aircraft.data;

/**
 * Created by mariusz.strzelecki on 02.11.16.
 */
public class AircraftNotFoundException extends Exception {
    public AircraftNotFoundException(String message) {
        super(message);
    }
}
