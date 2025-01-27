package com.quickparkassist.model;


import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "evmodel")

public class Evmodel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //private int reservation_id;
    private Integer reservation_id; // This will auto-generate the ID


    private String location;
    private double duration;
    private String spotName;
    private Long evSpot;

    @Temporal(TemporalType.DATE)
    private Date reservation_Date;

    private String start_time;
    private String end_time;
    private String vehicle_id;
    private Long userId;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getReservation_id() {
        return reservation_id;
    }
    public void setReservation_id(Integer reservation_id) {
        this.reservation_id = reservation_id;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public Date getReservation_Date() {
        return reservation_Date;
    }
    public void setReservation_Date(Date reservation_Date) {
        this.reservation_Date = reservation_Date;
    }
    public String getStart_time() {
        return start_time;
    }
    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }
    public String getEnd_time() {
        return end_time;
    }
    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
    public String getVehicle_id() {
        return vehicle_id;
    }
    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
    // Optionally, calculate end_time based on duration if needed
    public LocalTime calculateEndTime(LocalTime startTime) {
        return startTime.plusHours((long) duration);
    }

    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }

    public Long getEvSpot() {
        return evSpot;
    }

    public void setEvSpot(Long evSpot) {
        this.evSpot = evSpot;
    }
}
