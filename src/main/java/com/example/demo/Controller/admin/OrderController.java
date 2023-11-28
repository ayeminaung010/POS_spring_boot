package com.example.demo.Controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.daos.OrderProductRepository;
import com.example.demo.daos.OrderRepository;
import com.example.demo.model.Order;
import com.example.demo.model.OrderProducts;

@Controller
public class OrderController {
	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderProductRepository orderProductRepository;

	@GetMapping("/order/all")
	public String viewAll(@RequestParam(name = "search", required = false) String query, Model model) {
		List<Order> orders;

		if (query != null && !query.isEmpty()) {
			orders = orderRepository.findByOrderNumberContainingIgnoreCase(query.trim());
		} else {
			orders = orderRepository.findAll();
		}
		model.addAttribute("orders", orders);
		return "admin/order-manage/index";
	}

	@GetMapping("/order/pending")
	public String viewPending(@RequestParam(name = "search", required = false) String query,Model model) {
		List<Order> orders;

		if (query != null && !query.isEmpty()) {
			orders = orderRepository.findByOrderNumberContainingIgnoreCaseAndStatus(query.trim(),"PENDING");
		} else {
			orders = orderRepository.findByStatus("PENDING");
		}
		model.addAttribute("orders", orders);
		return "admin/order-manage/pending";
	}

	@GetMapping("/order/success")
	public String viewSuccess(@RequestParam(name = "search", required = false) String query,Model model) {
		List<Order> orders;

		if (query != null && !query.isEmpty()) {
			orders = orderRepository.findByOrderNumberContainingIgnoreCaseAndStatus(query.trim(),"SUCCESS");
		} else {
			orders = orderRepository.findByStatus("SUCCESS");
		}
		model.addAttribute("orders", orders);
		return "admin/order-manage/success";
	}

	@GetMapping("/order/reject")
	public String viewReject(@RequestParam(name = "search", required = false) String query,Model model) {
		List<Order> orders;

		if (query != null && !query.isEmpty()) {
			orders = orderRepository.findByOrderNumberContainingIgnoreCaseAndStatus(query.trim(),"REJECTED");
		} else {
			orders = orderRepository.findByStatus("REJECTED");
		}
		model.addAttribute("orders", orders);
		return "admin/order-manage/reject";
	}

	@GetMapping("/order/detail/{id}")
	public String viewDetail(@PathVariable("id") Integer id, Model model) {
		List<OrderProducts> orderProducts = orderProductRepository.findByOrderId(id);
		model.addAttribute("orderProducts", orderProducts);
		return "admin/order-product/index";
	}
}
