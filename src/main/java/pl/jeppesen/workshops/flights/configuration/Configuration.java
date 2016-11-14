package pl.jeppesen.workshops.flights.configuration;

import com.google.common.collect.Maps;
import org.joox.Context;
import org.joox.Mapper;
import org.joox.Match;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public String getAircraftDBPath() {
        return document.xpath("/etl/input/aircraftsDB/path").text().trim();
    }

    public String getOutputPath(String xpath) {
        return document.xpath(xpath).text().trim();
    }

    public Map<String, String> getReports() {
        HashMap<String, String> reports = Maps.newHashMap();
        document.xpath("/etl/reports/report").forEach(xml ->
                {
                    reports.put(
                            xml.getElementsByTagName("name").item(0).getTextContent(),
                            xml.getElementsByTagName("sql").item(0).getTextContent()
                    );
                }
        );
        return reports;
    }


}
