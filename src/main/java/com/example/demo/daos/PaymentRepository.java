package com.example.demo.daos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
	List<Payment> findByStatus(String status);
	
	List<Payment> findByTransactionIdContainingIgnoreCase(String query);
	
	List<Payment> findByTransactionIdContainingIgnoreCaseAndStatus(String transactionId, String status);
}
