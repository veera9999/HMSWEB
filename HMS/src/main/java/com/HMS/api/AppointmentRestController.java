// REST Controller
package com.HMS.api;


import com.HMS.entity.Appointment;
import com.HMS.entity.Doctor;
import com.HMS.entity.TimeSlot;
import com.HMS.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/appointment")
public class AppointmentRestController {

    private AppointmentService appointmentService;

    @Autowired
    public void setAppointmentService(AppointmentService appointmentService) {

        this.appointmentService = appointmentService;
    }

//    @GetMapping("/searchappointments")
//    @ResponseBody
//    public ResponseEntity<Iterable<Appointment>> searchAppointmentsByName(@RequestParam String name) {
//        // Search for appointments by name and return them directly
//        Iterable<Appointment> appointmentsByName = appointmentService.searchAppointmentsByName(name);
//        System.out.println();
//        return new ResponseEntity<Iterable<Appointment>>(appointmentsByName, HttpStatus.OK);
//    }

    @GetMapping("/getPatientAppointments")
    @ResponseBody
    public ResponseEntity<Iterable<Appointment>> getPatientAppointments(@RequestParam int patientId) {
        // Search for doctors by name and return them directly
        Iterable<Appointment> Appointments = appointmentService.listPatientAppointments(patientId);
        return new ResponseEntity<Iterable<Appointment>>(Appointments, HttpStatus.OK);
    }

    @PostMapping("/createAppointment")
    @ResponseBody
    public ResponseEntity<String> createTimeSlot(@RequestBody Appointment appointment ) {

        // Check for overlapping time slots
        if (appointmentService.hasOverlappingTimeSlot(appointment)) {
            return new ResponseEntity<>("Error: Overlapping time slot", HttpStatus.BAD_REQUEST);
        }

        // If no overlap, proceed to create the time slot
        appointmentService.createAppointment(appointment);
        return new ResponseEntity<>("Appointment created successfully", HttpStatus.OK);
    }

//    @DeleteMapping("/deleteappointment")
//    @ResponseBody
//    public ResponseEntity<?> deleteAppointment(@RequestParam int id) {
//        try {
//            appointmentService.deleteAppointment(id);
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Failed to delete appointment.");
//        }
//    }


}