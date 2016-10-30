package pl.jeppesen.workshops.flights.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Flight {
    private String id;
    private LocalDate date;
    private String airline;

    private LocalDateTime std;
    private LocalDateTime sta;

    private String aircraftId;

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

    @Override
    public String toString() {
        return "Flight{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", airline='" + airline + '\'' +
                ", std=" + std +
                ", sta=" + sta +
                ", aircraftId='" + aircraftId + '\'' +
                '}';
    }
}
