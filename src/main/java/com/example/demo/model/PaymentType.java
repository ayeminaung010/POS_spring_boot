package com.example.demo.model;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "payment_type")
public class PaymentType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int paymentTypeId;
	
	@NotNull
	@NotEmpty(message = "PaymentTypeName is required!!")
	private String paymentTypeName;
	
	@CreationTimestamp
	@Column(name = "created_time")
	private Timestamp createdTime;
	@UpdateTimestamp
	@Column(name = "updated_time")
	private Timestamp updatedTime;
	
	public int getPaymentTypeId() {
		return paymentTypeId;
	}
	public void setPaymentTypeId(int paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}
	public String getPaymentTypeName() {
		return paymentTypeName;
	}
	public void setPaymentTypeName(String paymentTypeName) {
		this.paymentTypeName = paymentTypeName;
	}
}
