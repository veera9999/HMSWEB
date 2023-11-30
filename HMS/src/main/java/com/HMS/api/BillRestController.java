package com.HMS.api;

import com.HMS.entity.*;
import com.HMS.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bill")
public class BillRestController {

    @Autowired
    private BillService billService;

    @GetMapping("/getBills")
    @ResponseBody
    public ResponseEntity<Iterable<Bill>> getBills() {
        Iterable<Bill> bills = billService.listActiveBills();
        return new ResponseEntity<>(bills, HttpStatus.OK);
    }

    @PostMapping("/createBill")
    @ResponseBody
    public ResponseEntity<String> createBill(@RequestBody Bill bill ) {

        // Check for overlapping time slots
        if (billService.hasBill(bill)) {
            return new ResponseEntity<>("Error: bill already exist", HttpStatus.BAD_REQUEST);
        }

        // If no overlap, proceed to create the time slot
        billService.createBill(bill);
        return new ResponseEntity<>("Bill created successfully", HttpStatus.OK);
    }

    @GetMapping("/searchBills")
    @ResponseBody
    public ResponseEntity<Iterable<Bill>> searchBillsByPatientName(@RequestParam String patientName) {
        // Search for bills by Patient name and return them directly
        Iterable<Bill> billsByPatientName = billService.searchBillsByPatientName(patientName);
        return new ResponseEntity<Iterable<Bill>>(billsByPatientName, HttpStatus.OK);
    }

    @PostMapping("/updateBill")
    @ResponseBody
    public ResponseEntity<Bill> updateBill(@RequestBody Bill updatedBill) {
        Bill updatedBillResult = billService.updateBill(updatedBill);

        if (updatedBillResult != null) {
            return new ResponseEntity<>(updatedBillResult, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteBill")
    @ResponseBody
    public ResponseEntity<?> deleteBill(@RequestParam int id) {
        try {
            billService.deleteBill(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete Bill.");
        }
    }

    // REST endpoint to restore the last deleted Bill
    @PostMapping("/restoreBill")
    public ResponseEntity<String> restoreLastDeletedBill() {
        try {
            Bill bill = billService.restoreLatestDeletedBill();
            if (bill != null){
                return ResponseEntity.ok().build();
            } else{
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to restore bill.");
        }
    }

}
