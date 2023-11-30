package com.HMS.web;

import com.HMS.entity.Appointment;
import com.HMS.service.AppointmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/web/appointment")
public class AppointmentWebController {
    private AppointmentService appointmentService;

    @Autowired
    public void setAppointmentService(AppointmentService appointmentService){
        this.appointmentService = appointmentService;
    }

    @GetMapping("/list")
    public String retrieveAppointment(Model model) {
        model.addAttribute("appointments", appointmentService.listAppointments());
        return "appointment/list";
    }

    @GetMapping("/createForm")
    public String showAppointmentForm(Model model) {
        model.addAttribute("appointment", new Appointment());
        return "appointment/createForm";
    }

    @PostMapping("/create")
    public String createAppointment(@ModelAttribute Appointment appointment) {
        appointmentService.createAppointment(appointment);
        return "redirect:/appointment/createFor";
    }

}
