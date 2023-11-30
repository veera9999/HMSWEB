package com.HMS.service;

import com.HMS.entity.Doctor;
import com.HMS.entity.Doctor;

import java.util.Optional;

public interface DoctorService {
    Iterable<Doctor> listDoctors();

    Iterable<Doctor> listActiveDoctors();

    void createDoctor(Doctor doctor);

    Doctor updateDoctor(Doctor doctor);

    Iterable<Doctor> searchDoctorsByName(String name);

    void deleteDoctor(int doctor);

    Optional<Doctor> restoreDoctor(int doctor);

    Doctor restoreLatestDeletedDoctor();
}
