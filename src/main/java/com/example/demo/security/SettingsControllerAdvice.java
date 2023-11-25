package com.example.demo.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.demo.daos.BrandRepository;
import com.example.demo.daos.CategoryRepository;
import com.example.demo.daos.ProductRepository;
import com.example.demo.daos.SubCategoryRepository;
import com.example.demo.dto.CartItem;
import com.example.demo.model.Brand;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.model.SubCategory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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

	@Autowired
	HttpSession session;

	@Autowired
	ObjectMapper objectMapper;
	
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
	
	@ModelAttribute("orderedItem")
	public Map<String, Double> getOrderedItem() throws JsonMappingException, JsonProcessingException {
		Map<String, Double> productPrices = new HashMap<>();
		
		String cartItemJson = (String) session.getAttribute("cart");
		if (cartItemJson != null) {
			List<CartItem> cartItem = objectMapper.readValue(cartItemJson, new TypeReference<List<CartItem>>() {
			});

			for (CartItem cart : cartItem) {
				Double productTotalPrice = cart.getPrice() * cart.getQuantity();
				 // Add product name and total price to the HashMap
                productPrices.put(cart.getName(), productTotalPrice);
			}
		} else {
		}
		return productPrices;
	}

}
