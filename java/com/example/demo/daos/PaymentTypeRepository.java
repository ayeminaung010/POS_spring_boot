package com.example.demo.daos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.PaymentType;

public interface PaymentTypeRepository extends JpaRepository <PaymentType, Integer> {
	PaymentType findByPaymentTypeName(String paymentTypeName);
}
