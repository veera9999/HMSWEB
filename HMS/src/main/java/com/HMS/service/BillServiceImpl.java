package com.HMS.service;

import com.HMS.entity.*;
import com.HMS.repository.AppointmentRepository;
import com.HMS.repository.BillRepository;
import com.HMS.repository.PatientRepository;
import com.HMS.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;

    private Stack<Bill> deletedBills = new Stack<>();

    @Override
    public Iterable<Bill> listBills() {
        return billRepository.findByActiveTrue();
    }

    @Override
    public Iterable<Bill> listActiveBills(){return billRepository.findByActiveTrue();}

    @Override
    public void createBill(Bill bill) {
        billRepository.save(bill);
    }


    @Override
    public boolean  hasBill(Bill bill){

        Bill existingBill = null;
        existingBill = billRepository.findByAppointmentId(bill.getAppointment().getId());
        System.out.println(existingBill);
        if (existingBill != null){
            return true;
        }
        return false;
    }

    @Override
    public Bill updateBill(Bill bill) {
        // Check if the Bill with the given ID exists in the database
        Optional<Bill> existingBillOptional = billRepository.findById(bill.getId());
        if (existingBillOptional.isPresent()) {
            // Save the updated bill to the database
            billRepository.save(bill);
        }
        return bill;
    }

    @Override
    public Iterable<Bill> searchBillsByPatientName(String patientName) {
        // Use the billRepository to search for bills by Patient name
        return billRepository.findByPatientNameContainingIgnoreCaseAndActiveTrue(patientName);
    }

    @Override
    public void deleteBill(int billId) {
        Optional<Bill> optionalBill = billRepository.findById(billId);

        if (optionalBill.isPresent()) {
            Bill bill = optionalBill.get();
            bill.setActive(false); // Set the active status to false
            deletedBills.push(bill);
            billRepository.save(bill);
        }
    }
    @Override
    public Optional<Bill> restoreBill(int billId) {
        Optional<Bill> optionalBill = billRepository.findById(billId);

        if (optionalBill.isPresent()) {
            Bill bill = optionalBill.get();
            bill.setActive(true); // Set the active status to false
            billRepository.save(bill);

        }
        return optionalBill;
    }

    @Override
    public Bill restoreLatestDeletedBill() {
        if (!deletedBills.isEmpty()) {
            Bill latestDeletedBill = deletedBills.pop();
            latestDeletedBill.setActive(true);
            billRepository.save(latestDeletedBill);

            return latestDeletedBill;
        }
        return null;

    }

}
