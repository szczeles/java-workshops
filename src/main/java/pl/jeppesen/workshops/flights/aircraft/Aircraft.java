package pl.jeppesen.workshops.flights.aircraft;

import java.time.LocalDate;

/**
 * Created by mariusz.strzelecki on 02.11.16.
 */
abstract public class Aircraft {
    private String id;
    private LocalDate firstFlight;
    private String name;
    private String model;
    private String series;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getFirstFlight() {
        return firstFlight;
    }

    public void setFirstFlight(LocalDate firstFlight) {
        this.firstFlight = firstFlight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public abstract String getType();
}
