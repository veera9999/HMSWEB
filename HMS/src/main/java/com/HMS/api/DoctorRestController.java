// REST Controller
package com.HMS.api;

import com.HMS.entity.Doctor;
import com.HMS.entity.TimeSlot;
import com.HMS.service.DoctorService;
import com.HMS.service.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/doctor")
public class DoctorRestController {

    private DoctorService doctorService;
    private TimeSlotService timeSlotService;

    @Autowired
    public void setDoctorService(DoctorService doctorService) {

        this.doctorService = doctorService;
    }


    @GetMapping("/getDoctors")
    @ResponseBody
    public ResponseEntity<Iterable<Doctor>> getDoctors() {
        // Search for doctors by name and return them directly
        Iterable<Doctor> doctors = doctorService.listActiveDoctors();
        return new ResponseEntity<Iterable<Doctor>>(doctors, HttpStatus.OK);
    }

    @GetMapping("/searchdoctors")
    @ResponseBody
    public ResponseEntity<Iterable<Doctor>> searchDoctorsByName(@RequestParam String name) {
        // Search for doctors by name and return them directly
        Iterable<Doctor> doctorsByName = doctorService.searchDoctorsByName(name);
        return new ResponseEntity<Iterable<Doctor>>(doctorsByName, HttpStatus.OK);
    }

    @PostMapping("/updatedoctor")
    @ResponseBody
    public ResponseEntity<Doctor> updateDoctor(@RequestBody Doctor updatedDoctor) {
        Doctor updatedDoctorResult = doctorService.updateDoctor(updatedDoctor);

        if (updatedDoctorResult != null) {
            return new ResponseEntity<>(updatedDoctorResult, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deletedoctor")
    @ResponseBody
    public ResponseEntity<?> deleteDoctor(@RequestParam int id) {
        try {
            doctorService.deleteDoctor(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete doctor.");
        }
    }

    // REST endpoint to restore the last deleted doctor
    @PostMapping("/restoredoctor")
    public ResponseEntity<String> restoreLastDeletedDoctor() {
        try {
            Doctor doctor = doctorService.restoreLatestDeletedDoctor();
            if (doctor != null){
                return ResponseEntity.ok().build();
            } else{
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to restore doctor.");
        }
    }


    @Autowired
    public void setTimeSlotService(TimeSlotService timeSlotService) {

        this.timeSlotService = timeSlotService;
    }

    @GetMapping("/getTimeSlotByDoctorId")
    @ResponseBody
    public ResponseEntity<Iterable<TimeSlot>> getTimeSlotByDoctorId(@RequestParam int doctorId) {
        // Search for doctors by name and return them directly
        Iterable<TimeSlot> timeSlots = timeSlotService.searchTimeSlotByDoctorID(doctorId);

        return new ResponseEntity<Iterable<TimeSlot>>(timeSlots, HttpStatus.OK);
    }

    @PostMapping("/createTimeSlot")
    @ResponseBody
    public ResponseEntity<String> createTimeSlot(@RequestBody TimeSlot timeSlot ) {

        // Check for overlapping time slots
        if (timeSlotService.hasOverlappingTimeSlot(timeSlot)) {
            return new ResponseEntity<>("Error: Overlapping time slot", HttpStatus.BAD_REQUEST);
        }

        // If no overlap, proceed to create the time slot
        timeSlotService.createTimeSlot(timeSlot);
        return new ResponseEntity<>("Time slot created successfully", HttpStatus.OK);
    }
}