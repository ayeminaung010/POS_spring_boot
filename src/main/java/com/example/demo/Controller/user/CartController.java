package com.example.demo.Controller.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;

import com.example.demo.daos.AddressRepository;
import com.example.demo.daos.OrderProductRepository;
import com.example.demo.daos.OrderRepository;
import com.example.demo.daos.PaymentRepository;
import com.example.demo.daos.ProductRepository;
import com.example.demo.dto.CartItem;
import com.example.demo.dto.ResponseHelper;
import com.example.demo.model.Address;
import com.example.demo.model.Order;
import com.example.demo.model.OrderProducts;
import com.example.demo.model.Payment;
import com.example.demo.model.Product;
import com.example.demo.service.CloudinaryImageService;
import com.example.demo.service.MailService;
import com.example.demo.service.UserOwnDetail;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class CartController {
	@Autowired
	HttpSession session;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderProductRepository orderProductRepository;

	@Autowired
	CloudinaryImageService cloudinaryImageService;

	@Autowired
	PaymentRepository paymentRepository;

	@Autowired
	MailService mailService;

	
	@PostMapping("/api/v1/updateCart")
	public ResponseEntity<Object> updateCart(@RequestBody List<CartItem> cartItem) {
		try {
			String jsonStr = objectMapper.writeValueAsString(cartItem);
			session.setAttribute("cart", jsonStr);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		ResponseHelper responseHelper = new ResponseHelper();
		responseHelper.setMessage("success");
		responseHelper.setData(cartItem);
		responseHelper.setStatus(true);
		return ResponseEntity.ok(responseHelper);
	}

	@GetMapping("/api/v1/getCart")
	public ResponseEntity<Object> getCart(Model model) {
		try {
			String cartItemJson = (String) session.getAttribute("cart");
			if (cartItemJson != null) {
				List<CartItem> cartItem = objectMapper.readValue(cartItemJson, new TypeReference<List<CartItem>>() {
				});
				ResponseHelper responseHelper = new ResponseHelper();
				responseHelper.setMessage("success");
				responseHelper.setData(cartItem);
				responseHelper.setStatus(true);
				return ResponseEntity.ok(responseHelper);
			} else {
				return ResponseEntity.status(404).body("Cart not found in session");
			}
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error retrieving cart: " + e.getMessage());
		}
	}

	@GetMapping("/cart")
	public String cart(Model model) {
		// get the cart from session
		try {
			Double totalPrice = 0.0;
			String cartItemJson = (String) session.getAttribute("cart");
			if (cartItemJson != null && !cartItemJson.isEmpty()) {
				List<CartItem> cartItems = objectMapper.readValue(cartItemJson, new TypeReference<List<CartItem>>() {
				});

				for (CartItem cartItem : cartItems) {
					totalPrice += cartItem.getPrice();
				}
				model.addAttribute("cartItems", cartItems);
			}
			model.addAttribute("totalPrice", totalPrice);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "user/cart/index";
	}

	@GetMapping("/cart/payment")
	public String payment(Model model) {
		Payment payment = new Payment();
		model.addAttribute("payment", payment);
		return "user/cart/payment";
	}

	@GetMapping("/cart/address")
	public String address(Model model) {
		calculateTotalPrice(model);
		Address address = new Address();
		model.addAttribute("address", address);
		return "user/cart/address";
	}
	
	@GetMapping("/cart/order-success")
	public String successOrder(Model model) {
		return "user/cart/successOrder";
	}
	
	@GetMapping("/cart/order-failed")
	public String errorOrder(Model model) {
		return "user/cart/errorOrder";
	}

	@PostMapping("/cart/payment/process")
	public String processPayment(@RequestParam(name = "screenshot", required = false) MultipartFile screenshot,
			@ModelAttribute("payment") @Valid Payment payment, BindingResult bindingResult, Model model,@AuthenticationPrincipal UserOwnDetail loginUser) {
		try {
			String imageURL = cloudinaryImageService.uploadFile(screenshot);
			payment.setScreenshot(imageURL);
			session.setAttribute("payment", payment);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		calculateTotalPrice(model);
//		Address address = new Address();
//		model.addAttribute("address", address);
		return "user/cart/address";
	}

	// paypal ajax
	@PostMapping("/cart/payment/paypal_process/old")
	public ResponseEntity<Object> paypalAjax(@RequestBody Payment payment) {
		session.setAttribute("payment", payment);
		ResponseHelper responseHelper = new ResponseHelper();
		responseHelper.setMessage("success");
		responseHelper.setData(payment);
		responseHelper.setStatus(true);
		return ResponseEntity.ok(responseHelper);
	}
	
	@PostMapping("/cart/address/store")
	public String storeAddress(@ModelAttribute("address") @Valid Address address,BindingResult bindingResult,Model model) {
		if (bindingResult.hasErrors()) {
			// If there are validation errors, return to the address form
			calculateTotalPrice(model);
			return "user/cart/address";
		}
		session.setAttribute("address", address); //store address
		return "redirect:/cart/payment";
	}
	

	
	//new order processing with screenshot 
	@PostMapping("/cart/order/payment/processing")
	@Transactional
	public String orderStore(@RequestParam(name = "screenshot", required = false) MultipartFile screenshot,
			@ModelAttribute("payment") @Valid Payment payment, BindingResult bindingResult, Model model,@AuthenticationPrincipal UserOwnDetail loginUser) {
		try {
			String imageURL = cloudinaryImageService.uploadFile(screenshot);
			payment.setScreenshot(imageURL);
			System.out.println("Done uploading...");
			
			double totalPrice = calculateTotalPrice(model);

			String cartItemJson = (String) session.getAttribute("cart");
			if (cartItemJson == null || cartItemJson.isEmpty()) {
				// Handle the case where the cart is empty
				model.addAttribute("error", "Your cart is empty.");
				return "user/cart/address";
			}

			List<CartItem> cartItem = objectMapper.readValue(cartItemJson, new TypeReference<List<CartItem>>() {
			});

			// 1. Payment process (if applicable)
			payment.setStatus("PENDING");
			paymentRepository.save(payment);

			//get address from session
			Address address = (Address) session.getAttribute("address");
			
			// 2. Order process
			Order order = new Order();
			order.setTotalPrice(totalPrice);
			Address savedAddress = addressRepository.save(address); // store address in table
			order.setAddress(savedAddress);
			order.setOrderNumber(generateOrderNumber());
			order.setStatus("PENDING");
			order.setUser(loginUser.getUser());
			order.setPayment(payment);

			// Save the order first
			orderRepository.save(order);

			// 3. Order product process
			List<OrderProducts> orderProductsList = new ArrayList<>();

			for (CartItem cart : cartItem) {
				OrderProducts orderProducts = new OrderProducts();
				Integer productId = Integer.parseInt(cart.getId());
				Product product = productRepository.getReferenceById(productId);

				// Add the product to the set of products in OrderProducts
				orderProducts.setProduct(product);

				// Handle product stock
				int remainingStock = product.getStock() - cart.getQuantity();
				if (remainingStock < 0) {
					// Handle insufficient stock
					model.addAttribute("error", "You can't buy this product .Insufficient stock..! " + product.getName());
					return "user/cart/address";
				}

				orderProducts.setQuantity(cart.getQuantity());
				orderProducts.setTotalPrice(cart.getPrice() * cart.getQuantity());
				orderProducts.setOrderNumber(order.getOrderNumber());
				orderProducts.setOrder(order);
				orderProductsList.add(orderProducts);

			}

			// Save all OrderProducts together
			orderProductRepository.saveAll(orderProductsList);
			// Remove cart from session
			session.removeAttribute("cart");
			session.removeAttribute("payment");
			
			//send mail to customer
			String sub = "Thanks For Your Order Recieved!";
			
			Context context = new Context();
	        context.setVariable("orderNumber", order.getOrderNumber());
	        context.setVariable("transactionId", payment.getTransactionId());
	        context.setVariable("totalPrice", order.getTotalPrice());
	        
	        mailService.sendEmailWithHtmlTemplate(loginUser.getEmail(), sub,"mail/order/recieved", context);

			// add here
			return "user/cart/successOrder";
			
		} catch (Exception e) {
			// Log the error and handle accordingly
			System.out.println("Order Error: " + e);
			model.addAttribute("error", "An error occurred during order processing.");
			return "user/cart/payment";
		}
	}
	
	// paypal ajax new
	@PostMapping("/cart/payment/paypal_process")
	public ResponseEntity<Object> paypalAjax(@RequestBody Payment payment,Model model,@AuthenticationPrincipal UserOwnDetail loginUser) {
		ResponseHelper responseHelper = new ResponseHelper();
		try {
			double totalPrice = calculateTotalPrice(model);

			String cartItemJson = (String) session.getAttribute("cart");
			if (cartItemJson == null || cartItemJson.isEmpty()) {
				// Handle the case where the cart is empty
				responseHelper.setMessage("Your cart is empty.");
				responseHelper.setStatus(false);
				return ResponseEntity.ok(responseHelper);
			}

			List<CartItem> cartItem = objectMapper.readValue(cartItemJson, new TypeReference<List<CartItem>>() {
			});

			// 1. Payment process (if applicable)
			payment.setStatus("PENDING");
			paymentRepository.save(payment);

			//get address from session
			Address address = (Address) session.getAttribute("address");
			
			// 2. Order process
			Order order = new Order();
			order.setTotalPrice(totalPrice);
			Address savedAddress = addressRepository.save(address); // store address in table
			order.setAddress(savedAddress);
			order.setOrderNumber(generateOrderNumber());
			order.setStatus("PENDING");
			order.setUser(loginUser.getUser());
			order.setPayment(payment);

			// Save the order first
			orderRepository.save(order);

			// 3. Order product process
			List<OrderProducts> orderProductsList = new ArrayList<>();

			for (CartItem cart : cartItem) {
				OrderProducts orderProducts = new OrderProducts();
				Integer productId = Integer.parseInt(cart.getId());
				Product product = productRepository.getReferenceById(productId);

				// Add the product to the set of products in OrderProducts
				orderProducts.setProduct(product);

				// Handle product stock
				int remainingStock = product.getStock() - cart.getQuantity();
				if (remainingStock < 0) {
					// Handle insufficient stock
					responseHelper.setMessage("You can't buy this product .Insufficient stock..!" + product.getName());
					responseHelper.setStatus(false);
					return ResponseEntity.ok(responseHelper);
				}

				orderProducts.setQuantity(cart.getQuantity());
				orderProducts.setTotalPrice(cart.getPrice() * cart.getQuantity());
				orderProducts.setOrderNumber(order.getOrderNumber());
				orderProducts.setOrder(order);
				orderProductsList.add(orderProducts);
			}

			// Save all OrderProducts together
			orderProductRepository.saveAll(orderProductsList);
			// Remove cart from session
			session.removeAttribute("cart");
			session.removeAttribute("address");
			
			//send mail to customer
			String sub = "Thanks For Your Order Recieved!";
			
			Context context = new Context();
	        context.setVariable("orderNumber", order.getOrderNumber());
	        context.setVariable("transactionId", payment.getTransactionId());
	        context.setVariable("totalPrice", order.getTotalPrice());
	        
	        mailService.sendEmailWithHtmlTemplate(loginUser.getEmail(), sub,"mail/order/recieved", context);

			// add here
	        responseHelper.setMessage("success");
			responseHelper.setData(payment);
			responseHelper.setStatus(true);
	        return ResponseEntity.ok(responseHelper);
			
		} catch (Exception e) {
			// Log the error and handle accordingly
			System.out.println("Order Error: " + e);
			responseHelper.setMessage("An error occurred during order processing.");
			responseHelper.setStatus(false);
			return ResponseEntity.ok(responseHelper);
		}
		
	}
	
	// calculate total price
	public Double calculateTotalPrice(Model model) {
		try {
			String cartItemJson = (String) session.getAttribute("cart");
			Double totalPrice = 0.0;
			Double deliveryFee = 5000.0;
			if (cartItemJson != null) {
				List<CartItem> cartItem = objectMapper.readValue(cartItemJson, new TypeReference<List<CartItem>>() {
				});

				for (CartItem cart : cartItem) {
					Double productTotalPrice = cart.getPrice() * cart.getQuantity();
					totalPrice += productTotalPrice;
				}
				totalPrice = totalPrice + deliveryFee;
				model.addAttribute("totalPrice", totalPrice);
				return totalPrice;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return 0.0;
	}

	// generate order number
	public static String generateOrderNumber() {
		// Use the current timestamp
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

		// Generate a random 5-digit number
		Random random = new Random();
		int randomDigits = random.nextInt(100000);

		// Combine timestamp and random number to create the order number
		return "ORDER_POS_" + timestamp + String.format("%05d", randomDigits);
	}

	

}
