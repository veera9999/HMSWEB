package com.HMS.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.HMS.service.PatientService;
import com.HMS.entity.Patient;


@Controller
@RequestMapping("/web/patient")
public class PatientWebController {
    private PatientService patientService;

    // Patient controllers

    @Autowired
    public void setPatientService(PatientService patientService){

        this.patientService = patientService;
    }

    @GetMapping("/list")
    public String retrievePatients(Model model){
        model.addAttribute("patients", patientService.listPatients());
        return "patient/list";
    }

    @GetMapping("/createForm")
    public String showPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "patient/createForm";
    }

    @PostMapping("/create")
    public String createPatient(@ModelAttribute Patient patient){
        patientService.createPatient(patient);
        return "redirect:/web/patient/createForm";
    }

    @GetMapping("/searchAndEdit")
    public String searchPatientsForm() {
        return "patient/searchAndEdit";
    }

}
