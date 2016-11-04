package pl.jeppesen.workshops.flights.dumper;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;

/**
 * Created by mariusz.strzelecki on 04.11.16.
 */
public class SqliteFlightDumper extends AbstractFlightDumper {

    private final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS flight (" +
            "id VARCHAR NOT NULL," +
            "date VARCHAR NOT NULL," +
            "aircraft_id VARCHAR," +
            "sta TIMESTAMP," +
            "std TIMESTAMP," +
            "`from` VARCHAR," +
            "`to` VARCHAR," +
            "airline VARCHAR," +
            "PRIMARY KEY (date, id)" +
            ")";

    public SqliteFlightDumper(String databaseName) {

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        dataSource.setJdbcUrl("jdbc:sqlite:" + databaseName);
        dataSource.setAutoCommit(false);

        try (Connection conn = dataSource.getConnection(); Statement createTable = conn.createStatement()) {
            createTable.executeUpdate(CREATE_TABLE);
            conn.commit();
        } catch (Exception e) {
            throw new RuntimeException("Unable to create table", e);
        }
    }

    @Override
    PreparedStatement getInsertStatement(Connection conn) throws SQLException {
        return conn.prepareStatement("INSERT OR IGNORE INTO flight VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
    }
}
