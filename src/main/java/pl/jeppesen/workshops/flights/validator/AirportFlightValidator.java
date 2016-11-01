package pl.jeppesen.workshops.flights.validator;

import com.google.common.collect.Sets;
import pl.jeppesen.workshops.flights.model.Flight;

import java.util.List;
import java.util.Set;

/**
 * Created by mariusz.strzelecki on 01.11.16.
 */
public class AirportFlightValidator implements FlightValidator{
    private Set<String> airports;

    public AirportFlightValidator(List<String> airports) {
        this(Sets.newHashSet(airports));
    }

    public AirportFlightValidator(Set<String> airports) {
        this.airports = airports;
    }

    @Override
    public boolean isValid(Flight f) {
        return airports.contains(f.getFrom()) || airports.contains(f.getTo());
    }
}
