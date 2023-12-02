package com.example.demo.daos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.PaymentType;

@Repository
public interface PaymentTypeRepository extends JpaRepository <PaymentType, Integer> {
	PaymentType findByPaymentTypeName(String paymentTypeName);
	
	List<PaymentType> findByPaymentTypeNameContainingIgnoreCase(String query);
}
