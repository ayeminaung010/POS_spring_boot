package com.example.demo.security;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.demo.daos.BrandRepository;
import com.example.demo.daos.CategoryRepository;
import com.example.demo.daos.OrderRepository;
import com.example.demo.daos.PaymentRepository;
import com.example.demo.daos.ProductRepository;
import com.example.demo.daos.SubCategoryRepository;
import com.example.demo.dto.CartItem;
import com.example.demo.model.Brand;
import com.example.demo.model.Category;
import com.example.demo.model.Order;
import com.example.demo.model.Payment;
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
	OrderRepository orderRepository;
	
	@Autowired
	PaymentRepository paymentRepository;

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
		for (Product product : products) {
	        double discount = product.getDiscount();
	        if(discount != 0.0) {
	        	double discountPrice = calculateDiscountPrice(discount, product.getPrice());
		        product.setDiscountPrice(discountPrice);
	        }
	        
	    }
		return products;
	}
	
	@ModelAttribute("discountProducts")
	public List<Product> getDisProducts() {
		List<Product> products = productRepository.findByDiscountGreaterThan(0.0);
		return products;
	}
	
	@ModelAttribute("showDisProducts")
	public List<Product> getShowDisProducts() {
		List<Product> products = productRepository.findByDiscountGreaterThan(0.0);
	    for (Product product : products) {
	        double discount = product.getDiscount();
	        double discountPrice = calculateDiscountPrice(discount, product.getPrice());
	        product.setDiscountPrice(discountPrice);
	        
	    }
	    return products;
	}
	
	@ModelAttribute("cartItemCount")
	public Integer getCartCount() {
		String cartItemJson = (String) session.getAttribute("cart");
	    int itemCount = 0;

	    if (cartItemJson != null && !cartItemJson.isEmpty()) {
	        try {
	            List<CartItem> cartItems = objectMapper.readValue(cartItemJson, new TypeReference<List<CartItem>>() {});
	            itemCount = cartItems.stream().mapToInt(CartItem::getQuantity).sum();
	        } catch (Exception e) {
	            System.out.println("Error reading cart items: " + e.getMessage());
	        }
	    }

	    return itemCount;
	}
	
	@ModelAttribute("getTotalPrice")
	public Double getTotalPrice() {
		try {
			String cartItemJson = (String) session.getAttribute("cart");
			Double totalPrice = 0.0;
			if (cartItemJson != null) {
				List<CartItem> cartItem = objectMapper.readValue(cartItemJson, new TypeReference<List<CartItem>>() {
				});
				
				for (CartItem cart : cartItem) {
					Double productTotalPrice = cart.getPrice() * cart.getQuantity();
					totalPrice += productTotalPrice;
				}
				return totalPrice;
			} 
			return totalPrice;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	
	@ModelAttribute("orderedItem")
	public Map<String, Double> getOrderedItem() throws JsonMappingException, JsonProcessingException {
	    Map<String, Double> productPrices = new HashMap<>();

	    String cartItemJson = (String) session.getAttribute("cart");
	    if (cartItemJson != null && !cartItemJson.isEmpty()) {
	        try {
	            List<CartItem> cartItem = objectMapper.readValue(cartItemJson, new TypeReference<List<CartItem>>() {});

	            for (CartItem cart : cartItem) {
	                Double productTotalPrice = cart.getPrice() * cart.getQuantity();
	                // Add product name and total price to the HashMap
	                productPrices.put(cart.getName(), productTotalPrice);
	            }
	        } catch (Exception e) {
	            System.out.println("Error reading cart items: " + e.getMessage());
	        }
	    }

	    return productPrices;
	}
	
	@ModelAttribute("orders")
	public List<Order> getShowAllOrders() {
		List<Order> orders = orderRepository.findAll();

	    return orders;
	}
	
	@ModelAttribute("successOrders")
	public List<Order> getShowSuccessOrders() {
		Page<Order> orderPage = orderRepository.findByStatus("SUCCESS", Pageable.unpaged());
	    List<Order> orders = orderPage.getContent();

	    return orders;
	}
	
	@ModelAttribute("rejectedOrders")
	public List<Order> getShowRejectedOrders() {
		Page<Order> orderPage = orderRepository.findByStatus("REJECTED", Pageable.unpaged());
	    List<Order> orders = orderPage.getContent();

	    return orders;
	}
	
	@ModelAttribute("pendingOrders")
	public List<Order> getShowPendingOrders() {
		Page<Order> orderPage = orderRepository.findByStatus("PENDING", Pageable.unpaged());
	    List<Order> orders = orderPage.getContent();

	    return orders;
	}
	
	@ModelAttribute("totalIncome")
	public double getShowTotalIcome() {
		Page<Order> orderPage = orderRepository.findByStatus("SUCCESS", Pageable.unpaged());
	    List<Order> orders = orderPage.getContent();
	    
	    double totalIncome = 0.0;

	    for (Order order : orders) {
	        totalIncome += order.getTotalPrice();
	    }
	    
	    return totalIncome;
	}

	@ModelAttribute("pendingIncome")
	public double getPendingIcome() {
		Page<Order> orderPage = orderRepository.findByStatus("PENDING", Pageable.unpaged());
	    List<Order> orders = orderPage.getContent();
	    
	    double totalIncome = 0.0;

	    for (Order order : orders) {
	        totalIncome += order.getTotalPrice();
	    }
	    
	    return totalIncome;
	}
	
	
	//calculate discount price
	public Double calculateDiscountPrice(double discount,double originalPrice) {
		if (discount < 0 || discount > 100) {
            return null;
        }
		double discountAmount = (originalPrice * discount) / 100;
        double discountedPrice = originalPrice - discountAmount;

		return discountedPrice;
	}

}
