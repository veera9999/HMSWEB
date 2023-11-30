package com.HMS.service;

import com.HMS.entity.Patient;
import com.HMS.entity.TimeSlot;
import com.HMS.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeSlotServiceImpl  implements TimeSlotService{
    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Override
    public Iterable<TimeSlot> listTimeSlots(){

        return timeSlotRepository.findAll();
    }

    @Override
    public void createTimeSlot(TimeSlot timeSlot) {
        // Save the new timeSlot to the database
        timeSlotRepository.save(timeSlot);
    }

    @Override
    public Iterable<TimeSlot> searchTimeSlotByDoctorID(int doctorId) {
        // Use the patientRepository to search for patients by name
        return timeSlotRepository.findByDoctorId(doctorId);
    }

    public boolean hasOverlappingTimeSlot(TimeSlot newTimeSlot) {
        Iterable<TimeSlot> existingTimeSlots = timeSlotRepository.findByDoctorId(newTimeSlot.getDoctor().getId());

        for (TimeSlot existingTimeSlot : existingTimeSlots) {
            if (isOverlapping(newTimeSlot, existingTimeSlot)) {
                return true; // Overlapping time slot found
            }
        }

        return false; // No overlapping time slots found
    }

    private boolean isOverlapping(TimeSlot timeSlot1, TimeSlot timeSlot2) {
        // Check if the start time of one slot falls between the start and end time of the other slot
        return (timeSlot1.getStartTime().before(timeSlot2.getEndTime()) &&
                timeSlot1.getEndTime().after(timeSlot2.getStartTime())) ||
                (timeSlot2.getStartTime().before(timeSlot1.getEndTime()) &&
                        timeSlot2.getEndTime().after(timeSlot1.getStartTime()));
    }
}
