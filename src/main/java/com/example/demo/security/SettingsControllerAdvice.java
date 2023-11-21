package com.example.demo.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.demo.daos.BrandRepository;
import com.example.demo.daos.CategoryRepository;
import com.example.demo.daos.ProductRepository;
import com.example.demo.daos.SubCategoryRepository;
import com.example.demo.model.Brand;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.model.SubCategory;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class SettingsControllerAdvice {
	@Autowired
	SubCategoryRepository subCategoryRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	BrandRepository brandRepository;
	
	@Autowired
	ProductRepository productRepository;

	@ModelAttribute("servletPath")
	String getRequestServletPath(HttpServletRequest request) {
		return request.getServletPath();
	}

	@ModelAttribute("categoryList")
	public List<Category> getCategories() {
		List<Category> categories = categoryRepository.findAll();
		return categories;
	}

	@ModelAttribute("brands")
	public List<Brand> getBrands() {
		List<Brand> brands = brandRepository.findAll();
		return brands;
	}

	@ModelAttribute("subCategories")
	public List<SubCategory> getSubCategories() {
		List<SubCategory> subCategories = subCategoryRepository.findAll();
		return subCategories;
	}
	
	@ModelAttribute("products")
	public List<Product> getProducts() {
		List<Product> products = productRepository.findAll();
		return products;
	}

}
