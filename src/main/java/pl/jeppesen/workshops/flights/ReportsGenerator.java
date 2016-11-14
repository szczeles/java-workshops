package pl.jeppesen.workshops.flights;

import pl.jeppesen.workshops.flights.configuration.ArgumentParser;
import pl.jeppesen.workshops.flights.configuration.Configuration;
import pl.jeppesen.workshops.flights.configuration.OutputDB;
import pl.jeppesen.workshops.flights.reports.ReportRunner;

import java.util.Map;

/**
 * Generates reports using data generated by DataMerger
 */
public class ReportsGenerator {

    public static void main(String[] args) throws Exception {
        ArgumentParser parser = new ArgumentParser("reports-generator", args);
        try {
            parser.parse();
        } catch (ArgumentParser.ArgumentsInvalid e) {
            parser.printHelp(e.getMessage());
            return;
        }
        run(new Configuration(parser.getConfig()), parser.getDb());
    }

    private static void run(Configuration configuration, OutputDB resultingDB) throws Exception {
        try (ReportRunner runner = new ReportRunner(resultingDB.getJdbcUri(configuration))) {
            Map<String, String> reports = configuration.getReports();
            reports.keySet().parallelStream().forEach(fileName -> {
                runner.run(reports.get(fileName), fileName + ".csv");
            });
        }
    }
}
