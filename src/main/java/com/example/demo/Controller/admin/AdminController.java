package com.example.demo.Controller.admin;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.daos.UserRepository;
import com.example.demo.model.User;
import com.example.demo.service.UserOwnDetail;

import jakarta.validation.Valid;

@Controller
public class AdminController {

	@Autowired
	UserRepository userRepository;

	@GetMapping("/admin/home")
	public String ShowHomePage() {
		return "admin/home";
	}

	@GetMapping("/account/profile")
	public String ShowProfile(@AuthenticationPrincipal UserOwnDetail loginUser, Model model) {
		String email = loginUser.getEmail();
		User user = userRepository.findByEmail(email);
		model.addAttribute("user", user);

		return "admin/account/profile";

	}

	@GetMapping("/account/changePassword")
	public String ShowChangePassword(@AuthenticationPrincipal UserOwnDetail loginUser, Model model) {
		String email = loginUser.getEmail();
		User user = userRepository.findByEmail(email);
		model.addAttribute("user", user);
		return "admin/account/changePassword";

	}

	@PostMapping("/account/changePassword")
	public String editChangePassword(@ModelAttribute("user") User user, Model model,
	        RedirectAttributes redirectAttributes) {

	    // Retrieve the user from the repository using a method that returns the actual entity
	    User alreadyUser = userRepository.findById(user.getId()).orElse(null);

	    // Check if the user exists
	    if (alreadyUser != null) {
	        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	        // Check if the entered current password matches the stored hashed password
	        boolean currentPasswordMatches = passwordEncoder.matches(user.getCurrentPassword(), alreadyUser.getPassword());

	        if (currentPasswordMatches) {
	            // Check if the new password and confirm password match
	            if (user.getPassword().equals(user.getConfirmPassword())) {
	                // Hash the new password and save it
	                String encodedPassword = passwordEncoder.encode(user.getPassword());
	                alreadyUser.setPassword(encodedPassword);
	                userRepository.save(alreadyUser);

	                model.addAttribute("success", "Change Password Success ... !");
	                return "admin/account/changePassword";
	            } else {
	                model.addAttribute("error", "New Password and Confirm Password do not match..!!");
	            }
	        } else {
	            model.addAttribute("error", "Current Password does not match..!!");
	        }
	    } else {
	        model.addAttribute("error", "User not found..!!");
	    }

	    return "admin/account/changePassword";
	}

}
