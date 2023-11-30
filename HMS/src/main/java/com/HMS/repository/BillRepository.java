package com.HMS.repository;


import com.HMS.entity.Appointment;
import com.HMS.entity.Bill;
import com.HMS.entity.Doctor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface BillRepository extends CrudRepository<Bill, Integer> {

    Bill findByAppointmentId(int appoitmentId);

    Iterable<Bill> findByActiveTrue();

    @Query("SELECT b FROM Bill b WHERE LOWER(b.appointment.patient.name) LIKE LOWER(CONCAT('%',:patientName,'%')) AND b.active = true")
    Iterable<Bill> findByPatientNameContainingIgnoreCaseAndActiveTrue(@Param("patientName") String patientName);

}

