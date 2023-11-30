package com.HMS.service;
import java.util.Optional;
import java.util.Stack;

import com.HMS.entity.Patient;
import com.HMS.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientServiceImpl  implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    private Stack<Patient> deletedPatients = new Stack<>();

    @Override
    public Iterable<Patient> listPatients() {

        return patientRepository.findByActiveTrue();
    }

    @Override
    public void createPatient(Patient patient) {
        // Save the new patient to the database
        patientRepository.save(patient);
    }

    @Override
    public Patient updatePatient(Patient patient) {
        // Check if the patient with the given ID exists in the database
        Optional<Patient> existingPatientOptional = patientRepository.findById(patient.getId());
        if (existingPatientOptional.isPresent()) {
            // Save the updated patient to the database
            patientRepository.save(patient);
        }
        return patient;
    }

    @Override
    public Iterable<Patient> searchPatientsByName(String name) {
        // Use the patientRepository to search for patients by name
        return patientRepository.findByNameContainingIgnoreCaseAndActiveTrue(name);
    }

    @Override
    public void deletePatient(int patientId) {
        Optional<Patient> optionalPatient = patientRepository.findById(patientId);

        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();
            patient.setActive(false); // Set the active status to false
            deletedPatients.push(patient);
            patientRepository.save(patient);
        }
    }
    @Override
    public Optional<Patient> restorePatient(int patientId) {
        Optional<Patient> optionalPatient = patientRepository.findById(patientId);

        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();
            patient.setActive(true); // Set the active status to false
            patientRepository.save(patient);

        }
        return optionalPatient;
    }

    @Override
    public Patient restoreLatestDeletedPatient() {
        if (!deletedPatients.isEmpty()) {
            Patient latestDeletedPatient = deletedPatients.pop();
            latestDeletedPatient.setActive(true);
            patientRepository.save(latestDeletedPatient);

            return latestDeletedPatient;
        }
        return null;

    }
}