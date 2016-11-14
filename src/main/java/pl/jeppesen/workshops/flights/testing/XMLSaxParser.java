package pl.jeppesen.workshops.flights.testing;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.Stack;

/**
 * Created by mariusz.strzelecki on 01.11.16.
 */
public class XMLSaxParser {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        ConfigurationFileHandler handler = new ConfigurationFileHandler();

        saxParser.parse("src/main/resources/configuration.xml", handler);

        System.out.println(handler.getFlightsCsvPath());
    }

    private static class ConfigurationFileHandler extends DefaultHandler {
        public String getFlightsCsvPath() {
            return flightsCsvPath;
        }

        private Stack<String> stack = new Stack<>();
        private String flightsCsvPath;
        private boolean insideElement = false;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            stack.push(qName);
            insideElement = true;
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            insideElement = false;
            stack.pop();
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (StringUtils.join(stack, '/').equals("etl/input/flightsCsv/path") && insideElement) {
                flightsCsvPath = new String(ch, start, length).trim();
            }
        }


    }
}
