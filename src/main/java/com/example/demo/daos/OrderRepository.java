package com.example.demo.daos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Order;

@Repository
public interface OrderRepository  extends JpaRepository<Order, Integer>{
	List<Order> findByStatus(String status);
	
	List<Order> findByOrderNumberContainingIgnoreCase(String query);
	
	List<Order> findByOrderNumberContainingIgnoreCaseAndStatus(String orderNumber, String status);
}
