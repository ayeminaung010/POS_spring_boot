package com.example.demo.Controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.daos.UserRepository;
import com.example.demo.model.User;



@Controller
public class AdminController {
	
	@Autowired 
	UserRepository userRepository;
	
	@GetMapping("/admin/home")
	public String ShowHomePage() {
		return "admin/home";
	}
	
	@GetMapping("/account/profile")
	public String ShowProfile(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "admin/account/profile";
		
	}
	
	@GetMapping("/account/changePassword")
	public String ShowChangePassword(Model model) {
		return "admin/account/changePassword";
		
	}
	
}
