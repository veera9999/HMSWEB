package com.HMS.service;

import com.HMS.entity.Patient;
import com.HMS.entity.TimeSlot;
import java.util.Date;

public interface TimeSlotService {
    Iterable<TimeSlot> listTimeSlots();


    void createTimeSlot(TimeSlot timeSlot);

    Iterable<TimeSlot> searchTimeSlotByDoctorID(int doctorId);

    boolean hasOverlappingTimeSlot(TimeSlot newTimeSlot);
}
