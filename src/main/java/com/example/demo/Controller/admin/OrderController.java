package com.example.demo.Controller.admin;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.daos.OrderRepository;
import com.example.demo.model.Order;


@Controller
public class OrderController {
	@Autowired
	OrderRepository orderRepository;
	
	@GetMapping("/order/all")
	public String viewAll(Model model) {
		List<Order> orders = orderRepository.findAll();
		model.addAttribute("orders",orders);
		return "admin/order-manage/index";
	}
	
	@GetMapping("/order/pending")
	public String viewPending(Model model) {
		List<Order> orders = orderRepository.findByStatus("PENDING");
		model.addAttribute("orders",orders);
		return "admin/order-manage/pending";
	}
	
	@GetMapping("/order/success")
	public String viewSuccess(Model model) {
		List<Order> orders = orderRepository.findByStatus("SUCCESS");
		model.addAttribute("orders",orders);
		return "admin/order-manage/success";
	}
	
	@GetMapping("/order/reject")
	public String viewReject(Model model) {
		List<Order> orders = orderRepository.findByStatus("REJECTED");
		model.addAttribute("orders",orders);
		return "admin/order-manage/reject";
	}
}
