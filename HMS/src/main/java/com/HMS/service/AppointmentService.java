package com.HMS.service;

import com.HMS.entity.Appointment;
import com.HMS.entity.TimeSlot;

public interface AppointmentService {
    Iterable<Appointment> listAppointments();

    Iterable<Appointment> listPatientAppointments(int patientId);
    void createAppointment(Appointment appointment);

    boolean hasOverlappingTimeSlot(Appointment appointment);
}
