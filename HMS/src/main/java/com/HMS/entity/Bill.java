package com.HMS.entity;

import javax.persistence.*;


@Entity
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;
    private String description;
    private double due_amount;

    @Column(columnDefinition = "boolean default true")
    private boolean active = true;  // Added field for soft delete

    public Bill() {
    }

    public Bill(int id, Appointment appointment, String description, double due_amount) {
        this.id = id;
        this.appointment = appointment;
        this.description = description;
        this.due_amount = due_amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDue_amount() {
        return due_amount;
    }

    public void setDue_amount(double due_amount) {
        this.due_amount = due_amount;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
