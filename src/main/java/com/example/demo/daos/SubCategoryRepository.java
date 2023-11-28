package com.example.demo.daos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.SubCategory;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Integer> {

	SubCategory findBySubCategoryName(String subCategoryName);
	
	List<SubCategory> findBySubCategoryNameContainingIgnoreCase(String query);
}
