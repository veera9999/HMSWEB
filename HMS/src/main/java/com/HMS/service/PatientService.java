package com.HMS.service;

import com.HMS.entity.Patient;

import java.util.Optional;

public interface PatientService {
    Iterable<Patient> listPatients();


    void createPatient(Patient patient);

    Patient updatePatient(Patient patient);

    Iterable<Patient> searchPatientsByName(String name);

    void deletePatient(int patient);

    Optional<Patient> restorePatient(int patient);

    Patient restoreLatestDeletedPatient();
}
