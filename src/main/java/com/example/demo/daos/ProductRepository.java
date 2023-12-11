package com.example.demo.daos;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	Page<Product> findByDiscountGreaterThan(Double discount, Pageable pageable);
	
	List<Product> findByDiscountGreaterThan(Double discount);
	
	Page<Product> findBySubCategorySubCategoryName(String subCategoryName,Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) "
			+ "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%'))" )
	Page<Product> searchProducts(@Param("query") String query,Pageable pageable);

	
	@Query("SELECT p FROM Product p " +
		       "WHERE (LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
		       "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')) " +
		       "OR LOWER(CAST(p.discount AS string)) LIKE LOWER(CONCAT('%', :query, '%'))) " +
		       "AND p.discount > 0")
		Page<Product> findByDiscountContainingIgnoreCase(@Param("query") String query, Pageable pageable);

	Page<Product> findByStock(Integer stock, PageRequest pageRequest);
	
	@Query("SELECT p FROM Product p " +
		       "WHERE (LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
		       "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')) " +
		       "OR LOWER(CAST(p.discount AS string)) LIKE LOWER(CONCAT('%', :query, '%'))) " +
		       "AND p.stock <= 0")
		Page<Product> findByOutofstockContainingIgnoreCase(@Param("query") String query, Pageable pageable);

	
}
