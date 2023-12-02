package com.example.demo.daos;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

@Repository
public interface UserRepository  extends JpaRepository<User, Integer>{
	User findByEmail(String email);

	User findByName(String name);
	
	@Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%'))")
	List<User> searchUser(String query);

	Page<User> findByNameContainingIgnoreCase(String trim, Pageable pageable);
	Page<User> findAll(Pageable pageable);
}
