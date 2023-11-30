package com.HMS.repository;


import com.HMS.entity.Doctor;
import com.HMS.entity.TimeSlot;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface TimeSlotRepository extends CrudRepository<TimeSlot, Integer> {

    Iterable<TimeSlot> findByDoctorId(int doctorId);

}