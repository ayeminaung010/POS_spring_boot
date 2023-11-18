package com.example.demo.Controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.model.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	@GetMapping("/home")
	public String home(Model model,HttpSession session) {
		if(session != null) {
		    User user =  (User) session.getAttribute("user");
		    if (user != null) {
		        System.out.println("User role" + user.getRole());
		        if (user.getRole().equals("ADMIN")) {
		            return "/admin/home";
		        }else {
		        	return "/user/home";
		        }
		    }
		}
		System.out.println("session user not found");
		return "/login";
	}
}
