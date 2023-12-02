package com.example.demo.Controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.daos.PaymentRepository;
import com.example.demo.model.Payment;

@Controller
public class PaymentController {
	
	@Autowired
	PaymentRepository paymentRepository;
	
	@GetMapping("/payment/all")
	public String getAllPayments(@RequestParam(name = "search", required = false) String query,Model model) {
		List<Payment> payments;

		if (query != null && !query.isEmpty()) {
			payments = paymentRepository.findByTransactionIdContainingIgnoreCase(query.trim());
		} else {
			payments = paymentRepository.findAll();
		}
		model.addAttribute("payments", payments);
		return "admin/payment-history/index";
	}
	
	@GetMapping("/payment/pending")
	public String getPendingPayments(@RequestParam(name = "search", required = false) String query,Model model) {
		List<Payment> payments;
		
		if (query != null && !query.isEmpty()) {
			payments = paymentRepository.findByTransactionIdContainingIgnoreCaseAndStatus(query.trim(),"PENDING");
		} else {
			payments = paymentRepository.findByStatus("PENDING");
		}
		
		model.addAttribute("payments", payments);
		return "admin/payment-history/index";
	}
	
	@GetMapping("/payment/success")
	public String getSuccessPayments(@RequestParam(name = "search", required = false) String query,Model model) {
		List<Payment> payments;
		
		if (query != null && !query.isEmpty()) {
			payments = paymentRepository.findByTransactionIdContainingIgnoreCaseAndStatus(query.trim(),"SUCCESS");
		} else {
			payments = paymentRepository.findByStatus("SUCCESS");
		}
		model.addAttribute("payments", payments);
		return "admin/payment-history/index";
	}
	
	@GetMapping("/payment/reject")
	public String getRejectPayments(@RequestParam(name = "search", required = false) String query,Model model) {
		List<Payment> payments;
		
		if (query != null && !query.isEmpty()) {
			payments = paymentRepository.findByTransactionIdContainingIgnoreCaseAndStatus(query.trim(),"REJECTED");
		} else {
			payments = paymentRepository.findByStatus("REJECTED");
		}
		model.addAttribute("payments", payments);
		return "admin/payment-history/index";
	}
}
