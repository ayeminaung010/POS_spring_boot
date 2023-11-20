package com.example.demo.Controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
	
	@GetMapping("/shop")
	public String shop() {
		return "user/shop/index";
	}
	
	@GetMapping("/shop/detail") // To show with id & change this path 
	public String shopDetail() {
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
