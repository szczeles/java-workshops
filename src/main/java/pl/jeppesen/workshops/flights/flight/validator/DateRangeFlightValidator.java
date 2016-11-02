package pl.jeppesen.workshops.flights.flight.validator;

import pl.jeppesen.workshops.flights.flight.Flight;

import java.time.LocalDate;

public class DateRangeFlightValidator implements FlightValidator {
    private final LocalDate minDate;
    private final LocalDate maxDate;

    public DateRangeFlightValidator(String minDate, String maxDate) {
        this.minDate = LocalDate.parse(minDate);
        this.maxDate = LocalDate.parse(maxDate);
    }

    @Override
    public boolean isValid(Flight f) {
        return f.getDate().compareTo(minDate) >= 0 && f.getDate().compareTo(maxDate) <= 0;
    }
}
