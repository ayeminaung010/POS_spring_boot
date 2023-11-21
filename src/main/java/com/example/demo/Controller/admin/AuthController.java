package com.example.demo.Controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.daos.UserRepository;
import com.example.demo.model.User;

import jakarta.validation.Valid;

@Controller
public class AuthController {
	@Autowired
	private UserRepository userRepository;
	@GetMapping("/")
	public String slahPage(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "user/home";
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		User user  = new User();
		model.addAttribute(user);
		return "/login";
	}

	@GetMapping("/signup")
	public String SignUp(Model model) {
		User user  = new User();
		model.addAttribute(user);
		return "/signup";
	}
	
	@PostMapping("/signup")
	public String userRegister(@Valid User user,BindingResult bindingResult,Model model,RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors()) {
			return "/signup";
		}
		User alreadyUser = userRepository.findByEmail(user.getEmail());
		if(alreadyUser != null) {
			if(user.getPassword().equals(user.getConfirmPassword())) {
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				
				String encodePassword= passwordEncoder.encode(user.getPassword());
				user.setPassword(encodePassword);
				userRepository.save(user);
				redirectAttributes.addFlashAttribute("success", "Register Success ... !");
				return "/user/login";
			}else {
				model.addAttribute("error","Password does not match.. !");
				return "/signup";
			}
		}
		model.addAttribute("error","Email already exits.. !");
		return "/signup";
		
	}
	
	

}
