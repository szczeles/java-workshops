package pl.jeppesen.workshops.flights.flight.validator;

import org.mapdb.DB;
import org.mapdb.Serializer;
import pl.jeppesen.workshops.flights.aircraft.data.AircraftDataProvider;
import pl.jeppesen.workshops.flights.flight.Flight;

import java.util.Map;

/**
 * Created by mariusz.strzelecki on 02.11.16.
 */
public class ValidAircraftFlightValidator implements FlightValidator {
    private final Map<String, Boolean> cache;
    private AircraftDataProvider dataProvider;

    public ValidAircraftFlightValidator(AircraftDataProvider dataProvider, DB db) {
        this.dataProvider = dataProvider;
        cache = db.treeMap("mmm", Serializer.STRING, Serializer.BOOLEAN).createOrOpen();
    }

    @Override
    public boolean isValid(Flight f) {
        if (f.getAircraftId() == null) {
            return false;
        }
        Boolean result = cache.get(f.getAircraftId());
        if (result == null) {
            result = dataProvider.getAircraft(f.getAircraftId()).isPresent();
            cache.put(f.getAircraftId(), result);
        }
        return result;
    }
}
