package pl.jeppesen.workshops.flights;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateExamples {
    public static void main(String[] args) {
        System.out.println(LocalDateTime.of(2016, 10, 20, 1, 0, 0));
        System.out.println(ZonedDateTime.of(LocalDateTime.of(2016, 11, 20, 1, 0, 0), ZoneId.of("Europe/Warsaw")));

        System.out.println(Duration.between(
                LocalDateTime.of(2016, 11, 20, 1, 0, 0),
                ZonedDateTime.of(LocalDateTime.of(2016, 12, 20, 1, 1, 1), ZoneId.of("Europe/Warsaw"))
        ).getSeconds());

        System.out.println(Instant.now());

        System.out.println(LocalDate.parse("07.11.2016", DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        System.out.println(ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME));

        System.out.println(LocalDate.now().plusDays(10));
    }
}
