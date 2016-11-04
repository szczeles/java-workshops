package pl.jeppesen.workshops.flights.flight.validator;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import pl.jeppesen.workshops.flights.aircraft.data.AircraftDataProvider;
import pl.jeppesen.workshops.flights.flight.Flight;

import java.util.Map;
import java.util.Set;

/**
 * Created by mariusz.strzelecki on 02.11.16.
 */
public class ValidAircraftFlightValidator implements FlightValidator {
    private final Map<String, Boolean> cache;
    private AircraftDataProvider dataProvider;

    public ValidAircraftFlightValidator(AircraftDataProvider dataProvider) {
        this.dataProvider = dataProvider;
        cache = Maps.newConcurrentMap();
    }

    @Override
    public boolean isValid(Flight f) {
        if (f.getAircraftId() == null) {
            return false;
        }
        if (!cache.containsKey(f.getAircraftId())) {
            cache.put(f.getAircraftId(), dataProvider.getAircraft(f.getAircraftId()).isPresent());
        }
        return cache.get(f.getAircraftId());
    }
}
