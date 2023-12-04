package com.example.demo.daos;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
	Page<Payment> findByStatus(String status,Pageable pageable);
	
	Page<Payment> findByTransactionIdContainingIgnoreCase(String query,Pageable pageable);
	
	Page<Payment> findByTransactionIdContainingIgnoreCaseAndStatus(String transactionId, String status,Pageable pageable);
	
	Page<Payment> findAll(Pageable pageable);

	List<Payment> findAllById(Integer id);

	

	
}
