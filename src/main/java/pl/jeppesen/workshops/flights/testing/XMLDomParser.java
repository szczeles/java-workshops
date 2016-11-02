package pl.jeppesen.workshops.flights.testing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;

/**
 * Created by mariusz.strzelecki on 01.11.16.
 */
public class XMLDomParser {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse("src/main/resources/configuration.xml");

        Element etl = document.getDocumentElement();
        Element inputTag = (Element)etl.getElementsByTagName("input").item(0);
        Element flightsCsv = (Element)inputTag.getElementsByTagName("flightsCsv").item(0);
        Element path = (Element)flightsCsv.getElementsByTagName("path").item(0);
        System.out.println(path.getTextContent());

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        XPathExpression expression = xPath.compile("/etl/input/flightsCsv/path");
        System.out.println(expression.evaluate(etl));
    }
}
