package com.example.demo.Controller.user;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.daos.AddressRepository;
import com.example.demo.dto.CartItem;
import com.example.demo.dto.ResponseHelper;
import com.example.demo.model.Address;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class CartController {
	@Autowired
	HttpSession session;

	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	private AddressRepository addressRepository;

	@PostMapping("/api/v1/updateCart")
	public ResponseEntity<Object> updateCart(@RequestBody List<CartItem> cartItem) {
		try {
			String jsonStr = objectMapper.writeValueAsString(cartItem);
			session.setAttribute("cart", jsonStr);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		ResponseHelper responseHelper = new ResponseHelper();
		responseHelper.setMessage("success");
		responseHelper.setData(cartItem);
		responseHelper.setStatus(true);
		return ResponseEntity.ok(responseHelper);
	}

	@GetMapping("/api/v1/getCart")
	public ResponseEntity<Object> getCart(Model model) {
		try {
			String cartItemJson = (String) session.getAttribute("cart");
			if (cartItemJson != null) {
				List<CartItem> cartItem = objectMapper.readValue(cartItemJson, new TypeReference<List<CartItem>>() {
				});
				ResponseHelper responseHelper = new ResponseHelper();
				responseHelper.setMessage("success");
				responseHelper.setData(cartItem);
				responseHelper.setStatus(true);
				return ResponseEntity.ok(responseHelper);
			} else {
				return ResponseEntity.status(404).body("Cart not found in session");
			}
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error retrieving cart: " + e.getMessage());
		}
	}
	
	@GetMapping("/cart")
	public String cart(Model model) {
		 // get the cart from session
		try {
			Double totalPrice = 0.0;
			String cartItemJson = (String) session.getAttribute("cart");
            if(cartItemJson != null) {
            	List<CartItem> cartItems = objectMapper.readValue(cartItemJson,new TypeReference<List<CartItem>>() {});
                
            	for (CartItem cartItem : cartItems) {
    				totalPrice += cartItem.getPrice();
    			}
            	model.addAttribute("cartItems",cartItems);
            }
            model.addAttribute("totalPrice",totalPrice);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
		return "user/cart/index";
	}
	
	@GetMapping("/cart/payment")
	public String payment() {
		return "user/cart/payment";
	}
	
	@GetMapping("/cart/address")
	public String address(Model model) {
		calculateTotalPrice(model);
		Address address = new Address();
		model.addAttribute("address", address);
		return "user/cart/address";
	}
	
	@PostMapping("/cart/order")
	public String order(@ModelAttribute("address") @Valid Address address,BindingResult bindingResult,Model model) {
		calculateTotalPrice(model);
		if(bindingResult.hasErrors()) {
			return "user/cart/address";
		}
		
//		addressRepository.save(address);
		return "user/cart/successOrder";
	}
	
	//calculate total price
	public void calculateTotalPrice(Model model) {
		try {
			String cartItemJson = (String) session.getAttribute("cart");
			Double totalPrice = 0.0;
			Double deliveryFee = 5000.0;
			if (cartItemJson != null) {
				List<CartItem> cartItem = objectMapper.readValue(cartItemJson, new TypeReference<List<CartItem>>() {
				});

				for (CartItem cart : cartItem) {
					Double productTotalPrice = cart.getPrice() * cart.getQuantity();
					totalPrice += productTotalPrice;
				}
				totalPrice = totalPrice + deliveryFee;
				model.addAttribute("totalPrice" , totalPrice);
			} 
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	

}

