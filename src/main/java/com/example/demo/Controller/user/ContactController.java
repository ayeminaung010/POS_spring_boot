package com.example.demo.Controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.daos.ContactRepository;
import com.example.demo.model.Contact;

@Controller
public class ContactController {
	@Autowired ContactRepository contactRepo;
	
	@GetMapping("/contactmessage") 
	public String showMessage(Model model) {
		List<Contact> messageList = contactRepo.findAll();
		model.addAttribute("messageList", messageList);
		Contact contact = new Contact();
		model.addAttribute("contact", contact);
		return "/admin/contactMessage/index";
	}
}
