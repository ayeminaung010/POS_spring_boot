package com.example.demo.daos;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.User;

public interface UserRepository  extends JpaRepository<User, Integer>{
	User findByEmail(String email);
}
