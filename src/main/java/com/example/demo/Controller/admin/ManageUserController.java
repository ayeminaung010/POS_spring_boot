package com.example.demo.Controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.daos.UserRepository;
import com.example.demo.model.Category;
import com.example.demo.model.SubCategory;
import com.example.demo.model.User;

import jakarta.validation.Valid;



@Controller
public class ManageUserController {
	@Autowired
	private UserRepository userRepo;
	@GetMapping("/manageuser")
	public String showUser(Model model) {
		List<User> userList = userRepo.findAll();
		model.addAttribute("userList", userList);
		User user = new User();
		model.addAttribute("user", user);
		return "admin/manageuser/index";			
	}
	
	@GetMapping("/manageuser/add")
	public String addUser(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "admin/manageuser/add";
	}
	
	@GetMapping("/manageuser/update/{id}")
	public String updateUser(@PathVariable("id") Integer id, Model model) {
		
		User user = userRepo.getReferenceById(id);
		model.addAttribute("user", user);
		return "admin/manageuser/update";
	}
	@PostMapping("/manageuser/update/{id}")
	public String editUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
			@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
	        return "admin/manageuser/update";
	    }
	    User existingUser = userRepo.findByEmail(user.getEmail());

	    if (existingUser != null) {
	    	if(existingUser.getId() != id) {
	    		bindingResult.rejectValue("email", "error.user", "Email already exists");
		        //model.addAttribute("error", "Email already exists");
		        return "admin/manageuser/update";
	    	}
	    }
	    User userById = userRepo.findById(id).orElse(null);
	    
	    userById.setName(user.getName());
	    userById.setEmail(user.getEmail());
	    
		userRepo.save(user);
		redirectAttributes.addFlashAttribute("success", "User Update successful!!");
		return "redirect:/manageuser";
	}

	@PostMapping("/manageuser/delete")
	public String deleteUser(@ModelAttribute("user") User user, Model model, RedirectAttributes redirectAttributes) {
		userRepo.deleteById(user.getId());
		redirectAttributes.addFlashAttribute("success", "User Delete successful!!");
		return "redirect:/manageuser";
		
	}
}
	
	
	


