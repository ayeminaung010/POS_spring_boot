package com.example.demo.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	Category findByCategoryName(String categoryName);
}
