package com.example.demo.daos;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	List<Product> findByDiscountGreaterThan(double discount);
	List<Product> findBySubCategorySubCategoryName(String subCategoryName);
	
	@Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%'))")
	List<Product> searchProducts(@Param("query") String query);
}
