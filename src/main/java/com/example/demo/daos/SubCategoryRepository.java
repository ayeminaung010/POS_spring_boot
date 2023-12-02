package com.example.demo.daos;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.SubCategory;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Integer> {

	SubCategory findBySubCategoryName(String subCategoryName);
	
	Page<SubCategory> findBySubCategoryNameContainingIgnoreCase(String query,Pageable pageable);
	Page<SubCategory> findAll(Pageable pageable);
}
