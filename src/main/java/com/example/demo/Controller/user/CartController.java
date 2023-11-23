package com.example.demo.Controller.user;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CartItem;
import com.example.demo.dto.ResponseHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

@RestController
public class CartController {
	@Autowired
	HttpSession session;
	
	@Autowired
	ObjectMapper objectMapper;

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
    public ResponseEntity<Object> getCart() {
        try {
            String cartItemJson = (String) session.getAttribute("cart");

            if (cartItemJson != null) {
            	List<CartItem> cartItem = objectMapper.readValue(cartItemJson,new TypeReference<List<CartItem>>() {});

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

    
}

//Cart Data: CartItem{id='7', name='new testing', price=9000.0, image='http://res.cloudinary.com/dbwkwowdx/image/upload/v1700638843/f405661b-edfa-47e7-a9f1-15974977269a.jpg', stock=5.0, quantity=1}