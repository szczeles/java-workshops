package pl.jeppesen.workshops.flights.dataprovider;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.opencsv.CSVIterator;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.*;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import pl.jeppesen.workshops.flights.model.Flight;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public class CsvFlightDataProvider implements FlightDataProvider {
    private String fileLocation;
    private ColumnPositionMappingStrategy mappingStrategy;

    public CsvFlightDataProvider(String fileLocation) {
        this.fileLocation = fileLocation;
        mappingStrategy = new ColumnPositionMappingStrategy();
        mappingStrategy.setType(Flight.class);
        mappingStrategy.setColumnMapping(new String[] {
                "id", "dateFromString", "aircraftId", "stdFromTimestamp", "staFromTimestamp", "from", "to", "airline"});
    }



    @Override
    public List<Flight> getFlights() {
        return new CsvToBean().parse(mappingStrategy, getReader());
    }

    private CSVReader getReader()  {
        Reader fileReader = null;
        try {
            fileReader = new FileReader(fileLocation);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Flights csv file " + fileLocation + " not found", e);
        }
        return new CSVReaderBuilder(fileReader)
                .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                .withSkipLines(1)
                .build();
    }

    @Override
    public Iterable<Flight> getFlightsIterator() {
        return new Iterable<Flight>() {
            @Override
            public Iterator<Flight> iterator() {
                try {
                    CSVIterator csvIterator = new CSVIterator(getReader());
                    return Iterators.transform(csvIterator, new Function<String[], Flight>() {
                        @Override
                        public Flight apply(String[] d) {
                            Flight f = new Flight();
                            f.setId(d[0]);
                            f.setDateFromString(d[1]);
                            f.setAircraftId(d[2]);
                            f.setStdFromTimestamp(d[3]);
                            f.setStaFromTimestamp(d[4]);
                            f.setFrom(d[5]);
                            f.setTo(d[6]);
                            f.setAirline(d[7]);
                            return f;
                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException("Unable to iterator over flights csv");
                }
            }
        };
    }
}
