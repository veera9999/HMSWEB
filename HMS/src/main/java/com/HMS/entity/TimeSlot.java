package com.HMS.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "doctor_Id")
    private Doctor doctor;

    private Date startTime;
    private Date endTime;
    private boolean booked;

    // Default constructor needed by JPA
    public TimeSlot() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

//    public boolean isBooked() {
//        return booked;
//    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public String getStatus() {
        return booked ? "Booked" : "Available";
    }
}
