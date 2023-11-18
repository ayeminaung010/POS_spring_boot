package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.daos.UserRepository;
import com.example.demo.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class AppUserDetailService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private HttpServletRequest request;

	@Autowired
	PasswordEncoder passwordEncoder;
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		HttpSession session = request.getSession();
		User user = userRepository.findByEmail(email);
		if (user == null) {
			System.out.println("not found user : " + email);
			throw new UsernameNotFoundException("Couldn't find user!" + email);
		} else {
			session.setAttribute("user", user);
			UserOwnDetail userOwnDetail = new UserOwnDetail(user);
			System.out.println("user found : " + userOwnDetail.getUser().getRole() + " :" + passwordEncoder.encode(userOwnDetail.getUser().getPassword()) );
			return userOwnDetail;
		}
	}
}
