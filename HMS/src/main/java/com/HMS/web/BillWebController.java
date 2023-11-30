package com.HMS.web;

import com.HMS.entity.Bill;
import com.HMS.service.BillService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/web/bill")
public class BillWebController {
    private BillService billService;

    @Autowired
    public void setBillService(BillService billService){
        this.billService = billService;
    }

    @GetMapping("/list")
    public String retrieveBill(Model model) {
        model.addAttribute("bills", billService.listBills());
        return "Bill/list";
    }

    @GetMapping("/createForm")
    public String showBillForm(Model model) {
        model.addAttribute("bill", new Bill());
        return "Bill/createForm";
    }

    @PostMapping("/create")
    public String createBill(@ModelAttribute Bill bill) {
        billService.createBill(bill);
        return "redirect:/Bill/createForm";
    }

    @GetMapping("/searchAndEdit")
    public String searchBillform() {

        return "Bill/searchAndEdit";
    }
}
