package com.example.demo.Controller.user;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.daos.ContactRepository;
import com.example.demo.daos.ProductRepository;
import com.example.demo.model.Contact;
import com.example.demo.model.Product;
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
	
	@Autowired ContactRepository contactRepo;
	
	@GetMapping("/shop")
	public String shop() {
		return "user/shop/index";
	}
	
	@GetMapping("/shop/detail/{id}") 
	public String shopDetail(@PathVariable("id") Integer id,Model model) {
		Product detailProduct = productRepository.getReferenceById(id);
		Double discount = detailProduct.getDiscount();
		if(discount != 0.0) {
			double discountPrice = calculateDiscountPrice(discount,detailProduct.getPrice());
			model.addAttribute("discountPrice", discountPrice);
		}
		model.addAttribute("product", detailProduct);
		return "user/shop/detail";
	}
	
	@GetMapping("/shop/{subCategory}/{id}")
	public String subCategoryProducts(@PathVariable("subCategory") String subCategory,@PathVariable("id") Integer subCategoryId,Model model) {
		List<Product> sortProducts = productRepository.findBySubCategorySubCategoryName(subCategory);
		for (Product product : sortProducts) {
	        double discount = product.getDiscount();
	        if(discount != 0.0) {
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
		model.addAttribute("contact",contact);
		return "user/contact/index";
	}
	
	@PostMapping("/contact/message")
	public String SaveContact(@ModelAttribute("contact") Contact contact, Model model, RedirectAttributes redirectAttributes) {
		contactRepo.save(contact);
		redirectAttributes.addFlashAttribute("success", "Message send successful...!!");
		System.out.print("contact" + contact);
		return "redirect:/contact";
		
	}
	
	
	//calculate discount price
	public Double calculateDiscountPrice(double discount,double originalPrice) {
		if (discount < 0 || discount > 100) {
            return null;
        }
		double discountAmount = (originalPrice * discount) / 100;
        double discountedPrice = originalPrice - discountAmount;

		return discountedPrice;
	}
	
	
	
}
