package com.HMS.repository;


import com.HMS.entity.Appointment;
import com.HMS.entity.TimeSlot;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface AppointmentRepository extends CrudRepository<Appointment, Integer> {

    @Query("SELECT a FROM Appointment a WHERE a.id NOT IN (SELECT b.appointment.id FROM Bill b)")
    Iterable<Appointment> findByPatientId(int patientId);

    Iterable<Appointment> findByPatientIdAndDoctorId(int patientID, int doctorID);

    Appointment findByPatientIdAndDoctorIdAndTimeSlotId(int patientID, int doctorID, int timeSlotID);

}