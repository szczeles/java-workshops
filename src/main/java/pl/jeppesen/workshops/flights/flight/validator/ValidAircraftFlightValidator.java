package pl.jeppesen.workshops.flights.flight.validator;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;
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
        DB db = DBMaker.fileDB("/tmp/elo5")
                .fileMmapEnableIfSupported()
                .fileMmapPreclearDisable()
                .closeOnJvmShutdown().make();
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
