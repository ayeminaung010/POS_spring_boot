package com.example.demo.daos;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
	Brand findByBrandName(String brandName);
	
	Page<Brand> findByBrandNameContainingIgnoreCase(String query,Pageable pageable);
	Page<Brand> findAll(Pageable pageable);
}
