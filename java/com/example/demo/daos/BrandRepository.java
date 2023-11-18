package com.example.demo.daos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Brand;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
	Brand findByBrandName(String brandName);
}
