package com.example.demo.Controller.admin;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.context.Context;

import com.example.demo.daos.UserRepository;
import com.example.demo.model.User;
import com.example.demo.service.MailService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class AuthController {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	HttpSession session;

	@Autowired
	MailService mailService;
	
	@GetMapping("/")
	public String slahPage(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "user/home";
	}

	@GetMapping("/login")
	public String login(Model model) {
		User user = new User();
		model.addAttribute(user);
		return "/login";
	}

	@GetMapping("/signup")
	public String SignUp(Model model) {
		User user = new User();
		model.addAttribute(user);
		return "/signup";
	}

	@PostMapping("/signup")
	public String userRegister(@Valid User user, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "/signup";
		}
		User alreadyUser = userRepository.findByEmail(user.getEmail());
		if (alreadyUser == null) {
			if (user.getPassword().equals(user.getConfirmPassword())) {
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

				String encodePassword = passwordEncoder.encode(user.getPassword());
				user.setPassword(encodePassword);
				userRepository.save(user);
				redirectAttributes.addFlashAttribute("success", "Register Success ... !");
				return "/user/login";
			} else {
				model.addAttribute("error", "Password does not match.. !");
				return "/signup";
			}
		}
		model.addAttribute("error", "Email already exits.. !");
		return "/signup";

	}

	// forgot password feature
	@GetMapping("/auth/forgotPassword")
	public String forgotPassword(Model model) {
		User user = new User();
		model.addAttribute(user);
		return "/auth/forgot-password";
	}

	@PostMapping("/auth/sendOTP")
	public String sendOTPcode(@ModelAttribute("user") User user, Model model) {
		
		String OTP = generateNumericOtp();
		session.setAttribute("otpCode", OTP); //
		//find email here
		String email = user.getEmail();
		User existUser = userRepository.findByEmail(email);
		if(existUser != null) {
			// send mail to customer
			String sub = "One time Password Code..!";

			Context context = new Context();
			context.setVariable("OTPcode", OTP);

			mailService.sendEmailWithHtmlTemplate(email, sub, "mail/otp-mail", context);
			model.addAttribute("user",existUser);
			model.addAttribute("success","OTP code sent successfully.Please check your mail box!");
			return "/auth/otp";
		}else {
			model.addAttribute("error", "User not found with this email address.");
			return "/auth/forgot-password";
		}
	}

	// OTP code page
	@PostMapping("/auth/otp/confirm")
	public String otpPage(@ModelAttribute("user") User user,Model model) {
		String sessionOTPcode = (String) session.getAttribute("otpCode");
		model.addAttribute("user", user);
		if(sessionOTPcode.equals(user.getOtpCode())) {
			return "auth/reset-password";
		}
		model.addAttribute("error","Invalid OTP code..!");
		return "/auth/otp";
	}

	// new password page
	@PostMapping("/auth/new-password")
	public String newPasswordPage(@ModelAttribute("user") User user,Model model) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		User existUser = userRepository.getReferenceById(user.getId());
		if (user.getPassword().equals(user.getConfirmPassword())) {
			// Hash the new password and save it
			String encodedPassword = passwordEncoder.encode(user.getPassword());
			existUser.setPassword(encodedPassword);
			userRepository.save(existUser);

			model.addAttribute("success", "Changed Password Success ... !");
			return "/login";
		} else {
			model.addAttribute("error", "New Password and Confirm Password do not match..!!");
			return "/auth/reset-password";
		}
	}

	// generate otp code
	public static String generateNumericOtp() {
		// Choose a secure random number generator
		SecureRandom random = new SecureRandom();

		// Generate a 6-digit numeric OTP
		int otp = 100000 + random.nextInt(900000);

		return String.valueOf(otp);
	}

}
