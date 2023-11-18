//package com.example.demo.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import com.example.demo.daos.UserRepository;
//import com.example.demo.model.User;
//
//import jakarta.transaction.Transactional;
//
//@Component
//public class AppUserInitializer {
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//	
//	@Autowired 
//	private UserRepository userRepository;
//	
//	@Transactional
//	@EventListener(classes = ContextRefreshedEvent.class)
//	public void initializeUser() {
//		if(userRepository.count() == 0) {
//			User user = new User();
//			user.setEmail("admin@gmail.com");
//			user.setName("Admin Test");
//			user.setPassword(passwordEncoder.encode("admin"));
//			user.setRole("ADMIN");
//			userRepository.save(user);
//		}
//	}
//}	
