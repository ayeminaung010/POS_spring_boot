package com.example.demo;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.daos.CategoryRepository;
import com.example.demo.daos.UserRepository;
import com.example.demo.model.User;

@SpringBootApplication
public class PoSprojectApplication {
	@Autowired
	UserRepository userRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	PasswordEncoder encoder;
	
	public static void main(String[] args) {
		SpringApplication.run(PoSprojectApplication.class, args);
	}



	@Bean
	CommandLineRunner runner() {
		return args -> {
			
//        	//admin account
//        	User admin = new User();
//            admin.setEmail("admin@gmail.com");
//            admin.setName("Admin");
//            admin.setPassword(encoder.encode("admin"));
//            admin.setConfirmPassword(encoder.encode("admin"));
//            admin.setRole("ADMIN");
//            userRepository.save(admin);
//        	
//        	//user account
//            User user = new User();
//        	user.setEmail("user@gmail.com");
//        	user.setName("User");
//        	user.setPassword(encoder.encode("user"));
//        	user.setConfirmPassword(encoder.encode("user"));
//        	user.setRole("USER");
//        	userRepository.save(user);

	
		};

	}
}
