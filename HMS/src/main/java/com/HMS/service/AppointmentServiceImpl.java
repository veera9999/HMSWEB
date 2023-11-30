package com.HMS.service;

import com.HMS.entity.Appointment;
import com.HMS.entity.TimeSlot;
import com.HMS.repository.AppointmentRepository;
import com.HMS.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

@Service
public class AppointmentServiceImpl  implements AppointmentService{
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private TimeSlotRepository timeSlotRepository;
    @Override
    public Iterable<Appointment> listAppointments(){

        return appointmentRepository.findAll();
    }

    @Override
    public Iterable<Appointment> listPatientAppointments(int patientId){

        return appointmentRepository.findByPatientId(patientId);
    }

    @Override
    public void createAppointment(Appointment appointment) {
        // Save the new appointment to the database
        appointment.getTimeSlot().setBooked(true);
        timeSlotRepository.save(appointment.getTimeSlot());
        appointmentRepository.save(appointment);
    }

    public boolean hasOverlappingTimeSlot(Appointment appointment) {
        Iterable<Appointment> appointments = appointmentRepository.findByPatientId(appointment.getPatient().getId());

        for (Appointment existingappointment : appointments) {
            if (isOverlapping(appointment.getTimeSlot(), existingappointment.getTimeSlot())) {
                return true; // Overlapping time slot found
            }
        }

        return false; // No overlapping time slots found
    }

    private boolean isOverlapping(TimeSlot timeSlot1, TimeSlot timeSlot2) {
        // Check if the start time of one slot falls between the start and end time of the other slot
        return (timeSlot1.getStartTime().before(timeSlot2.getEndTime()) &&
                timeSlot1.getEndTime().after(timeSlot2.getStartTime())) ||
                (timeSlot2.getStartTime().before(timeSlot1.getEndTime()) &&
                        timeSlot2.getEndTime().after(timeSlot1.getStartTime()));
    }

}
