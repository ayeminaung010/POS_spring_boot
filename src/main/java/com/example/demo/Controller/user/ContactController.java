package com.example.demo.Controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.daos.ContactRepository;
import com.example.demo.model.Contact;

@Controller
public class ContactController {
	@Autowired ContactRepository contactRepo;
	
	@GetMapping("/contactmessage") 
	public String showMessage(@RequestParam(name = "search", required = false) String query,
			@RequestParam(defaultValue = "1") int page,
	         @RequestParam(defaultValue = "10") int size,
	         @RequestParam(defaultValue = "createdTime") String sortBy,
			Model model) {
		
		Page<Contact> contactPage;
		
		PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sortBy).descending());
		if (query != null && !query.isEmpty()) {
			contactPage = contactRepo.findByNameContainingIgnoreCase(query.trim(), pageRequest);
		} else {
			contactPage = contactRepo.findAll(pageRequest);
		}
		List<Contact> contactList = contactPage.getContent();
		model.addAttribute("contactList", contactList);
		Contact contact = new Contact();
		model.addAttribute("contact", contact);
		model.addAttribute("currentPage", contactPage.getNumber() + 1);
	    model.addAttribute("totalPages", contactPage.getTotalPages());
		
		return "/admin/contactMessage/index";
	}
}
