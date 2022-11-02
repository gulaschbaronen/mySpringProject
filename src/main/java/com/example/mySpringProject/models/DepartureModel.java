package com.example.mySpringProject.models;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DepartureModel implements Serializable {
    private LocalTime departureTime;
    private String departureStation;


    public DepartureModel(String _departureTime, String _departureStation){
        this.departureStation = _departureStation;
        this.departureTime = LocalTime.parse(_departureTime, DateTimeFormatter.ofPattern("H:mm:ss"));
    }

    public DepartureModel(LocalTime _departureTime, String _departureStation){
        this.departureStation = _departureStation;
        this.departureTime = _departureTime;
    }


    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(final LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public String getDepartureStation() {
        return departureStation;
    }

    public void setDepartureStation(final String departureStation) {
        this.departureStation = departureStation;
    }
}
