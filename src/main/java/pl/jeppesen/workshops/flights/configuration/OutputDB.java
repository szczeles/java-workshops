package pl.jeppesen.workshops.flights.configuration;

import pl.jeppesen.workshops.flights.dumper.FlightDumper;
import pl.jeppesen.workshops.flights.dumper.H2FlightDumper;
import pl.jeppesen.workshops.flights.dumper.SqliteFlightDumper;

/**
 * Created by mariusz.strzelecki on 05.11.16.
 */
public enum OutputDB {
    SQLITE(SqliteFlightDumper.class, "/etl/output/sqlite"),
    H2(H2FlightDumper.class, "/etl/output/h2");

    private Class<? extends FlightDumper> dumperClass;
    private String xmlDbLocation;

    OutputDB(Class<? extends FlightDumper> dumperClass, String xmlDbLocation) {
        this.dumperClass = dumperClass;
        this.xmlDbLocation = xmlDbLocation;
    }

    public FlightDumper initialize(Configuration configuration) throws Exception {
        return this.dumperClass.getConstructor(String.class)
                .newInstance(configuration.getOutputPath(xmlDbLocation));
    }

    public String getJdbcUri(Configuration configuration) {
        return "jdbc:" + name().toLowerCase() + ":" + configuration.getOutputPath(xmlDbLocation);
    }

}
