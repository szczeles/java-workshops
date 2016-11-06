package pl.jeppesen.workshops.flights.aircraft.data;

import com.zaxxer.hikari.HikariDataSource;
import pl.jeppesen.workshops.flights.aircraft.Aircraft;
import pl.jeppesen.workshops.flights.aircraft.JetAircraft;
import pl.jeppesen.workshops.flights.aircraft.TurbopropAircraft;

import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Optional;

/**
 * Created by mariusz.strzelecki on 02.11.16.
 */
public class SqliteAircraftDataProvider implements AircraftDataProvider {
//    private final Connection connection;
//    private final PreparedStatement statement;
    private final HikariDataSource dataSource;
    private DateTimeFormatter dmyFormatter = DateTimeFormatter.ofPattern("dd-MM-yy");
    private DateTimeFormatter myFormatter = DateTimeFormatter.ofPattern("MM-yy");

    private DateTimeFormatter customFormatter = new DateTimeFormatterBuilder()
            .appendPattern("dd-MM-")
            .appendValueReduced(ChronoField.YEAR_OF_ERA, 2, 2, LocalDate.now().minusYears(80))
            .toFormatter();

    public SqliteAircraftDataProvider(String databaseFile) {
        try {
            Class.forName("org.sqlite.JDBC");
            dataSource = new HikariDataSource();
            dataSource.setJdbcUrl("jdbc:sqlite:" + databaseFile);
        } catch (Exception e) {
            throw new RuntimeException("Aircraft database is not valid", e);
        }
    }

    @Override
    public  Aircraft getAircraftIfExists(String id) throws AircraftNotFoundException {

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM aircraft WHERE id = ?")){
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Aircraft aircraft;
                if (resultSet.getInt(6) > 0) {
                    aircraft = new JetAircraft();
                    ((JetAircraft)aircraft).setEngines(resultSet.getInt(6));
                } else {
                    aircraft = new TurbopropAircraft();
                    ((TurbopropAircraft)aircraft).setEngines(resultSet.getInt(7));
                }
                aircraft.setId(resultSet.getString(1));
                String dateField = resultSet.getString(2);
                if (!dateField.equals("N/A")) {
                    aircraft.setFirstFlight(parseDate(dateField));
                }
                aircraft.setName(resultSet.getString(3));
                aircraft.setModel(resultSet.getString(4));
                aircraft.setSeries(resultSet.getString(5));
                return aircraft;
            }
            throw new AircraftNotFoundException("Aircraft " + id + " not found!");

        } catch (SQLException e) {
            throw new RuntimeException("Aircraft database is not valid", e);
        }
    }

    private LocalDate parseDate(String dateField) {
        if (dateField.length() == 5) {
            dateField = "01-" + dateField;
        }
        return LocalDate.parse(dateField, customFormatter);
    }

    @Override
    public Optional<Aircraft> getAircraft(String id) {
        try {
            return Optional.of(getAircraftIfExists(id));
        } catch (AircraftNotFoundException e) {
            return Optional.empty();
        }
    }

    @Override
    public void close() {
        dataSource.close();
    }
}
