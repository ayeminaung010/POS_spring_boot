package com.example.demo.Controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.daos.PaymentTypeRepository;
import com.example.demo.model.PaymentType;

import jakarta.validation.Valid;

@Controller
public class PaymentTypeController {
	@Autowired
	private PaymentTypeRepository paymentTypeRepo;

	@GetMapping("/paymenttype")
	public String viewPaymenttype(Model model) {
		List<PaymentType> paymentTypeList = paymentTypeRepo.findAll();
		model.addAttribute("paymentTypeList", paymentTypeList);
		PaymentType paymenttype = new PaymentType();
		model.addAttribute("paymenttype", paymenttype);
		return "admin/paymenttype/index";
	}

	@GetMapping("/paymenttype/add")
	public String addPaymenttype(Model model) {
		PaymentType paymenttype = new PaymentType();
		model.addAttribute("paymenttype", paymenttype);
		return "admin/paymenttype/add";
	}

	@PostMapping("/paymenttype/save")
	public String creatPaymenttype(@ModelAttribute("paymenttype") @Valid PaymentType paymenttype,
			BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
		
		if (bindingResult.hasErrors()) {
			return "admin/paymenttype/add";
		}
		PaymentType existingPaymentType = paymentTypeRepo.findByPaymentTypeName(paymenttype.getPaymentTypeName());
		if (existingPaymentType != null) {
			bindingResult.rejectValue("paymentTypeName", "error.paymenttype",
					"PaymentType with this name already exists");
			return "admin/paymenttype/add";
		}
		paymentTypeRepo.save(paymenttype);
		redirectAttributes.addFlashAttribute("success", "Payment-Type Add successful!!");

		return "redirect:/paymenttype";
	}
	
	@PostMapping("/paymenttype/delete")
	public String deleteCategory(@ModelAttribute("paymenttype") PaymentType paymenttype, Model model, RedirectAttributes redirectAttributes) {
		
		paymentTypeRepo.deleteById(paymenttype.getPaymentTypeId());
		redirectAttributes.addFlashAttribute("success", "Payment-Type Delete successful!!");
		return "redirect:/paymenttype";
	}

	@GetMapping("/paymenttype/update/{id}")
	public String updatePaymenttype(@PathVariable("id") Integer id, Model model) {

		PaymentType paymenttype = paymentTypeRepo.getReferenceById(id);
		model.addAttribute("paymenttype", paymenttype);
		return "admin/paymenttype/update";
	}

	@PostMapping("/paymenttype/update/{id}")
	public String updateBrand(@ModelAttribute("paymenttype") @Valid PaymentType paymenttype,
			BindingResult bindingResult, @PathVariable("id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return "admin/paymenttype/update";
		}

		PaymentType existingPaymentType = paymentTypeRepo.findByPaymentTypeName(paymenttype.getPaymentTypeName());
		
		if (existingPaymentType != null) {
			if (id != existingPaymentType.getPaymentTypeId()) {
				bindingResult.rejectValue("paymentTypeName", "error.paymenttype", "PaymentType with this name already exists");
				return "admin/paymentType/update";
			}
		}
		PaymentType paymentTypeById = paymentTypeRepo.getReferenceById(id);
		paymentTypeById.setPaymentTypeName(paymenttype.getPaymentTypeName());

		paymentTypeRepo.save(paymentTypeById);
		redirectAttributes.addFlashAttribute("success", "PaymentType Update successful!!");
		return "redirect:/paymenttype";
	}

}
