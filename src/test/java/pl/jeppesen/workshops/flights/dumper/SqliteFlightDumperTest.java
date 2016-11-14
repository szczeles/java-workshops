package pl.jeppesen.workshops.flights.dumper;

import com.beust.jcommander.internal.Lists;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.testng.annotations.Test;
import pl.jeppesen.workshops.flights.flight.Flight;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.testng.Assert.assertEquals;

/**
 * Created by mariusz.strzelecki on 04.11.16.
 */
public class SqliteFlightDumperTest {

    @Test
    public void shouldDumpDataToSqliteDatabase() throws SQLException {
        Flight f = new Flight();
        f.setId("4U2734");
        f.setDate( LocalDate.of(2016, 10, 22));
        f.setTo("KRK");
        f.setFrom("STR");
        f.setStd(LocalDateTime.of(2016, 10, 22, 13, 35));
        f.setSta(LocalDateTime.of(2016, 10, 22, 15, 05));
        f.setAirline("4U");
        f.setAircraftId("D-AKNR");

        Stream<Flight> stream = Lists.newArrayList(f).stream();

        try (SqliteFlightDumper flightDumper = new SqliteFlightDumper(":memory:")) {
            flightDumper.dumpStream(stream);

            HikariDataSource ds = flightDumper.getDataSource();
            QueryRunner queryRunner = new QueryRunner(ds);
            Integer result = queryRunner.query("SELECT COUNT(*) FROM flight", new ScalarHandler<Integer>("COUNT(*)"));
            assertEquals(1, result.intValue());
        }
    }

}