package pl.jeppesen.workshops.flights.dataprovider;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
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
        try {
            Reader fileReader = new FileReader(fileLocation);
            CSVReader reader = new CSVReaderBuilder(fileReader)
                    .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                    .withSkipLines(1)
                    .build();

            return new CsvToBean().parse(mappingStrategy, reader);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Flights csv file " + fileLocation + " not found", e);
        }
    }
}
