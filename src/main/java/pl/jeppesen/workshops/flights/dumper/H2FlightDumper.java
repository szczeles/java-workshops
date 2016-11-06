package pl.jeppesen.workshops.flights.dumper;

import com.zaxxer.hikari.HikariDataSource;
import org.h2.api.ErrorCode;
import org.h2.jdbc.JdbcSQLException;
import pl.jeppesen.workshops.flights.aircraft.data.AircraftDataProvider;
import pl.jeppesen.workshops.flights.flight.Flight;

import java.io.IOException;
import java.sql.*;

/**
 * Created by mariusz.strzelecki on 04.11.16.
 */
public class H2FlightDumper extends AbstractFlightDumper {
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS flight (" +
            "id VARCHAR NOT NULL," +
            "date VARCHAR NOT NULL," +
            "aircraft_id VARCHAR," +
            "sta TIMESTAMP," +
            "std TIMESTAMP," +
            "`from` VARCHAR," +
            "to VARCHAR," +
            "airline VARCHAR NOT NULL" +
            ")";

    private static final String ADD_PRIMARY_KEY = "ALTER TABLE flight ADD PRIMARY KEY (date, id, airline)";

    public H2FlightDumper(String databaseName) {

        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("H2 database driver not loaded");
        }

        dataSource.setJdbcUrl("jdbc:h2:" + databaseName + ";LOG=0;LOCK_MODE=0;UNDO_LOG=0");
        dataSource.setAutoCommit(false);

        try (Connection conn = dataSource.getConnection(); Statement createTable = conn.createStatement()) {
            createTable.executeUpdate(CREATE_TABLE);
            try {
                createTable.executeUpdate(ADD_PRIMARY_KEY);
            } catch (JdbcSQLException e) {
                if (e.getErrorCode() != ErrorCode.SECOND_PRIMARY_KEY) {
                    throw e;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("H2 database cannot be initialized", e);
        }
    }

    @Override
    PreparedStatement getInsertStatement(Connection conn) throws SQLException {
        return conn.prepareStatement("MERGE INTO flight KEY(date, id, airline) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
    }

    @Override
    public void dumpAircrafts(AircraftDataProvider aircraftDataProvider) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }
}
