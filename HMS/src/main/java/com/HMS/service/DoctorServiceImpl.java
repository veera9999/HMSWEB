package com.HMS.service;

import com.HMS.entity.Doctor;
import com.HMS.entity.Doctor;
import com.HMS.entity.Patient;
import com.HMS.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Stack;

@Service
public class DoctorServiceImpl  implements DoctorService{
    @Autowired
    private DoctorRepository doctorRepository;

    private Stack<Doctor> deletedDoctors = new Stack<>();

    @Override
    public Iterable<Doctor> listDoctors(){

        return doctorRepository.findByActiveTrue();
    }

    @Override
    public Iterable<Doctor> listActiveDoctors(){

        return doctorRepository.findByActiveTrue();
    }

    @Override
    public void createDoctor(Doctor doctor) {

        // Save the new doctor to the database
        doctorRepository.save(doctor);
    }

    @Override
    public Doctor updateDoctor(Doctor doctor) {
        // Check if the doctor with the given ID exists in the database
        Optional<Doctor> existingDoctorOptional = doctorRepository.findById(doctor.getId());
        if (existingDoctorOptional.isPresent()) {
            // Save the updated doctor to the database
            doctorRepository.save(doctor);
        }
        return doctor;
    }

    @Override
    public Iterable<Doctor> searchDoctorsByName(String name) {
        // Use the doctorRepository to search for doctors by name
        return doctorRepository.findByNameContainingIgnoreCaseAndActiveTrue(name);
    }

    @Override
    public void deleteDoctor(int doctorId) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);

        if (optionalDoctor.isPresent()) {
            Doctor doctor = optionalDoctor.get();
            doctor.setActive(false); // Set the active status to false
            deletedDoctors.push(doctor);
            doctorRepository.save(doctor);
        }
    }
    @Override
    public Optional<Doctor> restoreDoctor(int doctorId) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);

        if (optionalDoctor.isPresent()) {
            Doctor doctor = optionalDoctor.get();
            doctor.setActive(true); // Set the active status to false
            doctorRepository.save(doctor);

        }
        return optionalDoctor;
    }

    @Override
    public Doctor restoreLatestDeletedDoctor() {
        if (!deletedDoctors.isEmpty()) {
            Doctor latestDeletedDoctor = deletedDoctors.pop();
            latestDeletedDoctor.setActive(true);
            doctorRepository.save(latestDeletedDoctor);

            return latestDeletedDoctor;
        }
        return null;

    }
}
