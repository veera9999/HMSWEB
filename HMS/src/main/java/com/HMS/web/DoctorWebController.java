package com.HMS.web;

import com.HMS.entity.TimeSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.HMS.service.DoctorService;
import com.HMS.entity.Doctor;
import com.HMS.service.TimeSlotService;
import com.HMS.entity.TimeSlot;

@Controller
@RequestMapping("/web/doctor")
public class DoctorWebController {
    private DoctorService doctorService;
    private TimeSlotService timeSlotService;

    // Doctor controllers
    @Autowired
    public void setDoctorService(DoctorService doctorService){

        this.doctorService = doctorService;
    }

    @GetMapping("/list")
    public String retrieveDoctor(Model model){
        model.addAttribute("doctors", doctorService.listDoctors());
        return "doctor/list";
    }

    @GetMapping("/createForm")
    public String showDoctorForm(Model model) {
        model.addAttribute("doctor", new Doctor());
        return "doctor/createForm";
    }

    @PostMapping("/create")
    public String createDoctor(@ModelAttribute Doctor doctor){
        doctorService.createDoctor(doctor);
        return "redirect:/web/doctor/createForm";
    }

    @GetMapping("/searchAndEdit")
    public String searchdoctorform() {

        return "doctor/searchAndEdit";
    }

    @Autowired
    public void setTimeSlotService(TimeSlotService timeSlotService){

        this.timeSlotService = timeSlotService;
    }

    @GetMapping("/createTimeSlotForm")
    public String showTimeSlotForm(Model model) {
        model.addAttribute("timeSlot", new TimeSlot());
        return "doctor/createTimeSlotForm";
    }

}
