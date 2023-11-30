// REST Controller
package com.HMS.api;

import com.HMS.entity.Doctor;
import com.HMS.entity.Patient;
import com.HMS.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/patient")
public class PatientRestController {

    private PatientService patientService;

    @Autowired
    public void setPatientService(PatientService patientService) {

        this.patientService = patientService;
    }

    @GetMapping("/getPatients")
    @ResponseBody
    public ResponseEntity<Iterable<Patient>> getPatients() {
        // Search for doctors by name and return them directly
        Iterable<Patient> patients = patientService.listPatients();
        return new ResponseEntity<Iterable<Patient>>(patients, HttpStatus.OK);
    }

    @GetMapping("/searchpatients")
    @ResponseBody
    public ResponseEntity<Iterable<Patient>> searchPatientsByName(@RequestParam String name) {
        // Search for patients by name and return them directly
        Iterable<Patient> patientsByName = patientService.searchPatientsByName(name);
        return new ResponseEntity<Iterable<Patient>>(patientsByName, HttpStatus.OK);
    }

    @PostMapping("/updatepatient")
    @ResponseBody
    public ResponseEntity<Patient> updatePatient(@RequestBody Patient updatedPatient) {
        Patient updatedPatientResult = patientService.updatePatient(updatedPatient);

        if (updatedPatientResult != null) {
            return new ResponseEntity<>(updatedPatientResult, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deletepatient")
    @ResponseBody
    public ResponseEntity<?> deletePatient(@RequestParam int id) {
        try {
            patientService.deletePatient(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete patient.");
        }
    }

    // REST endpoint to restore the last deleted patient
    @PostMapping("/restorepatient")
    public ResponseEntity<String> restoreLastDeletedPatient() {
        try {
            Patient patient = patientService.restoreLatestDeletedPatient();
            if (patient != null){
                return ResponseEntity.ok().build();
            } else{
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to restire patient.");
        }
    }
}