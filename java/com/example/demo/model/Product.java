package com.example.demo.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotEmpty(message = "Product name is required!!")
	@Size(min = 1, max = 255, message = "Product name must be between 1 and 255 characters")
	@Column(unique = true,length = 255,nullable = false)
	private String name;


	@NotNull
	@NotEmpty(message = "Description is required!!")
	@Column(length = 500,nullable = false)
	private String description;

	@NotNull(message = "Price is required!!")
	@Min(value = 0, message = "Price should be a positive numerical value!!")
	private double price;

	
	@NotNull(message = "Stock is required!!")
	@Min(value = 1, message = "Stock should be a non-negative numerical value!!")
	private Integer stock;
	
    private String  thumbnailImage;
		
	private double discount;
	
	@ManyToOne
	@JoinColumn(name = "brandId", referencedColumnName = "brandId", nullable = true)
	private Brand brand;

	@ManyToOne
	@NotNull
	private SubCategory subCategory;
	
	@Column(name = "created_time")
	private Date createdTime;
	@Column(name = "updated_time")
	private Date updatedTime;
	
	
	public String getThumbnailImage() {
		return thumbnailImage;
	}

	public void setThumbnailImage(String thumbnailImage) {
		this.thumbnailImage = thumbnailImage;
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

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public SubCategory getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(SubCategory subCategory) {
		this.subCategory = subCategory;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public int getStock() {
		return stock != null ? stock.intValue() : 0;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}
	
	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
}
