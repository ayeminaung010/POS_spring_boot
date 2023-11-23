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
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotNull
	@NotEmpty(message = "Name is required..!")
	private String name;
	
	@NotNull
	@NotEmpty(message = "Email address is required..!")
	@Column(unique = true)
	private String email;
	
	@NotNull
	@NotEmpty(message = "Password is required..!")
	private String password;
//	@NotNull
//	@NotEmpty(message = "Confirm Password is required..!")
//	@Size(min = 6, message = "Password must be at least 6 characters long")
	@Transient
	private String confirmPassword;
	private String role = "USER";
	
	
	@Transient
	private String currentPassword;
	

	@Transient
	private boolean rememberMe;
	
	@CreationTimestamp
	@Column(name = "created_time")
	private Timestamp createdTime;
	@UpdateTimestamp
	@Column(name = "updated_time")
	private Timestamp updatedTime;

	@Transient
	private Boolean agreeTermAndPolicy;

	public Boolean getAgreeTermAndPolicy() {
		return agreeTermAndPolicy;
	}

	public void setAgreeTermAndPolicy(Boolean agreeTermAndPolicy) {
		this.agreeTermAndPolicy = agreeTermAndPolicy;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}
//	private boolean enabled;
//
//	public boolean isEnabled() {
//		return enabled;
//	}
//
//	public void setEnabled(boolean enabled) {
//		this.enabled = enabled;
//	}

//	@NotNull(message = "Agree term and policy is required..!")

}
