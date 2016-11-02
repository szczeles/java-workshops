package pl.jeppesen.workshops.flights.testing;

import org.joox.Match;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import static org.joox.JOOX.$;

/**
 * Created by mariusz.strzelecki on 01.11.16.
 */
public class XMLJooxParser {
    public static void main(String[] args) throws IOException, SAXException {
        Match document = $(new File("src/main/resources/configuration.xml"));
        System.out.println(document.xpath("/etl/input/flightsCsv/path").text());
    }
}
