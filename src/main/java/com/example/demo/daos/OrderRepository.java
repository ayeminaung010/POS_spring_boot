package com.example.demo.daos;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Order;

@Repository
public interface OrderRepository  extends JpaRepository<Order, Integer>{
	Page<Order> findByStatus(String status,Pageable pageable);
	
	Page<Order> findByOrderNumberContainingIgnoreCase(String query,Pageable pageable);
	Page<Order> findAll(Pageable pageable);
	
	Page<Order> findByOrderNumberContainingIgnoreCaseAndStatus(String orderNumber, String status,Pageable pageable);
	
	Page<Order> findByUserId(Integer id,Pageable pageable);
}
