package pl.jeppesen.workshops.flights.aircraft.data;

import pl.jeppesen.workshops.flights.aircraft.Aircraft;

import java.util.Optional;

/**
 * Created by mariusz.strzelecki on 02.11.16.
 */
public interface AircraftDataProvider {
    Aircraft getAircraftIfExists(String id) throws AircraftNotFoundException;

    Optional<Aircraft> getAircraft(String id);

    void close();
}
