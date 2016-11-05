package pl.jeppesen.workshops.flights.dumper;

import com.zaxxer.hikari.HikariDataSource;
import pl.jeppesen.workshops.flights.flight.Flight;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

/**
 * Created by mariusz.strzelecki on 04.11.16.
 */
abstract public class AbstractFlightDumper implements FlightDumper{
    protected HikariDataSource dataSource = new HikariDataSource();
    private final AtomicInteger counter = new AtomicInteger(0);

    abstract PreparedStatement getInsertStatement(Connection conn) throws SQLException;

    @Override
    public void close() {
        dataSource.close();
    }

    @Override
    public int count() {
        return counter.get();
    }

    @Override
    public void dumpStream(Stream<Flight> stream) {
        try (Connection conn = dataSource.getConnection(); PreparedStatement statement = getInsertStatement(conn)){
            stream.sequential().forEach(flight -> {

                try {
                    statement.setString(1, flight.getId());
                    statement.setString(2, flight.getDate().toString());
                    statement.setString(3, flight.getAircraftId());
                    statement.setTimestamp(4, Timestamp.valueOf(flight.getSta()));
                    statement.setTimestamp(5, Timestamp.valueOf(flight.getStd()));
                    statement.setString(6, flight.getFrom());
                    statement.setString(7, flight.getTo());
                    statement.setString(8, flight.getAirline());
                    statement.addBatch();

                    if (counter.incrementAndGet() % 1000 == 0) {
                        statement.executeBatch();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            statement.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
