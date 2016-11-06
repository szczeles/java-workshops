package pl.jeppesen.workshops.flights.flight;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import javax.annotation.*;
import javax.annotation.meta.TypeQualifier;
import javax.annotation.meta.TypeQualifierDefault;
import javax.annotation.meta.TypeQualifierNickname;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@ParametersAreNonnullByDefault
public class Flight {
    private String id;
    private LocalDate date;
    private String airline;

    private String from;
    private String to;

    private LocalDateTime std;
    private LocalDateTime sta;

    private String aircraftId;
    private static ZoneId plZone = ZoneId.of("Europe/Warsaw");

    public Flight(@Nonnull String id, @Nonnull LocalDate date, String airline) {
        this.id = id;
        this.date = date;
        this.airline = airline;
    }

    public Flight() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public LocalDateTime getStd() {
        return std;
    }

    public void setStd(LocalDateTime std) {
        this.std = std;
    }

    public LocalDateTime getSta() {
        return sta;
    }

    public void setSta(LocalDateTime sta) {
        this.sta = sta;
    }

    public String getAircraftId() {
        return aircraftId;
    }

    public void setAircraftId(String aircraftId) {
        this.aircraftId = aircraftId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", airline='" + airline + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", std=" + std +
                ", sta=" + sta +
                ", aircraftId='" + aircraftId + '\'' +
                '}';
    }

    public void setDateFromString(String date) {
        if (date != null) {
            this.date = LocalDate.parse(date);
        }
    }

    public void setStaFromTimestamp(String timestamp) {
        if (timestamp != null) {
            sta = LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(timestamp)), plZone);
        }
    }

    public void setStdFromTimestamp(String timestamp) {
        if (timestamp != null) {
            std = LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(timestamp)), plZone);
        }
    }
}
