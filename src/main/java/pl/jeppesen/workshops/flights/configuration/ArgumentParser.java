package pl.jeppesen.workshops.flights.configuration;

import org.apache.commons.cli.*;

import java.io.File;

/**
 * Created by mariusz.strzelecki on 14.11.16.
 */
public class ArgumentParser {

    private final String applicationName;
    private final String[] args;
    private String config;
    private OutputDB db;

    public ArgumentParser(String applicationName, String[] args) {
        this.applicationName = applicationName;
        this.args = args;
    }

    public void parse() throws ArgumentsInvalid {
        Options options = getCliOptions();
        CommandLineParser parser = new DefaultParser();

        CommandLine cli;
        try {
            cli = parser.parse(options, args);
        } catch (ParseException e) {
            throw new ArgumentsInvalid("Invalid options provided");
        }

        if (cli.hasOption("help")) {
            throw new ArgumentsInvalid(null);
        }

        if (cli.getOptionValue("config") == null) {
            throw new ArgumentsInvalid("Config not provided");
        }

        if (!new File(cli.getOptionValue("config")).exists()) {
            throw new ArgumentsInvalid("Config file not found");
        }

        this.config = cli.getOptionValue("config");

        if (cli.getOptionValue("db") == null) {
            throw new ArgumentsInvalid("Database type not provided");
        }

        try {
            this.db = OutputDB.valueOf(cli.getOptionValue("db").toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ArgumentsInvalid("Invalid database type");
        }
    }


    public void printHelp(String message) {
        if (message != null) {
            System.out.println(message);
        }
        new HelpFormatter().printHelp(applicationName, getCliOptions());
    }

    private Options getCliOptions() {
        Options options = new Options();
        options.addOption("d", "db", true, "resulting database, h2 or sqlite");
        options.addOption("c", "config", true, "configuration xml location");
        options.addOption("h", "help", false, "print help message");
        return options;
    }

    public static class ArgumentsInvalid extends Exception {
        public ArgumentsInvalid(String message) {
            super(message);
        }
    }

    public String getConfig() {
        return config;
    }

    public OutputDB getDb() {
        return db;
    }
}
