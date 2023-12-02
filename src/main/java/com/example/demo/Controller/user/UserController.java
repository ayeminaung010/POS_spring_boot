package com.example.demo.Controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.daos.OrderProductRepository;
import com.example.demo.daos.OrderRepository;
import com.example.demo.daos.PaymentRepository;
import com.example.demo.daos.UserRepository;
import com.example.demo.model.Order;
import com.example.demo.model.OrderProducts;
import com.example.demo.model.Payment;
import com.example.demo.model.User;
import com.example.demo.service.UserOwnDetail;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	OrderProductRepository orderProductRepository;
	
	@Autowired
	PaymentRepository paymentRepository;
	
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
	
	@GetMapping("/user/profile")
	public String profile(@AuthenticationPrincipal UserOwnDetail loginUser, Model model) {
		Integer id = loginUser.getId();
		User user = userRepository.findById(id).orElse(null);
		model.addAttribute("user", user);
		return "user/account/profile";
	}
	
	@PostMapping("/user/profile/update")
	public String updateProfile(@ModelAttribute("user") User user, Model model) {
		User alreadyUser = userRepository.findById(user.getId()).orElse(null);
		User emailCheckUser = userRepository.findByEmail(user.getEmail());
		
		alreadyUser.setName(user.getName());
		if(emailCheckUser == null) {
			alreadyUser.setEmail(user.getEmail());
			userRepository.save(alreadyUser);
			model.addAttribute("success", "Update Profile Success ... !");
		}else {
			if(emailCheckUser.getId() == user.getId()) {
				alreadyUser.setEmail(user.getEmail());
				userRepository.save(alreadyUser);
				model.addAttribute("success", "Update Profile Success ... !");
			}else{
				model.addAttribute("error", "Email already exists ... !");
			}
		}
		return "user/account/profile";

	}
	@GetMapping("/user/changepassword")
	public String ShowChangePassword(@AuthenticationPrincipal UserOwnDetail loginUser, Model model) {
		String email = loginUser.getEmail();
		User user = userRepository.findByEmail(email);
		model.addAttribute("user", user);
		return "user/account/changePassword";

	}
	@PostMapping("/user/changepassword/edit")
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
	                return "user/account/changePassword";
	            } else {
	                model.addAttribute("error", "New Password and Confirm Password do not match..!!");
	            }
	        } else {
	            model.addAttribute("error", "Current Password does not match..!!");
	        }
	    } else {
	        model.addAttribute("error", "User not found..!!");
	    }

	    return "user/account/changePassword";
	}
	
	@GetMapping("/user/orderhistory")
	public String ShowOrder(String query,Model model) {
		List<Order> orders;

		if (query != null && !query.isEmpty()) {
			orders = orderRepository.findByOrderNumberContainingIgnoreCase(query.trim());
		} else {
			orders = orderRepository.findAll();
		}
		model.addAttribute("orders", orders);
		return "user/account/orderHistory";

	}
	@GetMapping("/orderhistory/detail/{id}")
	public String viewDetail(@PathVariable("id") Integer id, Model model) {
		List<OrderProducts> orderProducts = orderProductRepository.findByOrderId(id);
		model.addAttribute("orderProducts", orderProducts);
		return "user/account/orderDetail";
	}
	
	@GetMapping("/user/paymenthistory")
	public String showPaymentHistory(String query,Model model) {
		
		List<Payment> payments;

		if (query != null && !query.isEmpty()) {
			payments = paymentRepository.findByTransactionIdContainingIgnoreCase(query.trim());
		} else {
			payments = paymentRepository.findAll();
		}
		model.addAttribute("payments", payments);
		return "user/account/paymentHistory";

	}
	//@GetMapping("/paymenthistory/detail/{id}")
	//public String PaymentHistoryDetail(String query, Model model) {
		
//		List<Payment> payments;
//		if (query != null && !query.isEmpty()) {
//			payments = paymentRepository.findByTransactionIdContainingIgnoreCase(query.trim());
//		} else {
//			payments = paymentRepository.findAll();
//		}
//		model.addAttribute("payments", payments);
//		return "user/account/paymentDetail";
	//}
	
	
}
