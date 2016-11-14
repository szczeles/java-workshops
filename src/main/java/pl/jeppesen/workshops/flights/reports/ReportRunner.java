package pl.jeppesen.workshops.flights.reports;

import com.opencsv.CSVWriter;
import com.zaxxer.hikari.HikariDataSource;
import org.h2.Driver;
import org.sqlite.JDBC;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by mariusz.strzelecki on 14.11.16.
 */
public class ReportRunner implements AutoCloseable {
    private final HikariDataSource dataSource;

    public ReportRunner(String jdbcUri) {
        new Driver();
        new JDBC();

        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(jdbcUri);
        dataSource.setAutoCommit(false);
    }

    public void run(String sql, String filename) {
        try (Connection conn = dataSource.getConnection();
             Statement st = conn.createStatement();
             CSVWriter writer = new CSVWriter(new FileWriter("reports/" + filename));
        ) {
            writer.writeAll(st.executeQuery(sql), true);
        } catch (SQLException |IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        dataSource.close();

    }
}
