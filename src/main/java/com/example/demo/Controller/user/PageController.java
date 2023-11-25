package com.example.demo.Controller.user;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.daos.ProductRepository;
import com.example.demo.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;


@Controller
public class PageController {
	@Autowired
	ProductRepository productRepository;
		
	@Autowired
	HttpSession session;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@GetMapping("/shop")
	public String shop() {
		return "user/shop/index";
	}
	
	@GetMapping("/shop/detail/{id}") 
	public String shopDetail(@PathVariable("id") Integer id,Model model) {
		Product detailProduct = productRepository.getReferenceById(id);
		Double discount = detailProduct.getDiscount();
		if(discount != 0.0) {
			double discountPrice = calculateDiscountPrice(discount,detailProduct.getPrice());
			model.addAttribute("discountPrice", discountPrice);
		}
		model.addAttribute("product", detailProduct);
		return "user/shop/detail";
	}
	
	@GetMapping("/contact")
	public String contact() {
		return "user/contact/index";
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
	
//	//calculates the total
//	public void calculateTotalPrice(Model model) {
//		try {
//			String cartItemJson = (String) session.getAttribute("cart");
//			Double totalPrice = 0.0;
//			Double deliveryFee = 5000.0;
//			if (cartItemJson != null) {
//				List<CartItem> cartItem = objectMapper.readValue(cartItemJson, new TypeReference<List<CartItem>>() {
//				});
//
//				for (CartItem cart : cartItem) {
//					Double productTotalPrice = cart.getPrice() * cart.getQuantity();
//					totalPrice += productTotalPrice;
//				}
//				totalPrice = totalPrice + deliveryFee;
//				model.addAttribute("totalPrice" , totalPrice);
//			} else {
//			}
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//	}
	
}
