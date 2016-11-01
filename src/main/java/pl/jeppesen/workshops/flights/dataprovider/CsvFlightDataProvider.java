package pl.jeppesen.workshops.flights.dataprovider;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.*;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import pl.jeppesen.workshops.flights.model.Flight;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
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
        return new IterableCSVToBeanBuilder<Flight>()
                .withReader(getReader())
                .withMapper(mappingStrategy)
                .build();
    }
}
