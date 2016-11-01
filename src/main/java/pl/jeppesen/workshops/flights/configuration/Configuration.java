package pl.jeppesen.workshops.flights.configuration;

import org.joox.Context;
import org.joox.Mapper;
import org.joox.Match;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.joox.JOOX.$;

public class Configuration {
    private final Match document;

    public Configuration(String configurationFile) {
        try {
            document = $(new File(configurationFile));
        } catch (SAXException|IOException e) {
            throw new RuntimeException("Unable to read configuration " + configurationFile, e);
        }
    }

    public String getDateRangeFrom() {
        return document.xpath("/etl/validation/dateRange/from").text().trim();
    }

    public String getDateRangeTo() {
        return document.xpath("/etl/validation/dateRange/to").text().trim();
    }

    public String getFlightsCsvPath() {
        return document.xpath("/etl/input/flightsCsv/path").text().trim();
    }

    public List<String> getAirports() {
        return document.xpath("/etl/validation/airports/airport")
                .map(xml -> xml.element().getTextContent());
    }
}
