package com.example.demo.Controller.user;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.daos.ContactRepository;
import com.example.demo.daos.ProductRepository;
import com.example.demo.model.Contact;
import com.example.demo.model.Product;
import com.example.demo.model.SubCategory;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {
	@Autowired
	ProductRepository productRepository;

	@Autowired
	HttpSession session;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	ContactRepository contactRepo;

	@GetMapping("/shop")
	public String shop(@RequestParam(name = "search", required = false) String query,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "9") int size,
			@RequestParam(defaultValue = "createdTime") String sortBy, Model model) {
		Page<Product> productPage;
		PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sortBy).descending());
		if (query != null && !query.isEmpty()) {
			productPage = productRepository.searchProducts(query.trim(), pageRequest);
		} else {
			productPage = productRepository.findAll(pageRequest);
		}
		List<Product> products = productPage.getContent();
		model.addAttribute("products", products);
		model.addAttribute("currentPage", productPage.getNumber() + 1);
		model.addAttribute("totalPages", productPage.getTotalPages());
		return "user/shop/index";
	}

	@GetMapping("/shop/detail/{id}")
	public String shopDetail(@PathVariable("id") Integer id, Model model) {
		Product detailProduct = productRepository.getReferenceById(id);
		Double discount = detailProduct.getDiscount();
		if (discount != 0.0 && discount >= 1.0) {
			double discountPrice = calculateDiscountPrice(discount, detailProduct.getPrice());
			model.addAttribute("discountPrice", discountPrice);
		}
		SubCategory subCategory = detailProduct.getSubCategory();
		if (subCategory != null) {
			Set<Product> relatedProducts = subCategory.getProducts();
			model.addAttribute("relatedProducts", relatedProducts);
		}
		model.addAttribute("product", detailProduct);
		return "user/shop/detail";
	}

	@GetMapping("/shop/{subCategory}/{id}")
	public String subCategoryProducts(@PathVariable("subCategory") String subCategory,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "9") int size,
			@RequestParam(defaultValue = "createdTime") String sortBy,
			@PathVariable("id") Integer subCategoryId, Model model) {
		
		Page<Product> productPage;
		PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sortBy).descending());
		productPage = productRepository.findBySubCategorySubCategoryName(subCategory,pageRequest);
		
		List<Product> sortProducts = productPage.getContent();
		for (Product product : sortProducts) {
			double discount = product.getDiscount();
			if (discount != 0.0) {
				double discountPrice = calculateDiscountPrice(discount, product.getPrice());
				product.setDiscountPrice(discountPrice);
			}
		}
		model.addAttribute("sortProducts", sortProducts);
		return "user/products/index";
	}

	@GetMapping("/contact")
	public String contact(Model model) {
		Contact contact = new Contact();
		model.addAttribute("contact", contact);
		return "user/contact/index";
	}

	@PostMapping("/contact/message")
	public String SaveContact(@ModelAttribute("contact") Contact contact, Model model,
			RedirectAttributes redirectAttributes) {
		contactRepo.save(contact);
		redirectAttributes.addFlashAttribute("success", "Message send successful...!!");
		System.out.print("contact" + contact);
		return "redirect:/contact";

	}
	

	// calculate discount price
	public Double calculateDiscountPrice(double discount, double originalPrice) {
		if (discount < 0 || discount > 100) {
			return null;
		}
		double discountAmount = (originalPrice * discount) / 100;
		double discountedPrice = originalPrice - discountAmount;

		return discountedPrice;
	}

}
