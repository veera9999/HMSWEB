package com.HMS.repository;


import com.HMS.entity.Patient;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface PatientRepository extends CrudRepository<Patient, Integer> {


    // Method to search patients by name (case-insensitive)
    Iterable<Patient> findByNameContainingIgnoreCase(String name);

    Iterable<Patient> findByActiveTrue();
    Iterable<Patient> findByNameContainingIgnoreCaseAndActiveTrue(String name);
}