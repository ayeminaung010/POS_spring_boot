package com.example.demo.Controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.daos.ProductRepository;
import com.example.demo.model.Product;


@Controller
public class PageController {
	@Autowired
	ProductRepository productRepository;
	@GetMapping("/shop")
	public String shop() {
		return "user/shop/index";
	}
	
	@GetMapping("/shop/detail/{id}") // To show with id & change this path 
	public String shopDetail(@PathVariable("id") Integer id,Model model) {
		System.out.println("Product Id : " + id);
		Product detailProduct = productRepository.getReferenceById(id);
		model.addAttribute("product", detailProduct);
		return "user/shop/detail";
	}
	
	@GetMapping("/contact")
	public String contact() {
		return "user/contact/index";
	}
	
	@GetMapping("/cart")
	public String cart() {
		return "user/cart/index";
	}
	
	@GetMapping("/cart/payment")
	public String payment() {
		return "user/cart/payment";
	}
	
	@GetMapping("/cart/address")
	public String address() {
		return "user/cart/address";
	}
}
