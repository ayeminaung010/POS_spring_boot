package com.example.demo.Controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.daos.UserRepository;
import com.example.demo.model.User;

import jakarta.validation.Valid;



@Controller
public class ManageUserController {
	@Autowired
	private UserRepository userRepo;
	
	@GetMapping("/manageuser")
	public String showUser(@RequestParam(name = "search", required = false) String query,
			@RequestParam(defaultValue = "1") int page,
	         @RequestParam(defaultValue = "10") int size,
	         @RequestParam(defaultValue = "createdTime") String sortBy,
			Model model) {
		
		Page<User> usersPage;
		PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sortBy).descending());
		
		if (query != null && !query.isEmpty()) {
			usersPage = userRepo.findByNameContainingIgnoreCase(query.trim(), pageRequest);
		} else {
			usersPage = userRepo.findAll(pageRequest);
		}
		List<User> users = usersPage.getContent();
		User user = new User();
		model.addAttribute("user", user);
		model.addAttribute("userList", users);
		model.addAttribute("currentPage", usersPage.getNumber() + 1);
	    model.addAttribute("totalPages", usersPage.getTotalPages());
	    
		return "admin/manageuser/index";			
	}
	
	@GetMapping("/manageuser/add")
	public String addUser(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "admin/manageuser/add";
	}
	@PostMapping("/manageuser/add")
	public String SaveUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "admin/manageuser/add";
		}
		User existingName = userRepo.findByName(user.getName());
		User existingEmail = userRepo.findByEmail(user.getEmail());
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String userPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(userPassword);
		user.setRole("ADMIN");
		if (existingEmail != null) {
			bindingResult.rejectValue("email", "error.user", "Email already exists");
			return "admin/manageuser/add";
		}
		if (existingName != null) {
			bindingResult.rejectValue("name", "error.user", "Name already exists");
			return "admin/manageuser/add";
		}
		userRepo.save(user);
		redirectAttributes.addFlashAttribute("success", "User Add successful!!");
		return "redirect:/manageuser";
	}
	
	
	@GetMapping("/manageuser/update/{id}")
	public String updateUser(@PathVariable("id") Integer id, Model model) {
		
		User user = userRepo.getReferenceById(id);
		model.addAttribute("user", user);
		return "admin/manageuser/update";
	}
	
	@PostMapping("/manageuser/updating/{id}")
	public String editUser(@PathVariable("id") Integer id, @ModelAttribute("user") @Valid User user,BindingResult bindingResult,
			 Model model, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
	        return "admin/manageuser/update";
	    }
		User alreadyUser = userRepo.findById(user.getId()).orElse(null);
		User emailCheckUser = userRepo.findByEmail(user.getEmail());
		
		alreadyUser.setName(user.getName());
		
		if(emailCheckUser == null) {
			alreadyUser.setEmail(user.getEmail());
			userRepo.save(alreadyUser);
			redirectAttributes.addFlashAttribute("success", "Update Profile Success ... !");
		}else {
			if(emailCheckUser.getId() == user.getId()) {
				alreadyUser.setEmail(user.getEmail());
				userRepo.save(alreadyUser);
				redirectAttributes.addFlashAttribute("success", "Update Profile Success ... !");
			}else{
				model.addAttribute("error", "Email already exists ... !");
				
				return "admin/manageuser/update";
			}
		}
		return "redirect:/manageuser";
	}

	@PostMapping("/manageuser/delete")
	public String deleteUser(@ModelAttribute("user") User user, Model model, RedirectAttributes redirectAttributes) {
		userRepo.deleteById(user.getId());
		redirectAttributes.addFlashAttribute("success", "User Account Delete successful!!");
		return "redirect:/manageuser";
		
	}
}
	
	
	


