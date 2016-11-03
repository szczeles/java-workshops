package pl.jeppesen.workshops.flights.flight.validator;

import pl.jeppesen.workshops.flights.aircraft.data.AircraftDataProvider;
import pl.jeppesen.workshops.flights.flight.Flight;

/**
 * Created by mariusz.strzelecki on 02.11.16.
 */
public class ValidAircraftFlightValidator implements FlightValidator {
    private AircraftDataProvider dataProvider;

    public ValidAircraftFlightValidator(AircraftDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public boolean isValid(Flight f) {
        return dataProvider.getAircraft(f.getAircraftId()).isPresent();
    }
}
