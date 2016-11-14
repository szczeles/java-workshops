# Java 8 training project for Jeppesen Poland

Training project covering libraries:

 * opencsv (csv reading/writing)
 * joox (xml reading using xpath)
 * mapdb (off-heap data storage)
 * commons-dbutils, sqlite-jdbc, h2, HikariCP (JDBC operations)
 * testng, mockito (unit tests)
 * google guava 
 * commons-cli

Compile with:

    mvn clean install

Dump data with:

    java -jar target/flights-etl-1.0-SNAPSHOT.jar -c src/main/resources/configuration.xml -d sqlite

Generate reports with:

    java -cp target/flights-etl-1.0-SNAPSHOT.jar pl.jeppesen.workshops.flights.ReportsGenerator -c src/main/resources/configuration.xml -d sqlite

