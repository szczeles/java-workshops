package pl.jeppesen.workshops.flights.aircraft.data;

import org.apache.commons.dbutils.BeanProcessor;
import pl.jeppesen.workshops.flights.aircraft.Aircraft;
import pl.jeppesen.workshops.flights.aircraft.JetAircraft;
import pl.jeppesen.workshops.flights.aircraft.TurbopropAircraft;

import javax.swing.text.DateFormatter;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Created by mariusz.strzelecki on 02.11.16.
 */
public class SqliteAircraftDataProvider implements AircraftDataProvider {
    private final Connection connection;
    private final PreparedStatement statement;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");

    public SqliteAircraftDataProvider(String databaseFile) {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile);
            statement = connection.prepareStatement("SELECT * FROM aircraft WHERE id = ?");
        } catch (Exception e) {
            throw new RuntimeException("Aircraft database is not valid", e);
        }
    }

    @Override
    public Aircraft getAircraftIfExists(String id) throws AircraftNotFoundException {
        try {
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
                if (!resultSet.getString(2).equals("N/A")) {
                    aircraft.setFirstFlight(LocalDate.parse(resultSet.getString(2), formatter));
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

    @Override
    public Optional<Aircraft> getAircraft(String id) {
        try {
            return Optional.of(getAircraftIfExists(id));
        } catch (AircraftNotFoundException e) {
            return Optional.empty();
        }
    }
}
