package com.HMS.service;
import com.HMS.entity.*;

import java.util.Optional;

public interface BillService {
    Iterable<Bill> listBills();

    Iterable<Bill> listActiveBills();

    void createBill(Bill bill);

    boolean hasBill(Bill bill);

    Bill updateBill(Bill bill);

    Iterable<Bill> searchBillsByPatientName(String patientName);

    void deleteBill(int billId);

    Optional<Bill> restoreBill(int billId);

    Bill restoreLatestDeletedBill();
}
