package com.HMS.repository;


import com.HMS.entity.Doctor;
import com.HMS.entity.Patient;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface DoctorRepository extends CrudRepository<Doctor, Integer> {

    // Method to search patients by name (case-insensitive)
    Iterable<Doctor> findByNameContainingIgnoreCase(String name);

    Iterable<Doctor> findByActiveTrue();
    Iterable<Doctor> findByNameContainingIgnoreCaseAndActiveTrue(String name);
}