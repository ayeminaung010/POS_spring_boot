package com.example.demo.model;

import java.sql.Date;

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
	
	@Column(name = "created_time")
	private Date createdTime;
	@Column(name = "updated_time")
	private Date updatedTime;
	
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
