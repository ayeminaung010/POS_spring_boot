package com.example.demo.Controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
	public String getAllPayments(@RequestParam(name = "search", required = false) String query,
			@RequestParam(defaultValue = "1") int page,
	         @RequestParam(defaultValue = "10") int size,
	         @RequestParam(defaultValue = "createdTime") String sortBy,
	         Model model) {
		
		Page<Payment> paymentPage;
		PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sortBy).descending());
		if (query != null && !query.isEmpty()) {
			paymentPage = paymentRepository.findByTransactionIdContainingIgnoreCase(query.trim(), pageRequest);
		} else {
			paymentPage = paymentRepository.findAll(pageRequest);
		}
		
		List<Payment> payments = paymentPage.getContent();
		model.addAttribute("currentPage", paymentPage.getNumber() + 1);
	    model.addAttribute("totalPages", paymentPage.getTotalPages());
		model.addAttribute("payments", payments);
		return "admin/payment-history/index";
	}
	
	@GetMapping("/payment/pending")
	public String getPendingPayments(@RequestParam(name = "search", required = false) String query,
			@RequestParam(defaultValue = "1") int page,
	         @RequestParam(defaultValue = "10") int size,
	         @RequestParam(defaultValue = "createdTime") String sortBy,
	         Model model) {
		
		Page<Payment> paymentPage;
		PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sortBy).descending());
		if (query != null && !query.isEmpty()) {
			paymentPage = paymentRepository.findByTransactionIdContainingIgnoreCaseAndStatus(query.trim(),"PENDING", pageRequest);
		} else {
			paymentPage = paymentRepository.findByStatus("PENDING",pageRequest);
		}
		
		List<Payment> payments = paymentPage.getContent();
		model.addAttribute("currentPage", paymentPage.getNumber() + 1);
	    model.addAttribute("totalPages", paymentPage.getTotalPages());
		model.addAttribute("payments", payments);
		return "admin/payment-history/index";
	}
	
	@GetMapping("/payment/success")
	public String getSuccessPayments(@RequestParam(name = "search", required = false) String query,
			@RequestParam(defaultValue = "1") int page,
	         @RequestParam(defaultValue = "10") int size,
	         @RequestParam(defaultValue = "createdTime") String sortBy,
	         Model model) {
		
		Page<Payment> paymentPage;
		PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sortBy).descending());
		if (query != null && !query.isEmpty()) {
			paymentPage = paymentRepository.findByTransactionIdContainingIgnoreCaseAndStatus(query.trim(),"SUCCESS", pageRequest);
		} else {
			paymentPage = paymentRepository.findByStatus("SUCCESS",pageRequest);
		}
		
		List<Payment> payments = paymentPage.getContent();
		model.addAttribute("currentPage", paymentPage.getNumber() + 1);
	    model.addAttribute("totalPages", paymentPage.getTotalPages());
		model.addAttribute("payments", payments);
		return "admin/payment-history/index";
	}
	
	@GetMapping("/payment/reject")
	public String getRejectPayments(@RequestParam(name = "search", required = false) String query,
			@RequestParam(defaultValue = "1") int page,
	         @RequestParam(defaultValue = "10") int size,
	         @RequestParam(defaultValue = "createdTime") String sortBy,
	         Model model) {
		Page<Payment> paymentPage;
		PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sortBy).descending());
		if (query != null && !query.isEmpty()) {
			paymentPage = paymentRepository.findByTransactionIdContainingIgnoreCaseAndStatus(query.trim(),"REJECTED", pageRequest);
		} else {
			paymentPage = paymentRepository.findByStatus("REJECTED",pageRequest);
		}
		
		List<Payment> payments = paymentPage.getContent();
		model.addAttribute("currentPage", paymentPage.getNumber() + 1);
	    model.addAttribute("totalPages", paymentPage.getTotalPages());
		model.addAttribute("payments", payments);
		return "admin/payment-history/index";
	}
	
}
