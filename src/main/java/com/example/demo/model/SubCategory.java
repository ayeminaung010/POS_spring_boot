package com.example.demo.model;

import java.sql.Timestamp;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "sub_category")
public class SubCategory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int subCategoryId;
	@NotNull
	@NotEmpty(message = "SubCategory Name is required!!")
	private String subCategoryName;

	@ManyToOne
	@JoinColumn(name = "categoryId", referencedColumnName = "categoryId", nullable = false)
	private Category category;

	@OneToMany(mappedBy = "subCategory", cascade = CascadeType.ALL)
	private Set<Product> products;
	
	@CreationTimestamp
	@Column(name = "created_time")
	private Timestamp createdTime;
	@UpdateTimestamp
	@Column(name = "updated_time")
	private Timestamp updatedTime;
	
	public Set<Product> getProducts() {
		return products;
	}
	public void setProducts(Set<Product> Products) {
		this.products = Products;
	}
	
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public int getSubCategoryId() {
		return subCategoryId;
	}

	public void setSubCategoryId(int subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	public String getSubCategoryName() {
		return subCategoryName;
	}

	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}
	public Timestamp getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}
	public Timestamp getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}


}
