package com.example.demo.Controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
	public String viewAll(@RequestParam(name = "search", required = false) String query,
			@RequestParam(defaultValue = "1") int page,
	         @RequestParam(defaultValue = "10") int size,
	         @RequestParam(defaultValue = "createdTime") String sortBy,
	         Model model) {
		Page<Order> orderPage;
		PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sortBy).descending());
		if (query != null && !query.isEmpty()) {
			orderPage = orderRepository.findByOrderNumberContainingIgnoreCase(query.trim(), pageRequest);
		} else {
			orderPage = orderRepository.findAll(pageRequest);
		}
		List<Order> orders = orderPage.getContent();
		model.addAttribute("orders", orders);
		model.addAttribute("currentPage", orderPage.getNumber() + 1);
	    model.addAttribute("totalPages", orderPage.getTotalPages());
		return "admin/order-manage/index";
	}

	@GetMapping("/order/pending")
	public String viewPending(@RequestParam(name = "search", required = false) String query,
			@RequestParam(defaultValue = "1") int page,
	         @RequestParam(defaultValue = "10") int size,
	         @RequestParam(defaultValue = "createdTime") String sortBy,
	         Model model) {
		Page<Order> orderPage;
		PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sortBy).descending());
		if (query != null && !query.isEmpty()) {
			orderPage = orderRepository.findByOrderNumberContainingIgnoreCaseAndStatus(query.trim(),"PENDING", pageRequest);
		} else {
			orderPage = orderRepository.findByStatus("PENDING",pageRequest);
		}
		List<Order> orders = orderPage.getContent();
		model.addAttribute("orders", orders);
		model.addAttribute("currentPage", orderPage.getNumber() + 1);
	    model.addAttribute("totalPages", orderPage.getTotalPages());
		return "admin/order-manage/pending";
	}

	@GetMapping("/order/success")
	public String viewSuccess(@RequestParam(name = "search", required = false) String query,
			@RequestParam(defaultValue = "1") int page,
	         @RequestParam(defaultValue = "10") int size,
	         @RequestParam(defaultValue = "createdTime") String sortBy,
	         Model model) {
		Page<Order> orderPage;
		PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sortBy).descending());
		if (query != null && !query.isEmpty()) {
			orderPage = orderRepository.findByOrderNumberContainingIgnoreCaseAndStatus(query.trim(),"SUCCESS", pageRequest);
		} else {
			orderPage = orderRepository.findByStatus("SUCCESS",pageRequest);
		}
		List<Order> orders = orderPage.getContent();
		model.addAttribute("orders", orders);
		model.addAttribute("currentPage", orderPage.getNumber() + 1);
	    model.addAttribute("totalPages", orderPage.getTotalPages());
		return "admin/order-manage/success";
	}

	@GetMapping("/order/reject")
	public String viewReject(@RequestParam(name = "search", required = false) String query,
			@RequestParam(defaultValue = "1") int page,
	         @RequestParam(defaultValue = "10") int size,
	         @RequestParam(defaultValue = "createdTime") String sortBy,
	         Model model) {
		Page<Order> orderPage;
		PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sortBy).descending());
		if (query != null && !query.isEmpty()) {
			orderPage = orderRepository.findByOrderNumberContainingIgnoreCaseAndStatus(query.trim(),"REJECTED", pageRequest);
		} else {
			orderPage = orderRepository.findByStatus("REJECTED",pageRequest);
		}
		List<Order> orders = orderPage.getContent();
		model.addAttribute("orders", orders);
		model.addAttribute("currentPage", orderPage.getNumber() + 1);
	    model.addAttribute("totalPages", orderPage.getTotalPages());
		return "admin/order-manage/reject";
	}

	@GetMapping("/order/detail/{id}")
	public String viewDetail(@PathVariable("id") Integer id, Model model) {
		List<OrderProducts> orderProducts = orderProductRepository.findByOrderId(id);
		Order order = orderRepository.getReferenceById(id);
		model.addAttribute("orderProducts", orderProducts);
		model.addAttribute("order", order);
		System.out.println(order.getPayment().getPaymentType());
		return "admin/order-product/index";
	}
}
