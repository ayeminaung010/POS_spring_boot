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

import com.example.demo.daos.CategoryRepository;
import com.example.demo.daos.SubCategoryRepository;
import com.example.demo.model.Category;
import com.example.demo.model.SubCategory;

import jakarta.validation.Valid;

@Controller
public class SubCategoryController {
	@Autowired
	private SubCategoryRepository subcategoryRepo;
	@Autowired
	private CategoryRepository categoryRepo;

	@GetMapping("/subcategory")
	public String viewSubCategory(Model model) {
		List<SubCategory> subcategories = subcategoryRepo.findAll();
		model.addAttribute("subcategories", subcategories);
		return "admin/subCategory/index";
	}

	@GetMapping("/subcategory/add")
	public String addSubCategory(Model model) {
		SubCategory subcategory = new SubCategory();
		model.addAttribute("subcategory", subcategory);
		List<Category> categoryList = categoryRepo.findAll();
		model.addAttribute("categoryList", categoryList);

		return "admin/subCategory/add";
	}

	@PostMapping("/subcategory/add")
	public String addSubCategory(@ModelAttribute("subcategory") @Valid SubCategory subcategory, BindingResult bindingResult, 
			Model model,RedirectAttributes redirectAttributes) {
		
		if (bindingResult.hasErrors()) {
			
			List<Category> categoryList = categoryRepo.findAll();
			model.addAttribute("categoryList", categoryList);
			
			return "admin/subCategory/add";
		}
		SubCategory existingsubcategory = subcategoryRepo.findBySubCategoryName(subcategory.getSubCategoryName());
		if (existingsubcategory != null) {
			
			List<Category> categoryList = categoryRepo.findAll();
			model.addAttribute("categoryList", categoryList);
			
			bindingResult.rejectValue("subCategoryName", "error.subcategory", "SubCategory with this name already exists");
			return "admin/subCategory/add";
		}
		subcategoryRepo.save(subcategory);
		redirectAttributes.addFlashAttribute("success", "SubCategory Add successful!!");
		return "redirect:/subcategory";
	}

	@GetMapping("/subcategory/delete/{id}")
	public String deleteSubCategory(@PathVariable("id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		subcategoryRepo.deleteById(id);
		redirectAttributes.addFlashAttribute("success", "SubCategory Delete successful!!");
		return "redirect:/subcategory";
	}

	@GetMapping("/subcategory/update/{id}")
	public String updateSubCategory(@PathVariable("id") Integer id, Model model) {

		SubCategory subcategory = subcategoryRepo.getReferenceById(id);
		model.addAttribute("subcategory", subcategory);

		List<Category> categoryList = categoryRepo.findAll();
		model.addAttribute("categoryList", categoryList);

		return "admin/subCategory/update";
	}

	@PostMapping("/subcategory/update/{id}")
	public String updateSubCategory(@ModelAttribute("subcategory") @Valid SubCategory subcategory, BindingResult bindingResult, @PathVariable("id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		
		if (bindingResult.hasErrors()) {
			List<Category> categoryList = categoryRepo.findAll();
			model.addAttribute("categoryList", categoryList);
			return "admin/subCategory/update";
		}
		SubCategory existingsubcategory = subcategoryRepo.findBySubCategoryName(subcategory.getSubCategoryName());
		
		if (existingsubcategory != null) {
			if (id != existingsubcategory.getSubCategoryId()) {
				
				List<Category> categoryList = categoryRepo.findAll();
				model.addAttribute("categoryList", categoryList);
				
				bindingResult.rejectValue("subCategoryName", "error.subcategory", "SubCategory with this name already exists");
				return "admin/subCategory/update";
			}
		}
		
		
		SubCategory subcategoryById = subcategoryRepo.getReferenceById(id);
		subcategoryById.setSubCategoryName(subcategory.getSubCategoryName());
		subcategoryById.setCategory(subcategory.getCategory());
		subcategoryRepo.save(subcategoryById);

		redirectAttributes.addFlashAttribute("success", "SubCategory Update successful!!");
		return "redirect:/subcategory";
	}

}
