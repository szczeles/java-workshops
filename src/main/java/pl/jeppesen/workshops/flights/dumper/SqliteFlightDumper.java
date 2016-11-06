package pl.jeppesen.workshops.flights.dumper;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import pl.jeppesen.workshops.flights.aircraft.Aircraft;
import pl.jeppesen.workshops.flights.aircraft.data.AircraftDataProvider;

import java.sql.*;

/**
 * Created by mariusz.strzelecki on 04.11.16.
 */
public class SqliteFlightDumper extends AbstractFlightDumper {

    private final static String CREATE_TABLE_FLIGHT = "CREATE TABLE IF NOT EXISTS flight (" +
            "id VARCHAR NOT NULL," +
            "date VARCHAR NOT NULL," +
            "aircraft_id VARCHAR," +
            "sta TIMESTAMP," +
            "std TIMESTAMP," +
            "`from` VARCHAR," +
            "`to` VARCHAR," +
            "airline VARCHAR," +
            "PRIMARY KEY (date, id, airline)" +
            ")";


    private final static String CREATE_TABLE_AIRCRAFT = "CREATE TABLE IF NOT EXISTS aircraft (" +
            "id VARCHAR NOT NULL," +
            "first_flight TIMESTAMP," +
            "name VARCHAR," +
            "model VARCHAR," +
            "series VARCHAR," +
            "type VARCHAR," +
            "PRIMARY KEY (id)" +
            ")";
    private static final java.lang.String INSERT_AIRCRAFT = "INSERT INTO aircraft VALUES (?, ?, ?, ?, ?, ?)";

    public SqliteFlightDumper(String databaseName) {

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        dataSource.setJdbcUrl("jdbc:sqlite:" + databaseName);
        dataSource.setAutoCommit(false);

        try (Connection conn = dataSource.getConnection(); Statement createTable = conn.createStatement()) {
            createTable.executeUpdate(CREATE_TABLE_FLIGHT);
            createTable.executeUpdate("DROP TABLE IF EXISTS aircraft");
            createTable.executeUpdate(CREATE_TABLE_AIRCRAFT);
            conn.commit();
        } catch (Exception e) {
            throw new RuntimeException("Unable to create table", e);
        }
    }

    @Override
    PreparedStatement getInsertStatement(Connection conn) throws SQLException {
        return conn.prepareStatement("INSERT OR IGNORE INTO flight VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
    }

    HikariDataSource getDataSource() {
        return dataSource;
    }


    @Override
    public void dumpAircrafts(AircraftDataProvider aircraftDataProvider) {

        QueryRunner runner = new QueryRunner(dataSource);
        try (Connection conn = dataSource.getConnection();
             PreparedStatement insert = conn.prepareStatement(INSERT_AIRCRAFT)){

            for (Object[] result: runner.query("SELECT DISTINCT aircraft_id FROM flight", new ArrayListHandler())) {
                Aircraft aircraft = aircraftDataProvider.getAircraft((String) result[0]).get();
                insert.setString(1, aircraft.getId());
                if (aircraft.getFirstFlight() != null) {
                    insert.setTimestamp(2, Timestamp.valueOf(aircraft.getFirstFlight().atStartOfDay()));
                }
                insert.setString(3, aircraft.getName());
                insert.setString(4, aircraft.getModel());
                insert.setString(5, aircraft.getSeries());
                insert.setString(6, aircraft.getType());
                insert.addBatch();
            }
            insert.executeBatch();
            conn.commit();

        } catch (SQLException e1) {
            throw new RuntimeException("Unable to count", e1);
        }

    }
}
